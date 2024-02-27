package com.demo.redisexample.config;


import com.demo.redisexample.model.Product;
import com.demo.redisexample.publisher.MessagePublisher;
import com.demo.redisexample.publisher.Publisher;
import com.demo.redisexample.subscriber.RedisMessageSubscriber;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.*;


@Configuration
@EnableCaching
//@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${channel.topic}")
    private String channelTopic;


    //lettuce connection asenkron
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        config.setPassword("");
        config.setUsername("");
        config.setDatabase(0);

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(8);

        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.
                builder()
                .poolConfig(poolConfig)
                .build();


        return new LettuceConnectionFactory(config, lettucePoolingClientConfiguration);
    }


//    jedis - senkron
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory() {
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setUsername("");
//        redisStandaloneConfiguration.setPassword("");
//        redisStandaloneConfiguration.setDatabase(0);
//        redisStandaloneConfiguration.setHostName(host);
//        redisStandaloneConfiguration.setPort(port);
//
//        GenericObjectPoolConfig<Jedis> genericObjectPool = new GenericObjectPoolConfig<>();
//        genericObjectPool.setMaxTotal(10);//max 10 connection pool olusturacak
//        genericObjectPool.setMaxIdle(5);// bosta max. 5 instance
//        genericObjectPool.setMinIdle(2);//bosta min 2 instance bıracak
//
//        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
//
//        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(1));//baglantı suresi
//        jedisClientConfiguration.usePooling().poolConfig(genericObjectPool);
//
//
//        return new JedisConnectionFactory(redisStandaloneConfiguration, JedisClientConfiguration.builder().build());
//    }

//    jedis pool connection
//    @Bean
//    public Jedis jedisConnectionFactorys() {
//
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        JedisPool jedisPool = new JedisPool(host, port);
//        Jedis jedis = jedisPool.getResource();
//        return jedis;
//    }



    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }






    //publisher

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(channelTopic);
    }
    @Bean
    public MessagePublisher messagePublisher()
    {
        return new Publisher(redisTemplateForProduct(),topic());
    }

    @Bean
    public RedisTemplate<String, Product> redisTemplateForProduct() {

        RedisTemplate<String, Product> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Product>(Product.class));

        return template;

    }


    //subs

//    @Bean
//    MessageListenerAdapter messageListener() {
//        return new MessageListenerAdapter( new RedisMessageSubscriber(redisTemplateForProduct()) );
//    }


    @Bean
    public RedisMessageListenerContainer redisContainer(){
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(lettuceConnectionFactory());
//        redisMessageListenerContainer.addMessageListener(messageListenerAdapter(),topic());
        return redisMessageListenerContainer;
    }




//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//
//        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.afterPropertiesSet();
//
//        return redisTemplate;
//    }




//    @Bean
//    public RedisTemplate<String, Object> template() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(lettuceConnectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
//        template.setEnableTransactionSupport(true);
//        template.afterPropertiesSet();
//        return template;
//    }


}
