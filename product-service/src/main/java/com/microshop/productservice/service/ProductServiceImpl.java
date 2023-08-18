package com.microshop.productservice.service;

import com.microshop.productservice.dto.ProductDto;
import com.microshop.productservice.entity.Product;
import com.microshop.productservice.exception.BusinessException;
import com.microshop.productservice.external.ExternalService;
import com.microshop.productservice.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.beans.FeatureDescriptor;
import java.util.Arrays;

@Service
public class ProductServiceImpl implements ProductService{

    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ExternalService externalService;

    @Autowired
    private ModelMapper modelMapper;

    public Mono<ProductDto> createProduct(ProductDto productDto) {
        logger.info("Entered updateProduct of ProductServiceImpl.");

        productDto.setReview(externalService.fetchReviews());
        productDto.setDescription(externalService.fetchDescription());
        productDto.setQuantity(externalService.fetchQuantity());
        return productRepository.save(modelMapper.map(productDto, Product.class)).map(savedProduct-> modelMapper.map(savedProduct, ProductDto.class));
    }

    public Mono<ProductDto> updateProduct(ProductDto productDto, Long id){

        logger.info("Entered updateProduct of ProductServiceImpl.");

        Product product = modelMapper.map(productDto, Product.class);

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException("Product not found")))
                .flatMap(existingProduct -> {
                    final BeanWrapper wrappedSource = new BeanWrapperImpl(product);
                    String[] nullPropertyNames = Arrays.stream(wrappedSource.getPropertyDescriptors())
                            .map(FeatureDescriptor::getName)
                            .filter(name -> wrappedSource.getPropertyValue(name) == null)
                            .toArray(String[]::new);
                    BeanUtils.copyProperties(product, existingProduct, nullPropertyNames);
                    return productRepository.save(existingProduct).map(savedProduct-> modelMapper.map(savedProduct, ProductDto.class));
                });
    }

    public Mono<ProductDto> getProduct(Long id){
        logger.info("Entered getProduct of ProductServiceImpl.");

        return productRepository.findById(id).switchIfEmpty(Mono.error(new BusinessException("Product not found"))).map(savedProduct-> modelMapper.map(savedProduct, ProductDto.class));
    }

    public Mono<String> deleteProduct(Long id){

        logger.info("Entered deleteProduct in ProductServiceImpl.");

        return productRepository.deleteById(id)
                .then(Mono.just("Product Deleted."));
    }

}
