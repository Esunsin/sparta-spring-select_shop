package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ItemDto;
import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    public static final int MIN_MY_PRICE = 100;

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto productRequestDto, User user){
        Product saved = productRepository.save(new Product(productRequestDto,user));
        return new ProductResponseDto(saved);
    }


    public ProductResponseDto updateProduct(Long productId, ProductMypriceRequestDto productMypriceRequestDto) {
        int myprice = productMypriceRequestDto.getMyprice();
        if(myprice < MIN_MY_PRICE){
            throw new IllegalArgumentException("유효하지 않은 관심가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요");
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("해당상품을 찾을 수 없습니다"));
        product.update(productMypriceRequestDto);
        return new ProductResponseDto(product);
    }

    public void updateBySearch(Long productId, ItemDto itemDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("해당상품이 없습니다."));
        product.updateByItemDto(itemDto);
    }

    public List<ProductResponseDto> getProducts(User user) {
        List<Product> products = productRepository.findAllByUser(user);
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;
    }

    public List<ProductResponseDto> gerAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;
    }
}
