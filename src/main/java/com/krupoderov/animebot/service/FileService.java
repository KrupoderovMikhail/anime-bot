package com.krupoderov.animebot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FileService extends Random {

    @Value("${file.path.images}")
    private String imagesPath;

    ArrayList<Integer> list = new ArrayList<>();

    LinkedList<Integer> l = new LinkedList<>();

    public List<String> getNames(String category) {
        File folder = new File(imagesPath + "/" + category);
        File[] listOfFiles = folder.listFiles();

        List<String> names = new ArrayList<>();

        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                names.add(listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

        return names;
    }

    public synchronized int getRandom(String category) {
        int result;
        do result = nextInt(count(category));
        while (l.contains(result));
        l.add(result);
        if (l.size() == 3) l.removeFirst();
        return result;
    }

    public String getImage(String category) throws IOException {
        List<String> names = getNames(category);
        int random = getRandom(category);
        writeToFile(random, category);

        return imagesPath + "/" + category + "/" + names.get(random);
    }

    public int count(String category) {
        int count = 0;
        try (Stream<Path> files = Files.list(Paths.get(imagesPath + "/" + category))) {
            count = (int) files.count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }
}
