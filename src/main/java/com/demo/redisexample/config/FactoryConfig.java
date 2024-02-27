package com.demo.redisexample.config;

import com.demo.redisexample.service.ProductService;
import com.demo.redisexample.service.ProductServiceImp;
import com.demo.redisexample.service.ProductServiceWithCacheImp;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class FactoryConfig {

    @Bean
    @Qualifier
    public ProductService productService(ProductServiceImp productServiceImp, RedisTemplate<String,Object> redisTemplate)
    {
        return new ProductServiceWithCacheImp(productServiceImp,redisTemplate);
    }
}
