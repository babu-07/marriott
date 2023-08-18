package com.microshop.productservice.external;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalService {
    private WebClient webClient;

    public ExternalService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9001/example").build();
    }

    public String fetchReviews() {
        return webClient.get().uri("/review").retrieve().bodyToMono(String.class).block();
    }

    public Integer fetchQuantity() {
        return webClient.get().uri("/quantity").retrieve().bodyToMono(Integer.class).block();
    }

    public String fetchDescription() {
        return webClient.get().uri("/description").retrieve().bodyToMono(String.class).block();
    }
}
