package com.example.demoShop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return pMapper.insertProduct(pDto);
	}
	
	//상품 수정
	public int updateProduct(ProductDTO pDto) {
		return pMapper.updateProduct(pDto);
	}
	
	//상품 삭제
	public int deleteProduct(int no) {
		return pMapper.deleteProduct(no);
	}
	
	//전체 상품
	public List<ProductDTO> selectAllProduct(int page, int size, String sort, String order) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", page * size);
        params.put("size", size);
        params.put("sort", sort);
        params.put("order", order);
        return pMapper.selectAllProduct(params);
    }
	
	//상품 검색
	public List<ProductDTO> selectProduct(String searchType, String keyword, int page, int size, String sort, String order) {
		List<ProductDTO> result = null;
		Map<String, Object> params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("offset", page * size);
        params.put("size", size);
        params.put("sort", sort);
        params.put("order", order);
		
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
			result = pMapper.selectProduct(params);
			break;
		}
		
		return result;
	}
	
	//전체 상품 개수
	public int countAllProducts() {
        return pMapper.countAllProducts();
    }

	//검색 시 총 상품 개수
    public int countProducts(String keyword) {
        return pMapper.countProducts(keyword);
    }
}
