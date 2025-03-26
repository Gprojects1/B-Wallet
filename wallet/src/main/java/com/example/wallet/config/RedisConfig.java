package com.example.wallet.config;

import com.example.wallet.dto.QRCodeData;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, QRCodeData> qrCodeRedisTemplate(
            RedisConnectionFactory connectionFactory,
            Jackson2JsonRedisSerializer<QRCodeData> serializer
    ) {
        RedisTemplate<String, QRCodeData> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public Jackson2JsonRedisSerializer<QRCodeData> qrCodeDataSerializer() {
        return new Jackson2JsonRedisSerializer<>(QRCodeData.class);
    }
}
