package com.microshop.orderservice.service;

import com.microshop.orderservice.controller.OrderController;
import com.microshop.orderservice.dto.OrderDto;
import com.microshop.orderservice.dto.OrderItemDto;
import com.microshop.orderservice.dto.ProductDto;
import com.microshop.orderservice.dto.ResponseDto;
import com.microshop.orderservice.entity.Order;
import com.microshop.orderservice.entity.OrderItem;
import com.microshop.orderservice.exception.BusinessException;
import com.microshop.orderservice.external.ProductService;
import com.microshop.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {OrderServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

    @Autowired
    OrderServiceImpl orderServiceImpl;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    ProductService productService;

    @MockBean
    ModelMapper modelMapper;

    @Test
    void testPlaceOrder(){

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setQuantity(10);
        productDto.setPrice(50.0);

        ResponseDto res = new ResponseDto(200,"",productDto);
        when(productService.getProduct(anyLong())).thenReturn(res);

        Order resultOrder = new Order();
        when(orderRepository.save(any(Order.class))).thenReturn(resultOrder);


        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(2L);
        OrderItemDto orderItem = new OrderItemDto();
        orderItem.setProductId(2L);
        orderItem.setQuantity(10);
        List<OrderItemDto> orderList = List.of(orderItem);
        orderDto.setItems(orderList);

        Order order = new Order();
        order.setCustomerId(2L);
        OrderItem orderItems = new OrderItem();
        orderItems.setProductId(2L);
        orderItems.setQuantity(10);
        List<OrderItem> ordersList = List.of(orderItems);
        order.setItems(ordersList);

        when(modelMapper.map(orderDto,Order.class)).thenReturn(order);
        when(modelMapper.map(res.getOutput(),ProductDto.class)).thenReturn(productDto);


        when(productService.updateProduct(any(ProductDto.class),anyLong())).thenReturn(Mono.just(res));

        Mono<Order> result = orderServiceImpl.placeOrder(orderDto);

        assertNotNull(result);

        StepVerifier.create(result)
                .expectNextMatches(o -> o.equals(resultOrder))
                .verifyComplete();

    }


    @Test
    void testPlaceOrderWithException(){

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setQuantity(10);
        productDto.setPrice(50.0);

        ResponseDto res = new ResponseDto(200,"",productDto);
        when(productService.getProduct(anyLong())).thenReturn(res);

        when(orderRepository.save(any(Order.class))).thenReturn(new Order());


        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(2L);
        OrderItemDto orderItem = new OrderItemDto();
        orderItem.setProductId(2L);
        orderItem.setQuantity(100);
        List<OrderItemDto> orderList = List.of(orderItem);
        orderDto.setItems(orderList);

        Order order = new Order();
        order.setCustomerId(2L);
        OrderItem orderItems = new OrderItem();
        orderItems.setProductId(2L);
        orderItems.setQuantity(100);
        List<OrderItem> ordersList = List.of(orderItems);
        order.setItems(ordersList);

        when(modelMapper.map(orderDto,Order.class)).thenReturn(order);
        when(modelMapper.map(res.getOutput(),ProductDto.class)).thenReturn(productDto);


        when(productService.updateProduct(any(ProductDto.class),anyLong())).thenReturn(Mono.just(res));

        assertThrows(BusinessException.class, () -> orderServiceImpl.placeOrder(orderDto));


    }


    @Test
    public void testGetOrderHistory() {
        Long customerId = 123L;
        Order order = new Order();
        order.setCustomerId(123L);
        OrderItem orderItems = new OrderItem();
        orderItems.setProductId(2L);
        orderItems.setQuantity(10);
        List<OrderItem> ordersList = List.of(orderItems);
        order.setItems(ordersList);
        List<Order> expectedOrders = List.of(order);
        when(orderRepository.findByCustomerId(customerId)).thenReturn(expectedOrders);

        Flux<Order> resultOrders = orderServiceImpl.getOrderHistory(customerId);

        StepVerifier.create(resultOrders)
                .expectNextSequence(expectedOrders)
                .verifyComplete();
    }


    @Test
    void testGetOrderDetails() {

        Long orderId = 123L;
        Order order = new Order();
        order.setCustomerId(123L);
        OrderItem orderItems = new OrderItem();
        orderItems.setProductId(2L);
        orderItems.setQuantity(10);
        List<OrderItem> ordersList = List.of(orderItems);
        order.setItems(ordersList);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Mono<Order> resultOrder = orderServiceImpl.getOrderDetails(orderId);

        StepVerifier.create(resultOrder)
                .expectNextMatches(o -> o.equals(order))
                .verifyComplete();

    }

    @Test
    public void testGetOrderDetailsOrderNotFound() {

        Long orderId = 123L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> orderServiceImpl.getOrderDetails(orderId));
    }
}

