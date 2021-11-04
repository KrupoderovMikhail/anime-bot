package com.krupoderov.animebot.service.impl;

import com.krupoderov.animebot.enumeration.Category;
import com.krupoderov.animebot.enumeration.Type;
import com.krupoderov.animebot.service.ButtonService;
import com.krupoderov.animebot.service.MessageService;
import com.krupoderov.animebot.service.ParseService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

@Service
public class MessageServiceImpl implements MessageService {

    private final ParseService parseService;
    private final ButtonService buttonService;

    public MessageServiceImpl(ParseService parseService, ButtonService buttonService) {
        this.parseService = parseService;
        this.buttonService = buttonService;
    }

    @Override
    public SendMessage getStartMessage(long chatId) {
        SendMessage message = new SendMessage(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        message.setText("Что выберите сегодня?");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton sfw = new InlineKeyboardButton();
        InlineKeyboardButton nsfw = new InlineKeyboardButton();
        InlineKeyboardButton test = new InlineKeyboardButton();
        sfw.setText("SFW");
        sfw.setCallbackData("sfw");
        nsfw.setText("NSFW");
        nsfw.setCallbackData("nsfw");
        test.setText("Test");
        test.setCallbackData("test");
//        keyboard.setText("About");
//        keyboard.setCallbackData("about");
        rowInline.add(sfw);
        rowInline.add(nsfw);
        rowInline.add(test);
//        rowInline.add(keyboard);
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        return message; // Sending our message object to user
    }

    @Override
    public SendMessage getMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }

    @Override
    public EditMessageText getNsfwEditMessageByCallback(String callback, long chatId, int messageId) {
        EditMessageText newMessage = new EditMessageText();
        String answer = "Выберите категорию";
        newMessage.setChatId(chatId);
        newMessage.setMessageId(messageId);
        newMessage.setText(answer);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton anal = new InlineKeyboardButton();
        InlineKeyboardButton dp = new InlineKeyboardButton();
        InlineKeyboardButton sf = new InlineKeyboardButton();
        InlineKeyboardButton trap = new InlineKeyboardButton();
        InlineKeyboardButton vaginal = new InlineKeyboardButton();

        anal.setText("Anal");
        anal.setCallbackData(Type.NSFW.getLabel() + Category.ANAL.getLabel());

        dp.setText("DP");
        dp.setCallbackData(Type.NSFW.getLabel() + Category.DP.getLabel());

        sf.setText("SF");
        sf.setCallbackData(Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel());

        trap.setText("Trap");
        trap.setCallbackData(Type.NSFW.getLabel() + Category.TRAP.getLabel());

        vaginal.setText("Classic");
        vaginal.setCallbackData(Type.NSFW.getLabel() + Category.VAGINAL.getLabel());

        rowInline.add(anal);
        rowInline.add(dp);
        rowInline.add(sf);
        rowInline.add(trap);
        rowInline.add(vaginal);

        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        newMessage.setReplyMarkup(markupInline);

        return newMessage;
    }

    @Override
    public EditMessageText getEditMessage(long messageId, long chatId, String message, String firstButtonText, String firstButtonCallback, String secondButtonText, String secondButtonCallback, String thirdButtonText, String thirdButtonCallback, String fourButtonText, String fourButtonCallback, String fiveButtonText, String fiveButtonCallback, String sixButtonText, String sixButtonCallback) {
        EditMessageText newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(toIntExact(messageId));
        newMessage.setText(message);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInlineFirst = new ArrayList<>();
        List<InlineKeyboardButton> rowInlineSecond = new ArrayList<>();
        InlineKeyboardButton firstButton = new InlineKeyboardButton();
        InlineKeyboardButton secondButton = new InlineKeyboardButton();
        InlineKeyboardButton thirdButton = new InlineKeyboardButton();
        InlineKeyboardButton fourButton = new InlineKeyboardButton();
        InlineKeyboardButton fiveButton = new InlineKeyboardButton();
        InlineKeyboardButton sixButton = new InlineKeyboardButton();

        firstButton.setText(firstButtonText);
        firstButton.setCallbackData(firstButtonCallback);

        secondButton.setText(secondButtonText);
        secondButton.setCallbackData(secondButtonCallback);

        thirdButton.setText(thirdButtonText);
        thirdButton.setCallbackData(thirdButtonCallback);

        fourButton.setText(fourButtonText);
        fourButton.setCallbackData(fourButtonCallback);

        fiveButton.setText(fiveButtonText);
        fiveButton.setCallbackData(fiveButtonCallback);

        sixButton.setText(sixButtonText);
        sixButton.setCallbackData(sixButtonCallback);

        rowInlineFirst.add(firstButton);
        rowInlineFirst.add(secondButton);
        rowInlineFirst.add(thirdButton);
        rowInlineFirst.add(fourButton);
        rowInlineFirst.add(fiveButton);

        rowInlineSecond.add(sixButton);

        // Set the keyboard to the markup
        rowsInline.add(rowInlineFirst);
        rowsInline.add(rowInlineSecond);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        newMessage.setReplyMarkup(markupInline);

        return newMessage;
    }

    @Override
    public EditMessageText getEditMessage(long messageId, long chatId, String message, String firstButtonText, String firstButtonCallback, String secondButtonText, String secondButtonCallback, String thirdButtonText, String thirdButtonCallback, String fourButtonText, String fourButtonCallback, String fiveButtonText, String fiveButtonCallback) {
        EditMessageText newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(toIntExact(messageId));
        newMessage.setText(message);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton firstButton = new InlineKeyboardButton();
        InlineKeyboardButton secondButton = new InlineKeyboardButton();
        InlineKeyboardButton thirdButton = new InlineKeyboardButton();
        InlineKeyboardButton fourButton = new InlineKeyboardButton();
        InlineKeyboardButton fiveButton = new InlineKeyboardButton();

        firstButton.setText(firstButtonText);
        firstButton.setCallbackData(firstButtonCallback);

        secondButton.setText(secondButtonText);
        secondButton.setCallbackData(secondButtonCallback);

        thirdButton.setText(thirdButtonText);
        thirdButton.setCallbackData(thirdButtonCallback);

        fourButton.setText(fourButtonText);
        fourButton.setCallbackData(fourButtonCallback);

        fiveButton.setText(fiveButtonText);
        fiveButton.setCallbackData(fiveButtonCallback);

        rowInline.add(firstButton);
        rowInline.add(secondButton);
        rowInline.add(thirdButton);
        rowInline.add(fourButton);
        rowInline.add(fiveButton);

        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        newMessage.setReplyMarkup(markupInline);

        return newMessage;
    }

    @Override
    public SendMessage getMessageByUrl(long chatId, String imageName, String description, String callbackData) {
        SendMessage message = new SendMessage(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        message.setParseMode(ParseMode.MARKDOWN);
        String url = parseService.getImage("sfw", imageName).getUrl();
        message.setText(description + url + ")");
        buttonService.setButtons(message, "Хочу еще", callbackData);

        return message;
    }
}
