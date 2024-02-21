package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ItemDto;
import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.*;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    public static final int MIN_MY_PRICE = 100;

    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;

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

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> products;

        if(userRoleEnum == UserRoleEnum.USER){
            products = productRepository.findAllByUser(user, pageable);
        }else {
            products = productRepository.findAll(pageable);
        }


        return products.map(ProductResponseDto::new);
    }

    public List<ProductResponseDto> gerAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product : products) {
            productResponseDtos.add(new ProductResponseDto(product));
        }
        return productResponseDtos;
    }

    public void addFolder(Long productId, Long folderId, User user) {
     //상품조회
        Product product = productRepository.findById(productId).orElseThrow(() -> new NullPointerException("상품이 존재 하지 않습니다."));
        //폴더 조회
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new NullPointerException("폴더가 존재하지 않습니다."));
        //조회한 폴더 및 상품이 유저가 가지고 있는지 확인
        if (!product.getUser().getId().equals(user.getId()) || !folder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }
        //중복 확인
        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product, folder);
        if(overlapFolder.isPresent()){
            throw new IllegalArgumentException("중복된 폴더 입니다.");
        }
        //상품을 폴더에 추가
        productFolderRepository.save(new ProductFolder(product, folder));
    }
}
