package com.example.app.dto.category;

import lombok.Getter;

import java.io.Serializable;

public record CategoryDTO(

        @Getter
        Long id,

        String name

) implements Serializable {
}
