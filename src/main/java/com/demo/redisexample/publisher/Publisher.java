package com.demo.redisexample.publisher;

import com.demo.redisexample.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;


@Component
public class Publisher implements MessagePublisher {

    private RedisTemplate<String, Product> redisTemplate;
    private ChannelTopic topic;

    public Publisher(RedisTemplate<String, Product> redisTemplate, ChannelTopic topic) {
        this.topic = topic;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void publish(Product product) {
        redisTemplate.convertAndSend(topic.getTopic(), product);
    }
}
