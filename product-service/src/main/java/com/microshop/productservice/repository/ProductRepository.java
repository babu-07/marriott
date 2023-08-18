package com.microshop.productservice.repository;

import com.microshop.productservice.entity.Product;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
}
