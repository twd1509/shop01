package com.example.demoShop.controller;

import com.example.demoShop.dto.CartDTO;
import com.example.demoShop.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 장바구니 항목 추가
    @PostMapping("/add")
    public ResponseEntity<?> addCart(@RequestBody CartDTO cartDTO) {
        try {
            int result = cartService.insertCart(cartDTO);
            
            if (result > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).body("장바구니 항목이 추가 되었습니다.");
            }
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("장바구니 항목 추가에 실패했습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 장바구니 항목 번호로 조회
    @GetMapping("/list/{no}")
    public ResponseEntity<?> getCartByNo(@PathVariable int no) {
        try {
            CartDTO cart = cartService.selectCartByNo(no);
            
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 회원별 장바구니 목록 조회
    @GetMapping("/member/{memberNo}")
    public ResponseEntity<?> getCartsByMemberNo(@PathVariable int memberNo) {
        try {
            List<CartDTO> carts = cartService.selectCartsByMemberNo(memberNo);
            
            return ResponseEntity.ok(carts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 특정 회원의 특정 상품 장바구니 항목 조회
    @GetMapping("/member/{memberNo}/product/{productNo}")
    public ResponseEntity<?> getCartByMemberAndProduct(
            @PathVariable int memberNo, @PathVariable int productNo) {
    	
        try {
            CartDTO cart = cartService.selectCartByMemberAndProduct(memberNo, productNo);
            
            if (cart == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 장바구니 항목이 없습니다.");
            }
            
            return ResponseEntity.ok(cart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 장바구니 항목 수량 수정
    @PostMapping("/update/count/{no}")
    public ResponseEntity<?> updateCartCount(
            @PathVariable int no, @RequestBody CartDTO cartDTO) {
    	
        try {
            cartDTO.setNo(no);
            int result = cartService.updateCartCount(cartDTO);
            
            if (result > 0) {
                return ResponseEntity.ok("장바구니 수량 수정 성공");
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("장바구니 항목을 찾을 수 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 장바구니 항목 삭제 (no)
    @PostMapping("/delete/{no}")
    public ResponseEntity<?> deleteCartByNo(@PathVariable int no) {
        try {
            int result = cartService.deleteCartByNo(no);
            
            if (result > 0) {
                return ResponseEntity.ok("장바구니 항목 삭제 성공");
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("장바구니 항목을 찾을 수 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 장바구니 항목 삭제 (productNo)
    @PostMapping("/delete/product/{productNo}")
    public ResponseEntity<?> deleteCartByProductNo(@PathVariable int productNo) {
        try {
            int result = cartService.deleteCartByProductNo(productNo);
            
            if (result > 0) {
                return ResponseEntity.ok("장바구니 항목 삭제 성공");
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품의 장바구니 항목을 찾을 수 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 회원의 모든 장바구니 항목 삭제
    @PostMapping("/delete/member/{memberNo}")
    public ResponseEntity<?> deleteCartsByMemberNo(@PathVariable int memberNo) {
        try {
            int result = cartService.deleteCartsByMemberNo(memberNo);
            
            if (result > 0) {
                return ResponseEntity.ok("회원 장바구니 전체 삭제 성공");
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 장바구니 항목이 없습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}