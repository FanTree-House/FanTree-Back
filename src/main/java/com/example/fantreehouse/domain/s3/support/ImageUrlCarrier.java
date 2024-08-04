package com.example.fantreehouse.domain.s3.support;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImageUrlCarrier {

    Long id;
    List<String> imageUrls;
    String imageUrl;

    public ImageUrlCarrier(Long id, List<String> imageUrls) {
        this.id = id;
        this.imageUrls = imageUrls;
    }

    public ImageUrlCarrier(Long id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}

