package com.example.fantreehouse.domain.product.product.controller;

import com.example.fantreehouse.common.dto.ResponseDataDto;
import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.product.product.dto.ProductRequestDto;
import com.example.fantreehouse.domain.product.product.dto.ProductResponseDto;
import com.example.fantreehouse.domain.product.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/shop/product")
public class ProductController {

    private ProductService productService;

    /**
     * 상품 등록
     * @param productRequestDto
     * @param userDetails
     * @return
     */
    @PostMapping
    private ResponseEntity<ResponseMessageDto> createProduct(
            @Valid @RequestBody ProductRequestDto productRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.createProduct(productRequestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PRODUCT_CREATE_SUCCESS));
    }

    /**
     * 상품 조회
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    private ResponseEntity<ResponseDataDto<ProductResponseDto>> getProduct(@PathVariable Long productId) {
        ProductResponseDto responseDto = productService.getProduct(productId);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.PRODUCT_READ_SUCCESS, responseDto));
    }

    // 상품 전체 조회(Page로 3개씩)
    @GetMapping
    public ResponseEntity<ResponseDataDto<Page<ProductResponseDto>>> getProductList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Page<ProductResponseDto> responseDto = productService.getProductList(page, size);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.PRODUCT_READ_SUCCESS, responseDto));
    }

    /**
     * 상품검색조회
     * @param productName
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseDataDto<Page<ProductResponseDto>>> searchProduct(
            @RequestParam String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Page<ProductResponseDto> responseDto = productService.searchProduct(productName, page, size);
        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.PRODUCT_READ_SUCCESS, responseDto));

    }
    /**
     * 상품 수정
     * @param productId
     * @param requestDto
     * @param userDetails
     * @return
     */
    @PatchMapping("/{productId}")
    private ResponseEntity<ResponseMessageDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.updateProduct(productId, requestDto, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PRODUCT_UPDATE_SUCCESS));
    }

    /**
     * 상품 삭제
     * @param productId
     * @param userDetails
     * @return
     */
    @DeleteMapping("/{productId}")
    private ResponseEntity<ResponseMessageDto> deleteProduct(
            @PathVariable Long productId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        productService.deleteProduct(productId, userDetails.getUser());
        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PRODUCT_DELETE_SUCCESS));
    }

}
