package com.microshop.orderservice.external;

import com.microshop.orderservice.dto.ProductDto;
import com.microshop.orderservice.dto.ResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    private WebClient webClient;

    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9000/products").build();
    }

    public ResponseDto getProduct(Long id) {
        return webClient.get().uri("/"+id).retrieve().bodyToMono(ResponseDto.class).block();
    }

    public Mono<ResponseDto> updateProduct(ProductDto productDto , Long id) {
        return webClient.put().uri("/"+id).bodyValue(productDto).retrieve().bodyToMono(ResponseDto.class);
    }

}