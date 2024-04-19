package com.example.app.repository;

import com.example.app.model.Category;
import com.example.app.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository <Product, Long> {

    Page<Product> findAll(Pageable pageable);

}
