package com.microshop.orderservice.controller;

import com.microshop.orderservice.dto.OrderDto;
import com.microshop.orderservice.dto.ResponseDto;
import com.microshop.orderservice.entity.Order;
import com.microshop.orderservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {OrderController.class})
@ExtendWith(SpringExtension.class)
public class OrderControllerTest {

    @Autowired
    OrderController orderController;

    @MockBean
    OrderService orderService;


    @Test
    void testPlaceOrder(){
        OrderDto orderDto = Mockito.mock(OrderDto.class);
        Order order = Mockito.mock(Order.class);

        Mockito.when(orderService.placeOrder(orderDto)).thenReturn(Mono.just(order));

        Mono<ResponseDto> response = orderController.placeOrder(orderDto);

        StepVerifier.create(response)
                .expectNextMatches(responseDto -> {

                    if (responseDto.getStatusCode() == HttpStatus.OK.value() &&
                            responseDto.getMessage().equals("Order placed")) {

                        Order returnedOrder = (Order) responseDto.getOutput();

                        Assertions.assertEquals(order, returnedOrder);
                        return true;
                    }
                    return false;
                })
                .verifyComplete();
    }

    @Test
    void testGetOrderDetails(){

        Long id = 5L;
        Order order = Mockito.mock(Order.class);

        Mockito.when(orderService.getOrderDetails(id)).thenReturn(Mono.just(order));

        Mono<ResponseDto> response = orderController.getOrderDetails(id);

        StepVerifier.create(response)
                .expectNextMatches(responseDto -> {

                    if (responseDto.getStatusCode() == HttpStatus.OK.value() &&
                            responseDto.getMessage().equals("Order Fetched")) {

                        Order returnedOrder = (Order) responseDto.getOutput();

                        Assertions.assertEquals(order, returnedOrder);
                        return true;
                    }
                    return false;
                })
                .verifyComplete();

    }


    @Test
    void testGetOrderHistory(){

        Long id = 5L;
        Order order = Mockito.mock(Order.class);
        List<Order> orderList = List.of(order);

        Mockito.when(orderService.getOrderHistory(id)).thenReturn(Flux.fromIterable(orderList));

        Mono<ResponseDto> response = orderController.getOrderHistory(id);
        StepVerifier.create(response)
                .expectNextMatches(responseDto -> {

                    if (responseDto.getStatusCode() == HttpStatus.OK.value() &&
                            responseDto.getMessage().equals("Fetched Orders")) {

                        List<Order> returnedOrder = (List<Order>) responseDto.getOutput();

                        Assertions.assertEquals(orderList, returnedOrder);
                        return true;
                    }
                    return false;
                })
                .verifyComplete();
    }
}
