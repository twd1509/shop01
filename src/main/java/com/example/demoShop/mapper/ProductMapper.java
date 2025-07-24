package com.example.demoShop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoShop.dto.ProductDTO;

@Mapper
public interface ProductMapper {
	//상품 등록
	int insertProduct(ProductDTO pDto);
	//상품 수정
	int updateProduct(ProductDTO pDto);
	//상품 삭제
	int deleteProduct(int no);
	//전체 상품 검색
	List<ProductDTO> selectAllProduct();
	//상품 검색
	List<ProductDTO> selectProductByTitle(String title);	//상품명
	List<ProductDTO> selectProductById(String id);			//상품아이디
}
