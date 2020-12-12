package ru.mykniz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import ru.mykniz.entity.MediaStream;
import ru.mykniz.repos.RedisRepo;
import ru.mykniz.services.MediaStreamServices;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    private MediaStreamServices mediaStreamServices;

    @Autowired
    private RedisRepo redisRepo;

    @GetMapping("/")
    public List<? extends MediaStream> findData() {
        return mediaStreamServices.findData();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id", value = RedisRepo.HASH_KEY)
    public MediaStream findDataById(@PathVariable Long id) {
        return mediaStreamServices.findDataById(id);
    }


    @PostMapping("/")
    public String sendData(@RequestBody MediaStream mediaStreamDB) {
        mediaStreamServices.sendData(mediaStreamDB);
        return "The data has been put into queue";
    }

}
