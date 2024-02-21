package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //관심상품 등록
    @PostMapping("/products")
    public ProductResponseDto creatProduct(@RequestBody ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }
    //관심상품 희망 최저가 등록하기
    @PostMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable(name = "id") Long id, @RequestBody ProductMypriceRequestDto productMypriceRequestDto){
        return productService.updateProduct(id, productMypriceRequestDto);
    }
}
