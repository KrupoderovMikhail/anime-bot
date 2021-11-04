package com.krupoderov.animebot.service;

import com.krupoderov.animebot.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ParseServiceImpl implements ParseService {

    private final RestTemplate restTemplate;

    public ParseServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Image getImage(String type, String category) {
        String url = "https://api.waifu.pics/" + type + "/" + category;
        ResponseEntity<Image> response = restTemplate.getForEntity(url, Image.class);

        return response.getBody();
    }

    public void test() throws IOException {
        String url = "http://localhost:8090/api/v1/images/1.jpg";
        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
        Files.write(Paths.get("test/image.jpg"), imageBytes);
    }
}
