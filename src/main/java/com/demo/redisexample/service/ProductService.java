package com.demo.redisexample.service;
import com.demo.redisexample.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    Product Create(Product product);
    Product GetById(int id);
    List<Product> GetAll() ;

}
