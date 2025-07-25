package com.example.demoShop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demoShop.dto.CategoryDTO;
import com.example.demoShop.mapper.CategoryMapper;

@Service
public class CategoryService {
	private final CategoryMapper cMapper;
	
	public CategoryService(CategoryMapper cMapper) {
		this.cMapper = cMapper;
	}
	
	//카테고리 생성
	public int insertCategory(CategoryDTO cDto) {
		return cMapper.insertCategory(cDto);
	}
	
	//전체 카테고리
	public List<CategoryDTO> selectAllCategory() {
		return cMapper.selectAllCategory();
	}
	
	//카테고리 검색
	public List<CategoryDTO> selectCategory(String searchType, String keyword) {
		List<CategoryDTO> result = null;
		keyword = keyword.replace(" ", "");
		
		switch (searchType) {
		case "title": 
			result = cMapper.selectCategoryByTitle(keyword);
			break;
		case "id":
			result = cMapper.selectCategoryById(keyword);
			break;
		case "no":
			result = cMapper.selectCategoryByNo(Integer.parseInt(keyword));
			break;
		default:
			result = cMapper.selectCategory(keyword);
			break;
		}
		
		return result;
	}
	
	//카테고리 삭제
	public int deleteCategory(int no) {
		return cMapper.deleteCategory(no);
	}
	
	//카테고리 수정
	public int updateCategory(CategoryDTO cDto) {
		return cMapper.updateCategory(cDto);
	}
}
