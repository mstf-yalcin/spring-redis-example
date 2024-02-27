package com.demo.redisexample.subscriber;

import com.demo.redisexample.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;


@Service
public class RedisMessageSubscriber implements MessageListener {
    Logger logger = LoggerFactory.getLogger(RedisMessageSubscriber.class);

    private RedisTemplate<String, Product> redisTemplate;

    public RedisMessageSubscriber(RedisTemplate<String, Product> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(message.toString(), Product.class);

            logger.info("Message description : " + product.description);
            logger.info("Message name : " + product.name);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        //2. redisTemplate deserialize
        Product product = (Product) redisTemplate.getValueSerializer().deserialize(message.getBody());
        logger.info("Message channel : " + new String(message.getChannel(), StandardCharsets.UTF_8));
        logger.info("Message received : " + product.name);


    }

    public void subscribe(String channel) {
        redisTemplate.getConnectionFactory().getConnection().subscribe(this, channel.getBytes());
    }
}
