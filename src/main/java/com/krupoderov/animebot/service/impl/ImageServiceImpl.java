package com.krupoderov.animebot.service.impl;

import com.krupoderov.animebot.service.ButtonService;
import com.krupoderov.animebot.service.FileService;
import com.krupoderov.animebot.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final FileService fileService;
    private final ButtonService buttonService;

    public ImageServiceImpl(FileService fileService, ButtonService buttonService) {
        this.fileService = fileService;
        this.buttonService = buttonService;
    }

    @Override
    public SendPhoto getPhoto(long chatId, String category, String firstButtonName, String secondButtonName, String firstCallbackData, String secondCallbackData) throws IOException {
        SendPhoto message = new SendPhoto(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        String image = fileService.getImage(category);
        message.setPhoto(new InputFile(new File(image)));

        log.info("\nFilename: " + image + "\nCategory: " + category);

        return message;
    }

    @Override
    public SendPhoto getPhoto(long chatId, String category, String firstButtonName, String secondButtonName, String thirdButtonName, String firstCallbackData, String secondCallbackData, String thirdButtonCallbackData) throws IOException {
        SendPhoto message = new SendPhoto();
        message.setChatId(String.valueOf(chatId));
        String image = fileService.getImage(category);
        if (image != null) {
            message.setPhoto(new InputFile(new File(image)));

            log.info("\nFilename: " + image + "\nCategory: " + category);
            log.info("callback: " + firstCallbackData + " " + secondCallbackData);

            buttonService.setButtons(message, firstButtonName, secondButtonName, thirdButtonName, firstCallbackData, secondCallbackData, thirdButtonCallbackData);
        }

        return message;
    }
}
