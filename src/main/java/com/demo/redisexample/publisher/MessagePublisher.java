package com.demo.redisexample.publisher;

import com.demo.redisexample.model.Product;
import org.springframework.stereotype.Component;


@Component
public interface MessagePublisher {
    void publish(Product product);
}
