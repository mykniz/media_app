package ru.mykniz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.mykniz.config.RabbitMQConfig;
import ru.mykniz.entity.MediaStream;
import ru.mykniz.entity.MediaStreamDB;
import ru.mykniz.exception.ResourceNotFoundException;
import ru.mykniz.repos.RedisRepo;
import ru.mykniz.repos.SpringRepo;

import java.util.List;
import java.util.Objects;

@Service
public class MediaStreamServices {

    private static final Logger log = LoggerFactory.getLogger(MediaStreamServices.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SpringRepo springRepo;

    @Autowired
    private RedisRepo redisRepo;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listenAndSaveData(MediaStreamDB mediaStreamDB) {
        springRepo.save(mediaStreamDB);
        log.info("Data received from queue to db: {}", mediaStreamDB.toString());
    }

    public void byPassSaveData(MediaStreamDB mediaStreamDB) {
        springRepo.save(mediaStreamDB);
    }

    public void sendData(MediaStream mediaStreamDB) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, mediaStreamDB);
    }

    public List<? extends MediaStream> findData() {


        List<MediaStreamDB> mediaList = springRepo.findAll();
        int springRepoSize = mediaList.size();
        int redisRepoSize = redisRepo.findAll().size();


        if (!redisTemplate.hasKey(redisRepo.HASH_KEY)) {
            for (MediaStreamDB media : mediaList) {
                redisRepo.save(media);
            }
            System.out.println("Redis is empty. The data received from Postgres");
            return springRepo.findAll();
        }
        else if (redisRepoSize != springRepoSize) {
            for (MediaStreamDB media : mediaList) {
                redisRepo.save(media);
            }
            System.out.println("Redis cash updated. The data received from Postgres");
            return mediaList;
        }
        else {
            System.out.println("The data received from Redis");
            return redisRepo.findAll();
        }
    }

    public MediaStream findDataById(Long id) throws ResourceNotFoundException {

        MediaStream resultFromRedis = redisRepo.findById(id);

        if (Objects.isNull(resultFromRedis)) {
            MediaStream data = springRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("The data not found"));
            redisRepo.save(data);
            System.out.println("called findDataById from DB");
            return data;
        }
        else {
            System.out.println("called findDataById from cache");
            return resultFromRedis;
        }
    }
}


