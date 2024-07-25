package com.example.fantreehouse.domain.product.product.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.domain.product.product.dto.ProductRequestDto;
import com.example.fantreehouse.domain.product.product.dto.ProductResponseDto;
import com.example.fantreehouse.domain.product.product.entity.Product;
import com.example.fantreehouse.domain.product.product.repository.ProductRepository;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 상품 등록
     * @param productRequestDto
     * @param user
     */
    @Transactional
    public void createProduct(ProductRequestDto productRequestDto, User user) {
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        Product product = new Product(productRequestDto);

        productRepository.save(product);
    }

    /**
     * 상품 조회
     * @param productId
     * @return
     */
    public ProductResponseDto getProduct(Long productId) {
        // [예외 1] - 존재하지 않는 상품
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ErrorType.PRODUCT_NOT_FOUND)
        );

        return new ProductResponseDto(product);
    }

    /**
     * 상품 전체 조회
     * @param page
     * @param size
     * @return
     */
    public Page<ProductResponseDto> getProductList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ProductResponseDto> productPage = productRepository.findAll(pageable).map(ProductResponseDto::new);
        return productPage;
    }

    private void checkEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_AVAILABLE_PERMISSION);
        }
    }
}
