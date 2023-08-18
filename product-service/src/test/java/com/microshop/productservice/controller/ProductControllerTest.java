package com.microshop.productservice.controller;

import com.microshop.productservice.dto.ProductDto;
import com.microshop.productservice.dto.ResponseDto;
import com.microshop.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {ProductController.class})
@ExtendWith(SpringExtension.class)
public class ProductControllerTest {


    @Autowired
    ProductController productController;

    @MockBean
    ProductService productService;

    @Test
    void createProduct(){

        ProductDto inputDto = new ProductDto();
        ProductDto savedProductDto = new ProductDto();
        savedProductDto.setName("Pen");
        when(productService.createProduct(inputDto)).thenReturn(Mono.just(savedProductDto));

        Mono<ResponseDto> result = productController.createProduct(inputDto);

        StepVerifier.create(result)
                .expectNextMatches(responseDto -> {

                    if (responseDto.getStatusCode() == HttpStatus.OK.value() &&
                            responseDto.getMessage().equals("Product saved Successfully.")) {

                        ProductDto returnedProductDto = (ProductDto) responseDto.getOutput();

                        Assertions.assertEquals(savedProductDto, returnedProductDto);
                        return true;
                    }
                    return false;
                })
                .verifyComplete();

    }

    @Test
    void updateProduct(){

        ProductDto inputDto = new ProductDto();
        Long id = 5L;
        ProductDto savedProductDto = new ProductDto();
        savedProductDto.setName("Pen");
        when(productService.updateProduct(inputDto,id)).thenReturn(Mono.just(savedProductDto));

        Mono<ResponseDto> result = productController.updateProduct(inputDto,id);

        StepVerifier.create(result)
                .expectNextMatches(responseDto -> {

                    if (responseDto.getStatusCode() == HttpStatus.OK.value() &&
                            responseDto.getMessage().equals("Product updated Successfully.")) {

                        ProductDto returnedProductDto = (ProductDto) responseDto.getOutput();

                        Assertions.assertEquals(savedProductDto, returnedProductDto);
                        return true;
                    }
                    return false;
                })
                .verifyComplete();

    }

    @Test
    void getProduct(){


        Long id = 5L;
        ProductDto savedProductDto = new ProductDto();
        savedProductDto.setName("Pen");
        when(productService.getProduct(id)).thenReturn(Mono.just(savedProductDto));

        Mono<ResponseDto> result = productController.getProduct(id);

        StepVerifier.create(result)
                .expectNextMatches(responseDto -> {

                    if (responseDto.getStatusCode() == HttpStatus.OK.value() &&
                            responseDto.getMessage().equals("Product fetched Successfully.")) {

                        ProductDto returnedProductDto = (ProductDto) responseDto.getOutput();

                        Assertions.assertEquals(savedProductDto, returnedProductDto);
                        return true;
                    }
                    return false;
                })
                .verifyComplete();


    }

    @Test
    void deleteProduct(){


        Long id = 5L;
        when(productService.deleteProduct(id)).thenReturn(Mono.just("deleted"));

        Mono<ResponseDto> result = productController.deleteProduct(id);

        StepVerifier.create(result)
                .expectNextMatches(responseDto -> {

                    if (responseDto.getStatusCode() == HttpStatus.OK.value() &&
                            responseDto.getMessage().equals("deleted")) {

                        return true;
                    }
                    return false;
                })
                .verifyComplete();


    }
}
