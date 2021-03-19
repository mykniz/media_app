package ru.mykniz.endpoints;

import lombok.Data;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Endpoint(id = "app-endpoint")
@ConfigurationProperties(prefix = "spring.application")
@Data
public class AppEndpoint {

    String name;

    Map<String, List<String>> appInfo = new HashMap<>();

    @PostConstruct
    public void init() {
        appInfo.put("version 0.1",
                Arrays.asList(this.name + " for localhost created",
                        "postgresql for localhost used",
                        "redis for localhost used",
                        "rabbitmq for localhost used"));
        appInfo.put("version 0.2",
                Arrays.asList(this.name + " for docker created",
                        "postgresql for docker used",
                        "redis for docker used",
                        "rabbitmq for docker used"));
    }

    @ReadOperation
    public Map<String, List<String>> getAppInfo() {
        return appInfo;
    }

}
