package com.example.demoShop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoShop.dto.OrderDTO;
import com.example.demoShop.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //주문 생성
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            int result = orderService.insertOrder(orderDTO);
            
            if (result > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 생성에 실패했습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //주문 번호로 주문 조회
    @GetMapping("/{no}")
    public ResponseEntity<?> getOrderByNo(@PathVariable int no) {
        try {
            OrderDTO order = orderService.selectOrderByNo(no);
            
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //전체 주문 조회
    @GetMapping("/list")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.selectAllOrders();
        
        return ResponseEntity.ok(orders);
    }

    //회원별 주문 조회
    @GetMapping("/member/{memberNo}")
    public ResponseEntity<?> getOrdersByMemberNo(@PathVariable int memberNo) {
        try {
            List<OrderDTO> orders = orderService.selectOrdersByMemberNo(memberNo);
            
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //주문 검색(전체, orderId, email, name)
    @GetMapping("/search")
    public ResponseEntity<?> searchOrders(
            @RequestParam String searchType,
            @RequestParam String keyword) {
    	
        try {
            List<OrderDTO> orders = orderService.searchOrders(searchType, keyword);
            
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //주문 상태 수정
    @PostMapping("/update/state/{no}")
    public ResponseEntity<?> updateOrderState(
            @PathVariable int no,
            @RequestBody Map<String, String> request) {
    	
        try {
            String state = request.get("state");
            String cancelReason = request.get("cancelReason");
            String returnReason = request.get("returnReason");
            int result = orderService.updateOrderState(no, state, cancelReason, returnReason);
            
            if (result > 0) {
                return ResponseEntity.ok("주문 상태 수정 성공");
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //주문 배송 정보 수정
    @PostMapping("/update/delivery/{no}")
    public ResponseEntity<?> updateOrderDelivery(
            @PathVariable int no,
            @RequestBody OrderDTO orderDTO) {
    	
        try {
            orderDTO.setNo(no);
            int result = orderService.updateOrderDelivery(orderDTO);
            
            if (result > 0) {
                return ResponseEntity.ok(orderDTO);
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //주문 삭제
    @PostMapping("/delete/{no}")
    public ResponseEntity<?> deleteOrder(@PathVariable int no) {
        try {
            int result = orderService.deleteOrder(no);
            
            if (result > 0) {
                return ResponseEntity.ok("주문 삭제 성공");
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("주문을 찾을 수 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
