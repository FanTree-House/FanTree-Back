package com.example.fantreehouse.domain.product.product.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.security.UserDetailsImpl;
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
        // [예외1] - Admin 권한 체크
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

    /**
     * 상품 수정
     * @param productId
     * @param requestDto
     * @param user
     */
    @Transactional
    public void updateProduct(Long productId, ProductRequestDto requestDto, User user) {
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        // [예외2] - 존재하지 않는 상품
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        if (null != requestDto.getProductName()) { product.updateProductName(requestDto.getProductName()); }
        else if (null != requestDto.getStock()) { product.updateStock(requestDto.getStock()); }
        else if (null != requestDto.getType()) { product.updateType(requestDto.getType()); }
        else if (null != requestDto.getArtist()) { product.updateArtist(requestDto.getArtist()); }
        else if (null != requestDto.getProductPicture()) { product.updateProductPicture(requestDto.getProductPicture()); }
        else if (null != requestDto.getPrice()) { product.updatePrice(requestDto.getPrice()); }

        productRepository.save(product);
    }

    /**
     * 상품 삭제
     * @param productId
     * @param user
     */
    @Transactional
    public void deleteProduct(Long productId, User user) {
        // [예외1] - Entertainment 권한 체크
        checkEntertainmentAuthority(user);

        // [예외2] - 존재하지 않는 상품
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        productRepository.delete(product);
    }

    private void checkEntertainmentAuthority(User user) {
        if (!UserRoleEnum.ADMIN.equals(user.getUserRole())) {
            throw new CustomException(ErrorType.NOT_AVAILABLE_PERMISSION);
        }
    }
}
