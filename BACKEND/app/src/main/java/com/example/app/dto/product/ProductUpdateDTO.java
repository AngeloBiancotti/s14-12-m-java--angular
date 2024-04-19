package com.example.app.dto.product;

import com.example.app.dto.category.CategoryDTO;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductUpdateDTO(

        String name,

        String img,

        String description,

        BigDecimal price,

        CategoryDTO categoryDTO

) implements Serializable {
}
