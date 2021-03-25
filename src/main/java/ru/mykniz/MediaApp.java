package ru.mykniz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import ru.mykniz.config.RedisProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MediaApp {

    @Autowired
    private RedisProperties redisProperties;

    public static void main(String[] args) {
        SpringApplication.run(MediaApp.class, args);
    }
}
