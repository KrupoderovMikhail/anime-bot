package com.krupoderov.animebot.service;

import com.krupoderov.animebot.model.Image;

import java.io.IOException;

public interface ParseService {

    Image getImage(String type, String category);

    void test() throws IOException;
}
