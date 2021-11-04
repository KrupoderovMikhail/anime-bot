package com.krupoderov.animebot.service;

import com.krupoderov.animebot.model.Image;

import java.io.IOException;

public interface ParseService {

    public Image getImage(String type, String category);

    public void test() throws IOException;
}
