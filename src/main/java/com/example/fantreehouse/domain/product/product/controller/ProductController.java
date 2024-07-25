package com.example.fantreehouse.domain.product.product.controller;

import com.example.fantreehouse.common.dto.ResponseMessageDto;
import com.example.fantreehouse.common.enums.ResponseStatus;
import com.example.fantreehouse.common.security.UserDetailsImpl;
import com.example.fantreehouse.domain.product.product.dto.ProductRequestDto;
import com.example.fantreehouse.domain.product.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

/*    // 상품 조회
    @GetMapping("/{productId}")
    private ResponseEntity<ResponseDataDto<ProductResponseDto>> getProduct(@RequestParam Long productId) {

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.PRODUCT_READ_SUCCESS, responseDto)
    }

    // 상품 전체 조회(Page로 3개씩)
    @GetMapping
    public ResponseEntity<ResponseDataDto<Page<ProductResponseDto>>> getDeckList() {

        return ResponseEntity.ok(new ResponseDataDto<>(ResponseStatus.PRODUCT_READ_SUCCESS, responseDto)
    }

    // 상품 수정
    @PatchMapping("/{productId}")
    private ResponseEntity<ResponseMessageDto> updateProduct() {

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PRODUCT_UPDATE_SUCCESS));
    }

    // 상품 삭제
    @DeleteMapping("/{productId}")
    private ResponseEntity<ResponseMessageDto> deleteProduct() {

        return ResponseEntity.ok(new ResponseMessageDto(ResponseStatus.PRODUCT_DELETE_SUCCESS));
    }  */

}
