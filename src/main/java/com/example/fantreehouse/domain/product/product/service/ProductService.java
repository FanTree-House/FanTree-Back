package com.example.fantreehouse.domain.product.product.service;

import com.example.fantreehouse.common.enums.ErrorType;
import com.example.fantreehouse.common.exception.CustomException;
import com.example.fantreehouse.common.exception.errorcode.NotFoundException;
import com.example.fantreehouse.common.exception.errorcode.S3Exception;
import com.example.fantreehouse.domain.artist.repository.ArtistRepository;
import com.example.fantreehouse.domain.feed.entity.Feed;
import com.example.fantreehouse.domain.product.product.dto.ProductRequestDto;
import com.example.fantreehouse.domain.product.product.dto.ProductResponseDto;
import com.example.fantreehouse.domain.product.product.entity.Product;
import com.example.fantreehouse.domain.product.product.repository.ProductRepository;
import com.example.fantreehouse.domain.s3.service.S3FileUploader;
import com.example.fantreehouse.domain.s3.support.ImageUrlCarrier;
import com.example.fantreehouse.domain.user.entity.User;
import com.example.fantreehouse.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.fantreehouse.common.enums.ErrorType.FEED_NOT_FOUND;
import static com.example.fantreehouse.common.enums.ErrorType.UPLOAD_ERROR;
import static com.example.fantreehouse.domain.s3.util.S3FileUploaderUtil.areFilesExist;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final S3FileUploader s3FileUploader;
    private final ArtistRepository artistRepository;


    /**
     * 상품 등록
     *
     * @param productRequestDto
     * @param user
     */
    @Transactional
    public void createProduct(List<MultipartFile> files, ProductRequestDto productRequestDto, User user) {
        // [예외1] - Admin 권한 체크
        checkEntertainmentAuthority(user);

        Product product = new Product(productRequestDto);

        productRepository.save(product);

        List<String> imageUrls = new ArrayList<>();
        if (areFilesExist(files)) { //이미지가 존재할 때만
            try {
                for (MultipartFile file : files) {
                    String imageUrl = s3FileUploader.saveProductImage(file, productRequestDto.getArtist(),
                            product.getType(), product.getId());
                    imageUrls.add(imageUrl);
                }
            } catch (Exception e) {
                throw new S3Exception(UPLOAD_ERROR);
            }
        }

        ImageUrlCarrier carrier = new ImageUrlCarrier(product.getId(), imageUrls);
        updateProductImageUrls(carrier);
    }

    /**
     * 상품 조회
     *
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
     *
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
     * 상품 검색 기능 구현
     *
     * @param productName
     * @param page
     * @param size
     * @return
     */
    public Page<ProductResponseDto> searchProduct(String productName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<ProductResponseDto> allProduct = productRepository.findAll(pageable).map(ProductResponseDto::new);
        List<ProductResponseDto> searchProduct = productRepository.findByProductNameContaining(productName, pageable).stream()
                .map(ProductResponseDto::new).toList();
        if (productName.isEmpty()) {
            return allProduct;
        }
        return new PageImpl<>(searchProduct);
    }


    /**
     * 상품 수정
     *
     * @param productId
     * @param requestDto
     * @param user
     */
    @Transactional
    public void updateProduct(List<MultipartFile> files, Long productId, ProductRequestDto requestDto, User user) {
        // [예외1] - Admin 권한 체크
        checkEntertainmentAuthority(user);

        // [예외2] - 존재하지 않는 상품
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        if (null != requestDto.getProductName()) {
            product.updateProductName(requestDto.getProductName());
        } else if (null != requestDto.getStock()) {
            product.updateStock(requestDto.getStock());
        } else if (null != requestDto.getType()) {
            product.updateType(requestDto.getType());
        } else if (null != requestDto.getArtist()) {
            product.updateArtist(requestDto.getArtist());
        } else if (null != requestDto.getPrice()) {
            product.updatePrice(requestDto.getPrice());
        }

        List<String> imageUrls = new ArrayList<>();
        if (areFilesExist(files)) {
            try {
                for (MultipartFile file : files) {
                    String imageUrl = s3FileUploader.saveProductImage(file, requestDto.getArtist(),
                            product.getType(), product.getId());
                }
            } catch (Exception e) {
                throw new S3Exception(UPLOAD_ERROR);
            }
        }

        ImageUrlCarrier carrier = new ImageUrlCarrier(product.getId(), imageUrls);
        updateProductImageUrls(carrier);

        productRepository.save(product);
    }

    /**
     * 상품 삭제
     *
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

    private void updateProductImageUrls(ImageUrlCarrier carrier) {
        if (!carrier.getImageUrls().isEmpty()) {
            Product product = productRepository.findById(carrier.getId())
                    .orElseThrow(() -> new NotFoundException(FEED_NOT_FOUND));
            product.updateImageUrls(carrier.getImageUrls());
            productRepository.save(product);
        }
    }


}
