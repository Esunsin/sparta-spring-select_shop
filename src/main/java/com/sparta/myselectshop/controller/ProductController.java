package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //관심상품 등록
    @PostMapping("/products")
    public ProductResponseDto creatProduct(@RequestBody ProductRequestDto productRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.createProduct(productRequestDto,userDetails.getUser());
    }
    //관심상품 희망 최저가 등록하기
    @PostMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable(name = "id") Long id, @RequestBody ProductMypriceRequestDto productMypriceRequestDto){
        return productService.updateProduct(id, productMypriceRequestDto);
    }


    //일반 회원
    @GetMapping("/products")
    public List<ProductResponseDto> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.getProducts(userDetails.getUser());
    }

    //관리자 권한으로 다 가져오기
    @GetMapping("/admin/products")
    public List<ProductResponseDto> getAllProducts(){
        return productService.gerAllProducts();
    }
}
