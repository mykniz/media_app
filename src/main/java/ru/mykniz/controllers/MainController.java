package ru.mykniz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mykniz.entity.MediaStream;
import ru.mykniz.entity.MediaStreamDB;
import ru.mykniz.exception.ResourceNotFoundException;
import ru.mykniz.repos.RedisRepo;
import ru.mykniz.services.MediaStreamServices;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    private MediaStreamServices mediaStreamServices;

    @GetMapping("/")
    public List<? extends MediaStream> findData() {
        return mediaStreamServices.findData();
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#id", value = RedisRepo.HASH_KEY)
    public MediaStream findDataById(@PathVariable Long id) throws ResourceNotFoundException {
        return mediaStreamServices.findDataById(id);
    }

    @PostMapping("/")
    public String sendData(@RequestBody MediaStreamDB mediaStreamDB) {
        mediaStreamServices.sendData(mediaStreamDB);
        return "The data has been put into queue";
    }
}
