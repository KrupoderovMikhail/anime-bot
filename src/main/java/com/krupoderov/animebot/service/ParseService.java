package com.krupoderov.animebot.service;

import com.krupoderov.animebot.model.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ParseService {

    private final RestTemplate restTemplate;

    public ParseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Image getImage(String type, String category) {
        String url = "https://api.waifu.pics/" + type + "/" + category;
        ResponseEntity<Image> response = restTemplate.getForEntity(url, Image.class);

        return response.getBody();
    }
}
