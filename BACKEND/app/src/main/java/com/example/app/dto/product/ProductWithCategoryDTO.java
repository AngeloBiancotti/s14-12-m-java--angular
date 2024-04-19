package com.example.app.dto.product;

import com.example.app.dto.category.CategoryDTO;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;


public record ProductWithCategoryDTO(

        Long id,

        String name,

        String img,

        String description,

        BigDecimal price,

        @Getter
        CategoryDTO categoryDTO

) implements Serializable {

    }

