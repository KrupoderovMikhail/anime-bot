package com.krupoderov.animebot.service.impl;

import com.krupoderov.animebot.service.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {

    @Override
    public void unzip(String source, String destination) {
        try {
            ZipFile zipFile = new ZipFile(source);
            log.info("Start unzip file: " + zipFile.getFile().getName());

            for (int i = 0; i < zipFile.getFileHeaders().size(); i++) {
                if (getFilename(zipFile, i).contains("anal/")) {
                    log.info("File anal : " + getFilename(zipFile, i));
                    extractFile(zipFile, getFilename(zipFile, i), destination);
                } else if (getFilename(zipFile, i).contains("dp/")) {
                    log.info("File dp : " + getFilename(zipFile, i));
                    extractFile(zipFile, getFilename(zipFile, i), destination);
                } else if (getFilename(zipFile, i).contains("sf/")) {
                    log.info("File sf : " + getFilename(zipFile, i));
                    extractFile(zipFile, getFilename(zipFile, i), destination);
                } else if (getFilename(zipFile, i).contains("trap/")) {
                    log.info("File trap : " + getFilename(zipFile, i));
                    extractFile(zipFile, getFilename(zipFile, i), destination);
                } else if (getFilename(zipFile, i).contains("vaginal/")) {
                    log.info("File vaginal : " + getFilename(zipFile, i));
                    extractFile(zipFile, getFilename(zipFile, i), destination);
                } else {
                    log.info("Not found category: " + getFilename(zipFile, i));
                }
            }
        } catch (ZipException e) {
            log.error("Issue in unzip archive" + e);
        } finally {
            deleteFile(source);
        }
    }

    private String getFilename(ZipFile zipFile, int count) {
        try {
            return zipFile.getFileHeaders().get(count).toString();
        } catch (ZipException e) {
            throw new RuntimeException("Issue in get the filename", e);
        }
    }

    private void extractFile(ZipFile zipFile, String filename, String destination) {
        try {
            zipFile.extractFile(filename, destination);
        } catch (ZipException e) {
            log.error("Issue in extract file", e);
        }
    }

    private void deleteFile(String path) {
        log.info(path);
        try {
            Files.delete(Paths.get(path));
        } catch (IOException e) {
            log.error("Issue in delete file", e);
        }
    }
}
