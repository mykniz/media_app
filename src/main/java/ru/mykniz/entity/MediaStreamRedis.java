package ru.mykniz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "MediaStreamRedis")
public class MediaStreamRedis implements MediaStream {

    @Id
    private Long id;
    private String user_name;
    private String first_name;
    private String last_name;
    private String gender;
    private String email;
    private String country;

}
