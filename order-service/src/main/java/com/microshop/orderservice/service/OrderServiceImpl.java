package com.microshop.orderservice.service;

import com.microshop.orderservice.dto.OrderDto;
import com.microshop.orderservice.dto.ProductDto;
import com.microshop.orderservice.dto.ResponseDto;
import com.microshop.orderservice.entity.Order;
import com.microshop.orderservice.entity.OrderItem;
import com.microshop.orderservice.exception.BusinessException;
import com.microshop.orderservice.external.ProductService;
import com.microshop.orderservice.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;



    @Transactional
    public Mono<Order> placeOrder(OrderDto orderDto) {

        Order order = modelMapper.map(orderDto, Order.class);
        List<OrderItem> items = order.getItems();
        Double totalAmount = 0.0;
        for(OrderItem item : items){
            ResponseDto res = productService.getProduct(item.getProductId());
            ProductDto productDto = modelMapper.map(res.getOutput(), ProductDto.class);
            if(productDto.getQuantity() < item.getQuantity()){
                throw new BusinessException("Insufficient Quantity.");
            }
            else{
                totalAmount = totalAmount + item.getQuantity()*productDto.getPrice();
                productDto.setQuantity(productDto.getQuantity()- item.getQuantity());
                productService.updateProduct(productDto,productDto.getId()).then().subscribe();
            }
            item.setOrder(order);
        }
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());

        return Mono.just(orderRepository.save(order));

    }

    public Flux<Order> getOrderHistory(Long customerId) {

        return Flux.fromIterable(orderRepository.findByCustomerId(customerId));
    }

    public Mono<Order> getOrderDetails(Long orderId) {

        return Mono.just(orderRepository.findById(orderId).orElseThrow(()->new BusinessException("Order Not Found")));
    }


}
