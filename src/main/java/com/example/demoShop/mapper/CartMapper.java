package com.example.demoShop.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoShop.dto.CartDTO;

@Mapper
public interface CartMapper {
    int insertCart(CartDTO cartDTO);
    CartDTO selectCartByNo(int no);
    List<CartDTO> selectCartsByMemberNo(int memberNo);
    CartDTO selectCartByMemberAndProduct(Map<String, Integer> params);
    int updateCartCount(CartDTO cartDTO);
    int deleteCartByNo(int no);
    int deleteCartByProductNo(int productNo);
    int deleteCartsByMemberNo(int memberNo);
}
