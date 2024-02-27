package com.demo.redisexample.service;

import com.demo.redisexample.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductServiceWithCacheImp implements ProductService {

    private final ProductService _productService;
    private final String cacheKey = "products";
    private final RedisTemplate<String, Object> _redisTemplate;
    private final HashOperations<String, String, Product> _hashOperations;

    public ProductServiceWithCacheImp(ProductService productService, RedisTemplate<String, Object> redisTemplate) {
        _productService = productService;
        _redisTemplate = redisTemplate;
        _hashOperations = _redisTemplate.opsForHash();
    }

    @Override
    public Product Create(Product product) {
        Product newProduct = _productService.Create(product);
        _redisTemplate.opsForHash().put(cacheKey, String.valueOf(newProduct.getId()), newProduct);
        _redisTemplate.expire(cacheKey, Duration.ofMinutes(10));
        return newProduct;
    }

    @Override
    public Product GetById(int id) {
        Product product;

        if (_redisTemplate.opsForHash().hasKey(cacheKey, String.valueOf(id))) {
            product = _hashOperations.get(cacheKey, String.valueOf(id));
            return product;
        }

        List<Product> productList = LoadCacheFromDb();
        product = productList.stream().filter(x -> x.id == id).findFirst().orElseThrow();

        return product;
    }

    @Override
    public List<Product> GetAll() {

        if (_redisTemplate.keys(cacheKey).isEmpty()) return LoadCacheFromDb();

        List<Product> productList = _hashOperations.values(cacheKey);
        return productList;
    }

    private List<Product> LoadCacheFromDb() {
        List<Product> productCacheList = _productService.GetAll();
        _hashOperations.putAll(cacheKey, convertProductListToMap(productCacheList));
        return productCacheList;
    }

    private Map<String, Product> convertProductListToMap(List<Product> productList) {
        Map<String, Product> productMap = new HashMap<>();
        productList.forEach(product -> productMap.put(String.valueOf(product.getId()), product));
        return productMap;
    }


    private List<Object> TransactionalUse() {
        List<Object> listObject = _redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) {
                try {
                    operations.multi();
                    operations.watch("key");
                    operations.opsForValue().set("key", "value");
                    operations.opsForValue().set("key2", "value2");

                    return operations.exec();

                } catch (DataAccessException ex) {
                    operations.discard();
                    return new ArrayList<>();
                }

            }
        });
        return listObject;
    }

    private void ObjectUse() {
        Object execute = _redisTemplate.execute(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().set("key", "value");

                return operations.exec();
            }
        });
    }


}
