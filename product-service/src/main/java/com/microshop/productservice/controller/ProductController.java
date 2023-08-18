package com.microshop.productservice.controller;

import com.microshop.productservice.dto.ProductDto;
import com.microshop.productservice.dto.ResponseDto;
import com.microshop.productservice.entity.Product;
import com.microshop.productservice.service.ProductService;
import com.microshop.productservice.service.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "Product Services for managing product.")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    @Operation(operationId = "createProduct", description = "Creating the Product.", summary = "Creating the Product.", tags = {"Product"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product Dto",
                    content = @Content(schema = @Schema(implementation = ProductDto.class)), required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product Created.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    public Mono<ResponseDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.createProduct(productDto)
                .map(savedProduct -> new ResponseDto(HttpStatus.OK.value(), "Product saved Successfully.", savedProduct));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateProduct", description = "Updating the Product.", summary = "Updating the Product.", tags = {"Product"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product Dto",
                    content = @Content(schema = @Schema(implementation = ProductDto.class)), required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product Updated.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    public Mono<ResponseDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable("id") Long id) {
        return productService.updateProduct(productDto, id)
                .map(savedProduct -> new ResponseDto(HttpStatus.OK.value(), "Product updated Successfully.", savedProduct));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getProduct", description = "Get the details of the product.", summary = "Get the details of the product.", tags = {"Product"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product Fetched.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    public Mono<ResponseDto> getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id)
                .map(savedProduct -> new ResponseDto(HttpStatus.OK.value(), "Product fetched Successfully.", savedProduct));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteProduct", description = "Deletes the product.", summary = "Deletes the product.", tags = {"Product"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product Deleted.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    public Mono<ResponseDto> deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteProduct(id)
                .map(message -> new ResponseDto(HttpStatus.OK.value(), message,null));

    }

}
