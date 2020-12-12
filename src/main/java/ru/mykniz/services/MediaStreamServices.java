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
import ru.mykniz.repos.RedisRepo;
import ru.mykniz.repos.SpringRepo;
import java.util.List;

@Service
public class MediaStreamServices {

    private static final Logger log = LoggerFactory.getLogger(MediaStreamServices.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private SpringRepo springRepo;

    @Autowired
    private RedisRepo redisRepo;


    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void listenAndSaveData(MediaStreamDB mediaStreamDB) {
        springRepo.save(mediaStreamDB);
        log.info("Data received from queue to db: {}", mediaStreamDB.toString());
    }

    public void sendData(MediaStream mediaStreamDB) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, mediaStreamDB);
    }

    public List<? extends MediaStream> findData() {
        if (redisRepo.findAll().size() != springRepo.findAll().size()) {
            for (MediaStreamDB media : springRepo.findAll()) {
                redisRepo.save(media);
            }
            return springRepo.findAll();
        }
        return redisRepo.findAll();
    }

    public MediaStream findDataById(Long id) {


        if (redisRepo.findById(id) == null) {
            MediaStream data = springRepo.findById(id).get();
            redisRepo.save(data);
            System.out.println("called findDataById from DB");
            return data;
        }
        System.out.println("called findDataById from cache");
        return redisRepo.findById(id);
    }
}
