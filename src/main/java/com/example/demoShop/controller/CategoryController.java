package com.example.demoShop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoShop.dto.CategoryDTO;
import com.example.demoShop.service.CategoryService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //카테고리 생성
    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        int result = categoryService.insertCategory(categoryDTO);
        
        if(result > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("카테고리 등록 성공");
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("카테고리 등록 실패");
    }

    //전체 카테고리 조회
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
    	return ResponseEntity.ok(categoryService.selectAllCategory());
    }

    //카테고리 검색
    @GetMapping("/search")
    public ResponseEntity<List<CategoryDTO>> searchCategories(
            @RequestParam("searchType") String searchType,
            @RequestParam("keyword") String keyword) {
    	
    	return ResponseEntity.ok(categoryService.selectCategory(searchType, keyword));
    }

    //카테고리 삭제
    @PostMapping("/delete/{no}")
    public ResponseEntity<?> deleteCategory(@PathVariable int no) {
        int result = categoryService.deleteCategory(no);
        
        if(result > 0) {
            return ResponseEntity.ok("카테고리 삭제 성공");
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("카테고리 정보를 찾을 수 없습니다.");
    }

    //카테고리 수정
    @PostMapping("/modify/{no}")
    public ResponseEntity<?> updateCategory(
    		@PathVariable int no,
            @RequestBody CategoryDTO categoryDTO) {
    	
        categoryDTO.setNo(no); // DTO에 ID 설정
        int result = categoryService.updateCategory(categoryDTO);
        
        if(result > 0) {
            return ResponseEntity.ok("카테고리 수정 성공");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("카테고리 정보를 찾을 수 없습니다.");
    }
}