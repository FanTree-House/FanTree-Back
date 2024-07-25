package com.example.fantreehouse.domain.product.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ProductRequestDto {
    @NotBlank(message = "상품명을 입력해주세요.")
    @Size(min = 1, max = 20, message = "상품명은 최대 20글자 입니다.")
    private String productName;

    @NotNull(message = "재고를 입력해주세요.")
    private Integer stock;

    @NotBlank(message = "상품타입을 입력해주세요.")
    @Size(min = 1, max = 20, message = "상품명은 최대 20글자 입니다.")
    private String type;

    @NotBlank(message = "관련아티스트를 입력해주세요.")
    @Size(min = 1, max = 20, message = "상품명은 최대 20글자 입니다.")
    private String artist;

    @NotBlank(message = "상품사진을 등록해주세요.")
    private String productPicture;

    @NotNull(message = "가격을 설정해주세요.")
    private  Integer price;

}
