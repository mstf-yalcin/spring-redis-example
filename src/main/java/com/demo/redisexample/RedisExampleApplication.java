package com.demo.redisexample;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

@SpringBootApplication
public class RedisExampleApplication implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisExampleApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        try {
//            addDataWithTransaction("key1", "value1");
//        } catch (Exception ex) {
//            System.out.println("An error occurred: " + ex.getMessage());
//        }
//
//        String result = redisTemplate.opsForValue().get("key1");
//        System.out.println("Value retrieved from Redis: " + result);
    }

    private void addDataWithTransaction(String key, String value) {

        redisTemplate.execute(new SessionCallback<Object>() {

            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().set("data","test");

                return operations.exec();
            }
        });

    }
}
