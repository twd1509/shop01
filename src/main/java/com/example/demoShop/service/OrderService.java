package com.example.demoShop.service;

import com.example.demoShop.dto.OrderDTO;
import com.example.demoShop.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderMapper orderMapper;

    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Transactional
    public int insertOrder(OrderDTO orderDTO) {
        // 유효성 검증
        if (orderDTO.getMemberNo() <= 0 || orderDTO.getProductNo() <= 0) {
            throw new IllegalArgumentException("회원 번호와 상품 번호는 필수입니다.");
        }
        if (orderDTO.getTitle() == null || orderDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        }
        if (orderDTO.getCount() <= 0) {
            throw new IllegalArgumentException("주문 수량은 1 이상이어야 합니다.");
        }
        if (orderDTO.getProductPrice() <= 0 || orderDTO.getTotalPrice() <= 0) {
            throw new IllegalArgumentException("상품 가격과 전체 가격은 0보다 커야 합니다.");
        }
        if (orderDTO.getOrderId() == null || orderDTO.getOrderId().isEmpty()) {
            // 주문 ID 자동 생성 (예: ORD202508060001)
            orderDTO.setOrderId("ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8));
        }
        if (orderDTO.getState() == null || orderDTO.getState().isEmpty()) {
            orderDTO.setState("결제완료"); // 기본 상태
        }

        // 전체 가격 검증
        int expectedTotalPrice = (orderDTO.getProductPrice() * orderDTO.getCount()) + orderDTO.getDeliPrice();
        if (orderDTO.getTotalPrice() != expectedTotalPrice) {
            throw new IllegalArgumentException("전체 가격이 올바르지 않습니다. 기대값: " + expectedTotalPrice);
        }

        // 주문 삽입
        return orderMapper.insertOrder(orderDTO);
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

    public OrderDTO selectOrderByOrderId(String orderId) {
        if (orderId == null || orderId.isEmpty()) {
            throw new IllegalArgumentException("주문 ID는 필수입니다.");
        }
        OrderDTO order = orderMapper.selectOrderByOrderId(orderId);
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

    public List<OrderDTO> selectAllOrders() {
        return orderMapper.selectAllOrders();
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

    public List<OrderDTO> searchOrders(String searchType, String keyword) {
        if (searchType == null || keyword == null || searchType.isEmpty() || keyword.isEmpty()) {
            throw new IllegalArgumentException("검색 유형과 키워드는 필수입니다.");
        }
        // 유효한 검색 유형인지 검증
        String[] validSearchTypes = {"orderId", "email", "name"};
        boolean isValidSearchType = false;
        for (String validType : validSearchTypes) {
            if (validType.equals(searchType)) {
                isValidSearchType = true;
                break;
            }
        }
        if (!isValidSearchType) {
            throw new IllegalArgumentException("유효하지 않은 검색 유형입니다: " + searchType);
        }

        Map<String, String> params = new HashMap<>();
        params.put("searchType", searchType);
        params.put("keyword", keyword);
        return orderMapper.searchOrders(params);
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