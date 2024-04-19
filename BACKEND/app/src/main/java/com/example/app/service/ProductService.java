package com.example.app.service;

import com.example.app.dto.product.ProductPromotionMessagesDto;
import com.example.app.dto.product.ProductUpdateDTO;
import com.example.app.dto.product.ProductWithCategoryDTO;
import com.example.app.dto.product.ProductWithPromoDto;
import com.example.app.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page<ProductWithCategoryDTO> findAll(Pageable pageable);

    Optional<ProductWithCategoryDTO> findById(Long id);

    ProductWithCategoryDTO save(ProductWithCategoryDTO productWithCategoryDTO);

    Optional<Product> update(Long id, ProductUpdateDTO productUpdateDTO);

    void delete(Long id);

    ProductPromotionMessagesDto addPromotion(Long idProduct, Long promotionId);

    void removePromotion(Long idProduct);

    ProductWithPromoDto getProductWithPromotion(Long idProduct);

}
