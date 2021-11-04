package com.krupoderov.animebot.service.impl;

import com.krupoderov.animebot.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileServiceImpl extends Random implements FileService {

    @Value("${file.path.images}")
    private String imagesPath;

    ArrayList<Integer> list = new ArrayList<>();

    LinkedList<Integer> l = new LinkedList<>();

    @Override
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

    // Test
    @Override
    public String getImage(String category) {
        List<String> names = getNames(category);
        String image = null;
        int random = 0;
        try {
            writeToFile(random, category);
            image = imagesPath + "/" + category + "/" + names.get(random);
        } catch (IOException | IndexOutOfBoundsException e) {
            log.error("Issue for get image: " + e);
        }

        return image;
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

    public void writeToFile(int number, String filename) throws IOException {
        File log = new File("logs/" + filename + ".txt");

        try {
            if (!log.exists()) {
                log.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(log, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(number + "\n");
            bufferedWriter.close();

            System.out.println("Done");
        } catch (IOException e) {
        }
    }
}
