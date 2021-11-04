package com.krupoderov.animebot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ButtonServiceImpl implements ButtonService {

    @Override
    public void setButtons(SendMessage message, String buttonName, String callbackData) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        message.setReplyMarkup(markupInline);
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton sfw_waifu = new InlineKeyboardButton();

        rowInline.add(sfw_waifu);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        sfw_waifu.setText(buttonName);
        sfw_waifu.setCallbackData(callbackData);
    }

    @Override
    public void setButtons(SendPhoto message, String buttonName, String callbackData) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        message.setReplyMarkup(markupInline);
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();

        rowInline.add(button);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        button.setText(buttonName);
        button.setCallbackData(callbackData);
    }

    @Override
    public void setButtons(SendPhoto message, String firstButtonText, String secondButtonText, String firstButtonCallback, String secondButtonCallback) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        message.setReplyMarkup(markupInline);
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton firstButton = new InlineKeyboardButton();
        InlineKeyboardButton secondButton = new InlineKeyboardButton();

        rowInline.add(firstButton);
        rowInline.add(secondButton);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        firstButton.setText(firstButtonText);
        firstButton.setCallbackData(firstButtonCallback);
        secondButton.setText(secondButtonText);
        secondButton.setCallbackData(secondButtonCallback);
    }

    @Override
    public void setButtons(SendPhoto message, String firstButtonText, String secondButtonText, String thirdButtonText, String firstButtonCallback, String secondButtonCallback, String thirdButtonCallback) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        message.setReplyMarkup(markupInline);
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> firstRowInline = new ArrayList<>();
        List<InlineKeyboardButton> secondRowInline = new ArrayList<>();
        List<InlineKeyboardButton> thirdRowInline = new ArrayList<>();

        InlineKeyboardButton firstButton = new InlineKeyboardButton();
        InlineKeyboardButton secondButton = new InlineKeyboardButton();
        InlineKeyboardButton thirdButton = new InlineKeyboardButton();
        InlineKeyboardButton like = new InlineKeyboardButton();
        InlineKeyboardButton dislike = new InlineKeyboardButton();

        firstRowInline.add(firstButton);
        firstRowInline.add(secondButton);
        secondRowInline.add(thirdButton);
        rowsInline.add(firstRowInline);
        rowsInline.add(secondRowInline);
        markupInline.setKeyboard(rowsInline);
        firstButton.setText(firstButtonText);
        firstButton.setCallbackData(firstButtonCallback);
        secondButton.setText(secondButtonText);
        secondButton.setCallbackData(secondButtonCallback);
        thirdButton.setText(thirdButtonText);
        thirdButton.setCallbackData(thirdButtonCallback);
    }
}
