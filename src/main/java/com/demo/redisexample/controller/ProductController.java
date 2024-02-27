package com.demo.redisexample.controller;

import com.demo.redisexample.model.Product;
import com.demo.redisexample.publisher.Publisher;
import com.demo.redisexample.service.ProductService;
import com.demo.redisexample.subscriber.RedisMessageSubscriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService _productService;

    private final Publisher _publisher;

    private final RedisMessageSubscriber _redisMessageSubscriber;


    public ProductController(ProductService productService, Publisher publisher, RedisMessageSubscriber redisMessageSubscriber) {
        _productService = productService;
        _publisher = publisher;
        _redisMessageSubscriber = redisMessageSubscriber;
    }

    @PostMapping
    public ResponseEntity<Product> Create(@RequestBody Product product) throws JsonProcessingException {
        Product newProduct = _productService.Create(product);

        return ResponseEntity.created(URI.create("/api/v1/product"))
                .body(newProduct);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> GetById(int id) {
        return ResponseEntity.ok(_productService.GetById(id));
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> GetAll() throws InterruptedException {
        return ResponseEntity.ok(_productService.GetAll());
    }

    @GetMapping("/publish")
    public ResponseEntity<Void> Publish(){
        Product product=new Product();
        product.name="test";
        product.description="test";
        product.id=1;

        _publisher.publish(product);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subscriber")
    public ResponseEntity<Void> Subscriber(String channel){
        _redisMessageSubscriber.subscribe(channel);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> DeleteCache(){
        return ResponseEntity.noContent().build();
    }
}
