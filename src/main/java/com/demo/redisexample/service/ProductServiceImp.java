package com.demo.redisexample.service;

import com.demo.redisexample.model.Product;
import com.demo.redisexample.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository _productRepository;


    public ProductServiceImp(ProductRepository productRepository) {
        _productRepository = productRepository;
    }


    @Override
//    @Cacheable(cacheNames = "productCache", key = "#id")
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

//    @CacheEvict(value = "usersCache", key = "#userId",
//            condition = "#result == true", allEntries = false, beforeInvocation = false)
    public boolean deleteUserFromCache(String userId) {
        return true;
    }

//    @Caching(cacheable = {
//            @Cacheable("test")
//    },evict = {
//            @CacheEvict(value = "usersCache", key = "#userId"),
//            @CacheEvict(value = "statisticsCache", allEntries = true)
//    })
    public void updateUserAndClearStatistics(String userId, Product updateProduct) {
    }

//    @CachePut(value = "usersCache", key = "#userId", condition = "#result != null")
    public void updateUserInCache(String userId, Product updateProduc) {

        return  ;
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
