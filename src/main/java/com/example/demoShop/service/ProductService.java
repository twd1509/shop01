package com.example.demoShop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demoShop.dto.ProductDTO;
import com.example.demoShop.mapper.ProductMapper;

@Service
public class ProductService {
	private final ProductMapper pMapper;
	
	public ProductService(ProductMapper pMapper) {
		this.pMapper = pMapper;
	}
	
	//상품 등록
	public int insertProduct(ProductDTO pDto) {
		if(selectProduct("id", pDto.getId()) != null) return -1;
		
		return pMapper.insertProduct(pDto);
	}
	
	//상품 수정
	public int updateProduct(ProductDTO pDto) {
		if(selectProduct("id", pDto.getId()) != null) return -1;
		
		return pMapper.updateProduct(pDto);
	}
	
	//상품 삭제
	public int delteProduct(int no) {
		return pMapper.deleteProduct(no);
	}
	
	//전체 상품
	public List<ProductDTO> selectAllProduct() {
		return pMapper.selectAllProduct();
	}
	
	//상품 검색
	public List<ProductDTO> selectProduct(String searchType, String keyword) {
		List<ProductDTO> result = null;
		keyword = keyword.replace(" ", "");
		
		switch (searchType) {
		case "title": 
			result = pMapper.selectProductByTitle(keyword);
			break;
		case "id":
			result = pMapper.selectProductById(keyword);
			break;
		case "no":
			result = pMapper.selectProductByNo(Integer.parseInt(keyword));
			break;
		default:
			result = pMapper.selectProduct(keyword);
			break;
		}
		
		return result;
	}
}
