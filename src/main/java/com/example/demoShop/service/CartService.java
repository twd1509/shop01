package com.example.demoShop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoShop.dto.CartDTO;
import com.example.demoShop.mapper.CartMapper;

@Service
public class CartService {
    private final CartMapper cartMapper;

    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    //장바구니 등록
    @Transactional
    public int insertCart(CartDTO cartDTO) {
        // 유효성 검증
        if (cartDTO.getMemberNo() <= 0 || cartDTO.getProductNo() <= 0) {
            throw new IllegalArgumentException("회원번호와 상품번호는 필수입니다.");
        }
        if (cartDTO.getCount() <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        // 동일 상품이 이미 장바구니에 있는지 확인
        Map<String, Integer> params = new HashMap<>();
        params.put("memberNo", cartDTO.getMemberNo());
        params.put("productNo", cartDTO.getProductNo());
        
        CartDTO existingCart = cartMapper.selectCartByMemberAndProduct(params);

        if (existingCart != null) {
            // 기존 항목이 있으면 수량 증가
            existingCart.setCount(existingCart.getCount() + cartDTO.getCount());
            return cartMapper.updateCartCount(existingCart);
        }

        // 새로운 항목 추가
        return cartMapper.insertCart(cartDTO);
    }

    //장바구니 조회(no)
    public CartDTO selectCartByNo(int no) {
        if (no <= 0) {
            throw new IllegalArgumentException("장바구니 번호는 1 이상이어야 합니다.");
        }
        CartDTO cart = cartMapper.selectCartByNo(no);
        if (cart == null) {
            throw new IllegalArgumentException("장바구니 번호 " + no + "에 해당하는 항목이 없습니다.");
        }
        return cart;
    }

    //회원별 장바구니 항목 조회
    public List<CartDTO> selectCartsByMemberNo(int memberNo) {
        if (memberNo <= 0) {
            throw new IllegalArgumentException("회원 번호는 1 이상이어야 합니다.");
        }
        return cartMapper.selectCartsByMemberNo(memberNo);
    }

    //특정 회원의 특정 상품 장바구니 항목 조회
    public CartDTO selectCartByMemberAndProduct(int memberNo, int productNo) {
        if (memberNo <= 0 || productNo <= 0) {
            throw new IllegalArgumentException("회원번호와 상품번호는 필수입니다.");
        }
        Map<String, Integer> params = new HashMap<>();
        params.put("memberNo", memberNo);
        params.put("productNo", productNo);
        return cartMapper.selectCartByMemberAndProduct(params);
    }

    //장바구니 항목 수량 수정
    @Transactional
    public int updateCartCount(CartDTO cartDTO) {
        if (cartDTO.getNo() <= 0) {
            throw new IllegalArgumentException("장바구니 번호는 1 이상이어야 합니다.");
        }
        if (cartDTO.getCount() <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        return cartMapper.updateCartCount(cartDTO);
    }

    //장바구니 항목 삭제(no)
    @Transactional
    public int deleteCartByNo(int no) {
        if (no <= 0) {
            throw new IllegalArgumentException("장바구니 번호는 1 이상이어야 합니다.");
        }
        return cartMapper.deleteCartByNo(no);
    }

    //장바구니 항목 삭제(product_no)
    @Transactional
    public int deleteCartByProductNo(int productNo) {
        if (productNo <= 0) {
            throw new IllegalArgumentException("상품 번호는 1 이상이어야 합니다.");
        }
        return cartMapper.deleteCartByProductNo(productNo);
    }

    //회원의 모든 장바구니 항목 삭제
    @Transactional
    public int deleteCartsByMemberNo(int memberNo) {
        if (memberNo <= 0) {
            throw new IllegalArgumentException("회원 번호는 1 이상이어야 합니다.");
        }
        return cartMapper.deleteCartsByMemberNo(memberNo);
    }
}
