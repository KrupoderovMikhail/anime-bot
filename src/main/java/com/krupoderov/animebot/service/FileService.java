package com.krupoderov.animebot.service;

import java.util.List;

public interface FileService {
    List<String> getNames(String category);

    String getImage(String category);
}
