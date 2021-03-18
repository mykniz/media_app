package ru.mykniz.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RabbitmqProperties {

    String host;
    int port;
    String username;
    String password;
}
