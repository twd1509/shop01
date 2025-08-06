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

import com.example.demoShop.dto.ProductDTO;
import com.example.demoShop.service.ProductService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //상품 등록
    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductDTO productDTO) {
        int result = productService.insertProduct(productDTO);
        
        if(result == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("상품 아이디가 이미 존재합니다.");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 등록 되었습니다.");
    }

    //상품 수정
    @PostMapping("/modify/{no}")
    public ResponseEntity<String> updateProduct(@PathVariable int no, @RequestBody ProductDTO productDTO) {
        productDTO.setNo(no);
        int result = productService.updateProduct(productDTO);
        
        if(result == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품을 찾을 수 없습니다.");
        }
        
        return ResponseEntity.ok("상품이 수정 되었습니다.");
    }

    //상품 삭제
    @PostMapping("/delete/{no}")
    public ResponseEntity<String> deleteProduct(@PathVariable int no) {
        int result = productService.deleteProduct(no);
        
        if(result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("상품을 찾을 수 없습니다.");
        }
        
        return ResponseEntity.ok("상품이 삭제 되었습니다.");
    }

    //전체 상품 출력
    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.selectAllProduct();
        
        return ResponseEntity.ok(products);
    }

    //상품 검색
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(
            @RequestParam String searchType,
            @RequestParam String keyword) {
        List<ProductDTO> products = productService.selectProduct(searchType, keyword);
        
        if(products == null || products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(products);
    }
}
