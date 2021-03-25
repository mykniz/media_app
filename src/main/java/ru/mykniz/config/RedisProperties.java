package ru.mykniz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data

public class RedisProperties {

    public int port;
    public String host;

}
