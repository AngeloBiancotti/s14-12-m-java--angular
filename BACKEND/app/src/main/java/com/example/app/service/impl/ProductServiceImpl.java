package com.example.app.service.impl;

import com.example.app.dto.category.CategoryDTO;
import com.example.app.dto.product.ProductPromotionMessagesDto;
import com.example.app.dto.product.ProductUpdateDTO;
import com.example.app.dto.product.ProductWithCategoryDTO;
import com.example.app.dto.product.ProductWithPromoDto;
import com.example.app.exception.category.CategoryNotFoundException;
import com.example.app.exception.product.ProductNotFoundException;
import com.example.app.exception.promotion.PromotionNotFoundException;
import com.example.app.mapper.ProductMapper;
import com.example.app.model.Category;
import com.example.app.model.Product;
import com.example.app.model.Promotion;
import com.example.app.repository.CategoryRepository;
import com.example.app.repository.ProductRepository;
import com.example.app.repository.PromotionRepository;
import com.example.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductWithCategoryDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::productToProductWithCategoryDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductWithCategoryDTO> findById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        return productOptional.map(productMapper::productToProductWithCategoryDTO);
    }

    @Override
    @Transactional
    public ProductWithCategoryDTO save(ProductWithCategoryDTO productWithCategoryDTO) {
        Product product = productMapper.toEntityFromCategoryDTO(productWithCategoryDTO);

        Long categoryId = productWithCategoryDTO.getCategoryDTO().id();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductWithCategoryDTO(savedProduct);
    }

    @Override
    @Transactional
    public Optional<Product> update(Long id, ProductUpdateDTO productUpdateDTO) {
        Optional<Product> productOptional = productRepository.findById(id);
        productOptional.ifPresent(product -> {
            product.setName(productUpdateDTO.name());
            product.setImg(productUpdateDTO.img());
            product.setDescription(productUpdateDTO.description());
            product.setPrice(productUpdateDTO.price());

            Long newCategoryId = null;
            CategoryDTO categoryDTO = productUpdateDTO.categoryDTO();
            if (categoryDTO != null) {
                newCategoryId = categoryDTO.id();
            }

            if (newCategoryId != null) {
                Category category = categoryRepository.findById(newCategoryId)
                        .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
                product.setCategory(category);
            }

            productRepository.save(product);
        });
        return productOptional;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductPromotionMessagesDto addPromotion(Long idProduct, Long promotionId) {
        Product product = productRepository.findById(idProduct).orElseThrow(
                () -> new ProductNotFoundException("Product not found with id: " + idProduct)
        );

        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(
                () -> new PromotionNotFoundException("Promotion not found with id: " + promotionId)
        );

        promotion.addProduct(product);

        return new ProductPromotionMessagesDto(
                false,
                "Promotion added to product successfully"
        );
    }

    @Override
    public void removePromotion(Long idProduct) {
        Product product = productRepository.findById(idProduct).orElseThrow(
                () -> new ProductNotFoundException("Product not found with id: " + idProduct)
        );

        if (product.getPromotion() != null) {
            product.getPromotion().removeProduct(product);
        }
    }

    @Override
    public ProductWithPromoDto getProductWithPromotion(Long idProduct) {
        Product product = productRepository.findById(idProduct).orElseThrow(
                () -> new ProductNotFoundException("Product not found with id: " + idProduct)
        );

        return productMapper.productToProductWithPromosDto(product);
    }
}
