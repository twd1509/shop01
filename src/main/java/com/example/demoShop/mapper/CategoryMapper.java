package com.example.demoShop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demoShop.dto.CategoryDTO;

@Mapper
public interface CategoryMapper {
	//카테고리 등록
	int insertCategory(CategoryDTO cDto);
	//카테고리 수정
	int updateCategory(CategoryDTO cDto);
	//카테고리 삭제
	int deleteCategory(int no);
	//전체 카테고리
	List<CategoryDTO> selectAllCategory();
	//카테고리 검색
	List<CategoryDTO> selectCategoryByTitle(String title);		//카테고리명
	List<CategoryDTO> selectCategoryById(String id);			//아이디
	List<CategoryDTO> selectCategoryByNo(int no);				//no
	List<CategoryDTO> selectCategory(String keyword);			//전체 검색
}
