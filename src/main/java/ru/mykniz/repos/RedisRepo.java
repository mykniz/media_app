package ru.mykniz.repos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import ru.mykniz.entity.MediaStream;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepo {

    public static final String HASH_KEY = "MediaStreamRedis";

    @Autowired
    private RedisTemplate redisTemplate;

    public MediaStream save(MediaStream mediaStream) {
        redisTemplate.opsForHash().put(HASH_KEY, mediaStream.getId(), mediaStream);
        redisTemplate.expire(RedisRepo.HASH_KEY, 10, TimeUnit.SECONDS);
        return mediaStream;
    }

    public List<MediaStream> findAll() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    public MediaStream findById(Long id) {
        return (MediaStream) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

}
