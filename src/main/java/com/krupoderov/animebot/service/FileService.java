package com.krupoderov.animebot.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FileService extends Random {

    ArrayList<Integer> list = new ArrayList<>();

    LinkedList<Integer> l = new LinkedList<>();

    public List<String> getNames(String category) {
        File folder = new File("images/" + category);
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

    public String getImage(String category) {
        List<String> names = getNames(category);

        return "images/" + category + "/" + names.get(getRandom(category));
    }

        public int count(String category) {
        int count = 0;
        try (Stream<Path> files = Files.list(Paths.get("images/" + category))) {
            count = (int) files.count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }
}
