package com.bot.tg.meme.search.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

@Component
public class StaticGifProvider implements FileProvider {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public byte[] getFile(String message) throws IOException {

        ClassLoader classLoader = StaticGifProvider.class.getClassLoader();
        URL resource = classLoader.getResource("static/icegif-380.gif");

        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        }
        Resource gifResource = restTemplate.getForObject("https://media1.giphy.com/media/cZ7rmKfFYOvYI/100.mp4", Resource.class);

        byte[] gifBytes = null;
        if (gifResource != null) {
            gifBytes = gifResource.getInputStream().readAllBytes(); // Java 9+
        }


        return gifBytes;
    }
}
