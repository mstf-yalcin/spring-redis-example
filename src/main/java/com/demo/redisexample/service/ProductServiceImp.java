package com.demo.redisexample.service;

import com.demo.redisexample.model.Product;
import com.demo.redisexample.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository _productRepository;


    public ProductServiceImp(ProductRepository productRepository) {
        _productRepository = productRepository;
    }


    @Override
//    @CustomProductCache
    public Product GetById(int id) {

        Product product = _productRepository.findById(id).orElseThrow();
        waitSec();
        return product;
    }


//    @CachePut(cacheNames = "productCache", key = "#product.id")
    public Product Create(Product product) {
        _productRepository.save(product);
        return product;
    }

    @Override
//    @Cacheable(cacheNames = "productCache", key = "#root.methodName")
    public List<Product> GetAll() {

        List<Product> all = _productRepository.findAll();

        waitSec();
        return all;
    }

//    @CacheEvict(cacheNames = "productCache", allEntries = true)
    public void Delete() {

    }

    public void waitSec() {
        try {
            Thread.sleep(2000);
        } catch (Exception ex) {

        }
    }


}
