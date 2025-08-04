package com.example.demoShop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoShop.dto.CategoryDTO;
import com.example.demoShop.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // 카테고리 생성
    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        int result = categoryService.insertCategory(categoryDTO);
        
        if (result > 0) {
            return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
        }
        
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // 전체 카테고리 조회
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.selectAllCategory();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // 카테고리 검색
    @PostMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategories(
            @RequestParam("searchType") String searchType,
            @RequestParam("keyword") String keyword) {
    	
        List<CategoryDTO> categories = categoryService.selectCategory(searchType, keyword);
        
        if (categories != null && !categories.isEmpty()) {
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 카테고리 삭제
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteCategory(@RequestParam("no") int no) {
        int result = categoryService.deleteCategory(no);
        
        if (result > 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 카테고리 수정
    @PostMapping("/update")
    public ResponseEntity<CategoryDTO> updateCategory(
            @RequestParam("no") int no,
            @RequestBody CategoryDTO categoryDTO) {
    	
        categoryDTO.setNo(no); // DTO에 ID 설정
        int result = categoryService.updateCategory(categoryDTO);
        
        if (result > 0) {
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}