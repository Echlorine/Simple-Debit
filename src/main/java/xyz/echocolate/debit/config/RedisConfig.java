package xyz.echocolate.debit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Description: Redis配置
 * @ClassName: RedisConfig
 * @Author Cmite
 * @Date: 2022/7/8 10:13
 * @Version 1.0
 */

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 设置 key 的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        // 设置普通 value 的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        // 设置 hash key 的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        // 设置 hash value 的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();
        return template;

    }
}
