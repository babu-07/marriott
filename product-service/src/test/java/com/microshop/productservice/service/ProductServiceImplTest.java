package com.microshop.productservice.service;

import com.microshop.productservice.controller.ProductController;
import com.microshop.productservice.dto.ProductDto;
import com.microshop.productservice.entity.Product;
import com.microshop.productservice.exception.BusinessException;
import com.microshop.productservice.external.ExternalService;
import com.microshop.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ProductServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class ProductServiceImplTest {

    @Autowired
    ProductServiceImpl productServiceImpl;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    ExternalService externalService;

    @MockBean
    ModelMapper modelMapper;

    @Test
    void testCreateProduct(){

        ProductDto savedProductDto = new ProductDto();
        savedProductDto.setName("Pen");
        savedProductDto.setPrice(10);

        Product savedProduct = new Product();
        savedProduct.setName("Pen");
        savedProduct.setPrice(10);

        when(externalService.fetchReviews()).thenReturn("Reviews");
        when(externalService.fetchDescription()).thenReturn("Description");
        when(externalService.fetchQuantity()).thenReturn(10);

        when(modelMapper.map(savedProductDto,Product.class)).thenReturn(savedProduct);
        when(modelMapper.map(savedProduct,ProductDto.class)).thenReturn(savedProductDto);

        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(savedProduct));

        Mono<ProductDto> result = productServiceImpl.createProduct(savedProductDto);

        StepVerifier.create(result)
                .expectNextMatches(productDto -> productDto.equals(savedProductDto))
                .verifyComplete();

    }

    @Test
    void updateProduct(){

        Long id = 5L;
        ProductDto savedProductDto = new ProductDto();
        savedProductDto.setName("Pen");
        savedProductDto.setPrice(10);

        Product savedProduct = new Product();
        savedProduct.setName("Pen");
        savedProduct.setPrice(10);

        when(modelMapper.map(savedProductDto,Product.class)).thenReturn(savedProduct);
        when(modelMapper.map(savedProduct,ProductDto.class)).thenReturn(savedProductDto);

        when(productRepository.findById(anyLong())).thenReturn(Mono.just(savedProduct));
        when(productRepository.save(any(Product.class))).thenReturn(Mono.just(savedProduct));

        Mono<ProductDto> result = productServiceImpl.updateProduct(savedProductDto,id);

        StepVerifier.create(result)
                .expectNextMatches(productDto -> productDto.equals(savedProductDto))
                .verifyComplete();
    }


    @Test
    void testGetProduct(){

        Long productId = 123L;
        Product expectedProduct = new Product();
        ProductDto expectedProductDto = new ProductDto();

        when(modelMapper.map(expectedProduct,ProductDto.class)).thenReturn(expectedProductDto);

        when(productRepository.findById(productId)).thenReturn(Mono.just(expectedProduct));

        Mono<ProductDto> result = productServiceImpl.getProduct(productId);

        StepVerifier.create(result)
                .expectNextMatches(productDto -> productDto.equals(expectedProductDto))
                .verifyComplete();
    }

    @Test
    void testDeleteProduct(){
        Long productId = 123L;

        when(productRepository.deleteById(productId)).thenReturn(Mono.empty());

        Mono<String> result = productServiceImpl.deleteProduct(productId);

        StepVerifier.create(result)
                .expectNextMatches(s->s.equals("Product Deleted."))
                .verifyComplete();

    }
}
