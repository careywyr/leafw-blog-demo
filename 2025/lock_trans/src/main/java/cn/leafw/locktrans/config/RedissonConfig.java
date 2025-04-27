package cn.leafw.locktrans.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

/**
 * @Desc Redisson配置类（极简单节点模式）
 * @Author leafw
 * @Date 2025/4/27
 **/
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redisson() {
        Config config = new Config();
        config.setCodec(new StringCodec(StandardCharsets.UTF_8));
        config.useSingleServer()
              .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
              .setDatabase(0)
              .setPassword(redisProperties.getPassword());
        
        return Redisson.create(config);
    }
}
