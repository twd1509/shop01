package com.example.demoShop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoShop.dto.CartDTO;
import com.example.demoShop.dto.CategoryDTO;
import com.example.demoShop.dto.OrderCancelDTO;
import com.example.demoShop.dto.OrderDTO;
import com.example.demoShop.dto.ProductDTO;
import com.example.demoShop.mapper.CategoryMapper;
import com.example.demoShop.mapper.OrderCancelMapper;
import com.example.demoShop.mapper.OrderMapper;
import com.example.demoShop.mapper.ProductMapper;

@Service
public class OrderService {
	private final OrderMapper orderMapper;
	private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final CartService cartService;
    private final OrderCancelMapper orderCancelMapper;

    public OrderService(OrderMapper orderMapper, CategoryMapper categoryMapper, ProductMapper productMapper, 
    					CartService cartService, OrderCancelMapper orderCancelMapper) {
        this.orderMapper = orderMapper;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.cartService = cartService;
        this.orderCancelMapper = orderCancelMapper;
    }

    @Transactional
    public int insertOrder(OrderDTO orderDTO) {
        if (orderDTO.getMemberNo() <= 0 || orderDTO.getProductNo() <= 0) {
            throw new IllegalArgumentException("회원번호와 상품번호는 필수입니다.");
        }
        if (orderDTO.getCount() <= 0) {
            throw new IllegalArgumentException("주문 수량은 1 이상이어야 합니다.");
        }

        ProductDTO product = productMapper.selectProductByNo(orderDTO.getProductNo()).get(0);
        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }
        if (!"판매중".equals(product.getState())) {
            throw new IllegalArgumentException("현재 판매중인 상품이 아닙니다. 상태: " + product.getState());
        }
        if (product.getStock() < orderDTO.getCount()) {
            throw new IllegalArgumentException("재고가 부족합니다. 현재 재고: " + product.getStock());
        }
        if (product.getMinCount() > orderDTO.getCount()) {
            throw new IllegalArgumentException("최소 주문 수량(" + product.getMinCount() + ") 이상이어야 합니다.");
        }
        if (product.getMaxCount() > 0 && product.getMaxCount() < orderDTO.getCount()) {
            throw new IllegalArgumentException("최대 주문 수량(" + product.getMaxCount() + ") 이하여야 합니다.");
        }

        // 카테고리 상태 검증
        CategoryDTO category = categoryMapper.selectCategoryByNo(product.getCateNo()).get(0);
        if (category == null || !"Y".equals(category.getState())) {
            throw new IllegalArgumentException("카테고리 " + (category != null ? category.getTitle() : "알 수 없음") + "는 활성 상태가 아닙니다.");
        }

        orderDTO.setTitle(product.getTitle());
        orderDTO.setProductPrice(product.getPrice());
        orderDTO.setDeliPrice(product.getDeliPrice());
        orderDTO.setTotalPrice(product.getPrice() * orderDTO.getCount() + product.getDeliPrice());

        product.setStock(product.getStock() - orderDTO.getCount());
        int stockUpdateResult = productMapper.updateProductStock(product);
        if (stockUpdateResult <= 0) {
            throw new IllegalArgumentException("재고 업데이트에 실패했습니다.");
        }

        int orderResult = orderMapper.insertOrder(orderDTO);
        if (orderResult <= 0) {
            throw new IllegalArgumentException("주문 생성에 실패했습니다.");
        }

        CartDTO cart = cartService.selectCartByMemberAndProduct(orderDTO.getMemberNo(), orderDTO.getProductNo());
        if (cart != null) {
            cartService.deleteCartByMemberAndProduct(orderDTO.getMemberNo(), orderDTO.getProductNo());
        }

        return orderResult;
    }
    
    @Transactional
    public List<OrderDTO> insertOrderFromCart(int memberNo, OrderDTO orderDTO) {
        if (memberNo <= 0) {
            throw new IllegalArgumentException("회원번호는 필수입니다.");
        }

        List<CartDTO> carts = cartService.selectCartsByMemberNo(memberNo);
        if (carts.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }

        List<OrderDTO> orders = new ArrayList<>();
        int totalPrice = 0;

        for (CartDTO cart : carts) {
            ProductDTO product = productMapper.selectProductByNo(cart.getProductNo()).get(0);
            if (product == null || !"판매중".equals(product.getState())) {
                continue; // 삭제된 상품 또는 판매중이 아닌 상품은 건너뜀
            }
            if (product.getStock() < cart.getCount()) {
                throw new IllegalArgumentException("상품 " + product.getTitle() + "의 재고가 부족합니다. 현재 재고: " + product.getStock());
            }
            if (product.getMinCount() > cart.getCount()) {
                throw new IllegalArgumentException("상품 " + product.getTitle() + "의 최소 주문 수량(" + product.getMinCount() + ") 이상이어야 합니다.");
            }
            if (product.getMaxCount() > 0 && product.getMaxCount() < cart.getCount()) {
                throw new IllegalArgumentException("상품 " + product.getTitle() + "의 최대 주문 수량(" + product.getMaxCount() + ") 이하여야 합니다.");
            }

            // 카테고리 상태 검증
            CategoryDTO category = categoryMapper.selectCategoryByNo(product.getCateNo()).get(0);
            if (category == null || !"Y".equals(category.getState())) {
                throw new IllegalArgumentException("카테고리 " + (category != null ? category.getTitle() : "알 수 없음") + "는 활성 상태가 아닙니다.");
            }

            OrderDTO singleOrder = new OrderDTO();
            singleOrder.setMemberNo(memberNo);
            singleOrder.setProductNo(cart.getProductNo());
            singleOrder.setCount(cart.getCount());
            singleOrder.setTitle(product.getTitle());
            singleOrder.setProductPrice(product.getPrice());
            singleOrder.setDeliPrice(product.getDeliPrice());
            singleOrder.setTotalPrice(product.getPrice() * cart.getCount() + product.getDeliPrice());
            singleOrder.setOrderId(orderDTO.getOrderId());
            singleOrder.setTid(orderDTO.getTid());
            singleOrder.setPayType(orderDTO.getPayType());
            singleOrder.setPayPrice(product.getPrice() * cart.getCount() + product.getDeliPrice());
            singleOrder.setEmail(orderDTO.getEmail());
            singleOrder.setAddress1(orderDTO.getAddress1());
            singleOrder.setAddress2(orderDTO.getAddress2());
            singleOrder.setPostNum(orderDTO.getPostNum());
            singleOrder.setName(orderDTO.getName());
            singleOrder.setPhone(orderDTO.getPhone());
            singleOrder.setMemo(orderDTO.getMemo());
            singleOrder.setDeliCompany(orderDTO.getDeliCompany());
            singleOrder.setDeliCode(orderDTO.getDeliCode());
            singleOrder.setState("결제완료");

            product.setStock(product.getStock() - cart.getCount());
            int stockUpdateResult = productMapper.updateProductStock(product);
            if (stockUpdateResult <= 0) {
                throw new IllegalArgumentException("상품 " + product.getTitle() + "의 재고 업데이트에 실패했습니다.");
            }

            int orderResult = orderMapper.insertOrder(singleOrder);
            if (orderResult <= 0) {
                throw new IllegalArgumentException("상품 " + product.getTitle() + "의 주문 생성에 실패했습니다.");
            }

            orders.add(singleOrder);
            totalPrice += singleOrder.getTotalPrice();
        }

        if (orders.isEmpty()) {
            throw new IllegalArgumentException("유효한 주문 항목이 없습니다.");
        }

        // payPrice 검증
        if (orderDTO.getPayPrice() != totalPrice) {
            throw new IllegalArgumentException("결제 금액(" + orderDTO.getPayPrice() + ")이 총액(" + totalPrice + ")과 일치하지 않습니다.");
        }

        cartService.deleteCartsByMemberNo(memberNo);
        orderDTO.setTotalPrice(totalPrice);
        
        return orders;
    }
    
    //주문 취소
    @Transactional
    public int cancelOrder(int orderNo, String reason) {
        if (orderNo <= 0) {
            throw new IllegalArgumentException("주문 번호는 1 이상이어야 합니다.");
        }
        
        Map<String, Object> params = new HashMap<>();
        params.put("no", orderNo);
        params.put("state", "취소요청");
        params.put("cancelReason", reason);
        int result = orderMapper.updateOrderState(params);

        OrderDTO order = orderMapper.selectOrderByNo(orderNo);
        if (order == null) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }
        if (!"결제완료".equals(order.getState())) {
            throw new IllegalArgumentException("취소할 수 없는 주문 상태입니다: " + order.getState());
        }

        ProductDTO product = productMapper.selectProductByNo(order.getProductNo()).get(0);
        if (product != null) {
            product.setStock(product.getStock() + order.getCount());
            int stockUpdateResult = productMapper.updateProductStock(product);
            if (stockUpdateResult <= 0) {
                throw new IllegalArgumentException("재고 복구에 실패했습니다.");
            }
        }
        
//        OrderCancelDTO cancelDTO = new OrderCancelDTO();
//        cancelDTO.setOrderNo(order.getNo());
//        cancelDTO.setProductNo(order.getProductNo());
//        cancelDTO.setMemberNo(order.getMemberNo());
//        cancelDTO.setProductTitle(order.getTitle());
//        cancelDTO.setProductCount(order.getCount());
//        cancelDTO.setProductPrice(order.getProductPrice());
//        cancelDTO.setDeliPrice(order.getDeliPrice());
//        cancelDTO.setTotalPrice(order.getTotalPrice());
//        cancelDTO.setPayPrice(order.getPayPrice());
//        cancelDTO.setState("COMPLETED");
//        cancelDTO.setReason(reason != null ? reason : "고객 요청");
        
//        int cancelResult = orderCancelMapper.insertOrderCancel(cancelDTO);
//        if (cancelResult <= 0) {
//            throw new IllegalArgumentException("취소 데이터 저장에 실패했습니다.");
//        }
//
//        CartDTO cart = new CartDTO();
//        cart.setMemberNo(order.getMemberNo());
//        cart.setProductNo(order.getProductNo());
//        cart.setCount(order.getCount());
//        cartService.insertCart(cart);
//
//        int deleteResult = orderMapper.deleteOrder(orderNo);
//        if (deleteResult <= 0) {
//            throw new IllegalArgumentException("주문 삭제에 실패했습니다.");
//        }
        
//        return deleteResult;
        
        OrderCancelDTO cancelDTO = new OrderCancelDTO();
        cancelDTO.setOrderNo(orderNo);
        cancelDTO.setProductNo(order.getProductNo());
        cancelDTO.setMemberNo(order.getMemberNo());
        cancelDTO.setProductTitle(order.getTitle());
        cancelDTO.setProductCount(order.getCount());
        cancelDTO.setProductPrice(order.getProductPrice());
        cancelDTO.setDeliPrice(order.getDeliPrice());
        cancelDTO.setTotalPrice(order.getTotalPrice());
        cancelDTO.setPayPrice(order.getPayPrice());
        cancelDTO.setState("PENDING");
        cancelDTO.setReason(reason);
        orderCancelMapper.insertOrderCancel(cancelDTO);

        return result;
    }
    
    //주문 취소(order_id)
    @Transactional
    public int cancelOrdersByOrderId(String orderId, String reason) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("주문 ID는 필수입니다.");
        }

        List<OrderDTO> orders = orderMapper.selectOrdersByOrderId(orderId);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("주문 ID에 해당하는 주문이 없습니다: " + orderId);
        }

        int result = 0;
        for (OrderDTO order : orders) {
            Map<String, Object> params = new HashMap<>();
            params.put("no", order.getNo());
            params.put("state", "취소요청");
            params.put("cancelReason", reason);
            result += orderMapper.updateOrderState(params);

            ProductDTO product = productMapper.selectProductByNo(order.getProductNo()).get(0);
            product.setStock(product.getStock() + order.getCount());
            productMapper.updateProductStock(product);

            OrderCancelDTO cancelDTO = new OrderCancelDTO();
            cancelDTO.setOrderNo(order.getNo());
            cancelDTO.setProductNo(order.getProductNo());
            cancelDTO.setMemberNo(order.getMemberNo());
            cancelDTO.setProductTitle(order.getTitle());
            cancelDTO.setProductCount(order.getCount());
            cancelDTO.setProductPrice(order.getProductPrice());
            cancelDTO.setDeliPrice(order.getDeliPrice());
            cancelDTO.setTotalPrice(order.getTotalPrice());
            cancelDTO.setPayPrice(order.getPayPrice());
            cancelDTO.setState("PENDING");
            cancelDTO.setReason(reason);
            orderCancelMapper.insertOrderCancel(cancelDTO);
        }

        return result;
    }

    public OrderDTO selectOrderByNo(int no) {
        if (no <= 0) {
            throw new IllegalArgumentException("주문 번호는 1 이상이어야 합니다.");
        }
        OrderDTO order = orderMapper.selectOrderByNo(no);
        if (order == null) {
            throw new IllegalArgumentException("주문 번호 " + no + "에 해당하는 주문이 없습니다.");
        }
        return order;
    }

    public List<OrderDTO> selectOrdersByOrderId(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("주문 ID는 필수입니다.");
        }
        List<OrderDTO> order = orderMapper.selectOrdersByOrderId(orderId);
        if (order == null) {
            throw new IllegalArgumentException("주문 ID " + orderId + "에 해당하는 주문이 없습니다.");
        }
        return order;
    }

    public List<OrderDTO> selectOrdersByMemberNo(int memberNo) {
        if (memberNo <= 0) {
            throw new IllegalArgumentException("회원 번호는 1 이상이어야 합니다.");
        }
        return orderMapper.selectOrdersByMemberNo(memberNo);
    }

    public List<OrderDTO> selectAllOrders(int page, int size, String sort, String order) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("size", size);
        params.put("sort", sort);
        params.put("order", order);
        
        return orderMapper.selectAllOrders(params);
    }

    public List<OrderDTO> selectOrdersByState(String state) {
        if (state == null || state.isEmpty()) {
            throw new IllegalArgumentException("주문 상태는 필수입니다.");
        }
        // 유효한 상태인지 검증
        String[] validStates = {"결제완료", "배송중", "배송완료", "취소요청", "취소완료", "반품요청", "반품완료"};
        boolean isValidState = false;
        for (String validState : validStates) {
            if (validState.equals(state)) {
                isValidState = true;
                break;
            }
        }
        if (!isValidState) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태입니다: " + state);
        }
        return orderMapper.selectOrdersByState(state);
    }

    public List<OrderDTO> searchOrders(String searchType, String keyword, int page, int size, String sort, String order) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("keyword", keyword);
        params.put("offset", page * size);
        params.put("size", size);
        params.put("sort", sort);
        params.put("order", order);
        
        return orderMapper.searchOrders(params);
    }
    
    public int countAllOrders() {
        return orderMapper.countAllOrders();
    }

    public int countOrders(String searchType, String keyword) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("keyword", keyword);
        return orderMapper.countOrders(params);
    }

    @Transactional
    public int updateOrderState(int no, String state, String cancelReason, String returnReason) {
        if (no <= 0) {
            throw new IllegalArgumentException("주문 번호는 1 이상이어야 합니다.");
        }
        if (state == null || state.isEmpty()) {
            throw new IllegalArgumentException("주문 상태는 필수입니다.");
        }
        // 유효한 상태인지 검증
        String[] validStates = {"결제완료", "배송중", "배송완료", "취소요청", "취소완료", "반품요청", "반품완료"};
        boolean isValidState = false;
        for (String validState : validStates) {
            if (validState.equals(state)) {
                isValidState = true;
                break;
            }
        }
        if (!isValidState) {
            throw new IllegalArgumentException("유효하지 않은 주문 상태입니다: " + state);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("no", no);
        params.put("state", state);
        params.put("cancelReason", cancelReason);
        params.put("returnReason", returnReason);
        
        return orderMapper.updateOrderState(params);
    }

    @Transactional
    public int updateOrderDelivery(OrderDTO orderDTO) {
        if (orderDTO.getNo() <= 0) {
            throw new IllegalArgumentException("주문 번호는 1 이상이어야 합니다.");
        }
        if (orderDTO.getDeliCompany() == null || orderDTO.getDeliCompany().isEmpty()) {
            throw new IllegalArgumentException("택배사는 필수입니다.");
        }
        if (orderDTO.getDeliCode() == null || orderDTO.getDeliCode().isEmpty()) {
            throw new IllegalArgumentException("송장번호는 필수입니다.");
        }
        return orderMapper.updateOrderDelivery(orderDTO);
    }

    @Transactional
    public int deleteOrder(int no) {
        if (no <= 0) {
            throw new IllegalArgumentException("주문 번호는 1 이상이어야 합니다.");
        }
        return orderMapper.deleteOrder(no);
    }
}