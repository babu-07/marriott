package com.microshop.orderservice.controller;

import com.microshop.orderservice.dto.OrderDto;
import com.microshop.orderservice.dto.ResponseDto;
import com.microshop.orderservice.entity.Order;
import com.microshop.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/orders")
@Tag(name = "Order", description = "Order Services for placing orders.")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(operationId = "placeOrder", description = "Placing the Order", summary = "Places the order for the products ordered", tags = {"Order"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Order Dto",
                    content = @Content(schema = @Schema(implementation = OrderDto.class)), required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order Placed", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    public Mono<ResponseDto> placeOrder(@Valid @RequestBody OrderDto orderDto) {

        return orderService.placeOrder(orderDto)
                .map(saveOrder -> new ResponseDto(HttpStatus.OK.value(),"Order placed",saveOrder));
    }

    @GetMapping("/{orderId}")
    @Operation(operationId = "getOrderDetails", description = "Get the details of the order.", summary = "Get the details of the order.", tags = {"Order"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order Fetched.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    public Mono<ResponseDto> getOrderDetails(@PathVariable Long orderId) {

        return orderService.getOrderDetails(orderId)
                .map(savedOrder -> new ResponseDto(HttpStatus.OK.value(), "Order Fetched", savedOrder));
    }

    @GetMapping("/history/{customerId}")
    @Operation(operationId = "getOrderHistory", description = "Get the order history.", summary = "Get the order history.", tags = {"Order"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order history Fetched.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            })
    public Mono<ResponseDto> getOrderHistory(@PathVariable Long customerId) {
        Flux<Order> order = orderService.getOrderHistory(customerId);
        return order
                .collectList()
                .map(orderList -> new ResponseDto(HttpStatus.OK.value(), orderList.isEmpty() ? "No history Found":"Fetched Orders", orderList));
    }


}
