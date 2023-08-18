package com.microshop.productservice.service;

import com.microshop.productservice.dto.ProductDto;
import com.microshop.productservice.entity.Product;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<ProductDto> createProduct(ProductDto productDto);

    Mono<ProductDto> updateProduct(ProductDto productDto, Long id);

    Mono<ProductDto> getProduct(Long id);

    Mono<String> deleteProduct(Long id);
}
