package com.krupoderov.animebot.service;

import com.google.common.io.Files;
import com.krupoderov.animebot.config.BotConfig;
import com.krupoderov.animebot.enumeration.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;


/* Basic functionality for interacting with Telegram */
@Component
@Slf4j
public class BotService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final ParseService parseService;
    private final FileService fileService;
    private final ArchiveService archiveService;

    public BotService(BotConfig botConfig, ParseService parseService, FileService fileService, ArchiveService archiveService) {
        this.botConfig = botConfig;
        this.parseService = parseService;
        this.fileService = fileService;
        this.archiveService = archiveService;
    }

    @Value("${file.path.archive}")
    private String archivePath;

    @Value("${file.path.images}")
    private String imagesPath;

    //Accepts and processes messages received in private messages or in the channel where the bot administrator is located
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start") ||
                    update.getMessage().getText().equals("start") ||
                    update.getMessage().getText().equals("Хочу картинку")
            ) {
                startMessage(chatId);
            } else if (update.getMessage().getText().equals("/getChatId")) {
                sendMessage(chatId, "Твой chatId: " + chatId);
            }

        } else if (update.hasMessage() && update.getMessage().hasDocument() &&
                update.getMessage().getChatId().toString().equals(botConfig.getOwnerId()) |
                        update.getMessage().getChatId().toString().equals(botConfig.getAdminId())
        ) {
            saveFileFromMessage(update);

        } else if (update.hasCallbackQuery()) {
            // Set variables
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals("menu")) {
                log.info("Menu");
                log.info("callback: " + callbackData);
                startMessage(chatId);
            }

            if (callbackData.equals("nsfw")) {
                String answer = "Выберите категорию";
                EditMessageText new_message = new EditMessageText();
                new_message.setChatId(chatId);
                new_message.setMessageId(toIntExact(messageId));
                new_message.setText(answer);

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
                new_message.setReplyMarkup(markupInline);

                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            /* NSFW */

            /* Traps */
            if (callbackData.equals(Type.NSFW.getLabel() + Category.TRAP.getLabel())) {
                sendImage(chatId, Category.TRAP.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.TRAP.getLabel(), Command.MENU.getLabel());
            }

            /* Anal */
            if (callbackData.equals(Type.NSFW.getLabel() + Category.ANAL.getLabel())) {
                sendImage(chatId, Category.ANAL.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.ANAL.getLabel(), Command.MENU.getLabel());
            }

            /* Double penetration */
            if (callbackData.equals(Type.NSFW.getLabel() + Category.DP.getLabel())) {
                sendImage(chatId, Category.DP.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.DP.getLabel(), Command.MENU.getLabel());
            }

            /* Solo female */
            if (callbackData.equals(Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel())) {
                sendImage(chatId, Category.SOLO_FEMALE.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel(), Command.MENU.getLabel());
            }

            /* Vaginal */
            if (callbackData.equals(Type.NSFW.getLabel() + Category.VAGINAL.getLabel())) {
                sendImage(chatId, Category.VAGINAL.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.VAGINAL.getLabel(), Command.MENU.getLabel());
            }

            /* SFW */

            /* Waifu */
            if (callbackData.equals("sfw_waifu")) {
                sendMessageByUrl(chatId, "waifu", "[Одетая вайфу](", "sfw_waifu");
            }

            /* Neko */
            if (callbackData.equals("sfw_neko")) {
                sendMessageByUrl(chatId, "neko", "[Одетая кошкодевочка](", "sfw_neko");
            }

            /* Shinobu */
            if (callbackData.equals("sfw_shinobu")) {
                sendMessageByUrl(chatId, "shinobu", "[Синобутян](", "sfw_shinobu");
            }

            /* Megumin */
            if (callbackData.equals("sfw_megumin")) {
                sendMessageByUrl(chatId, "megumin", "[Мегумин](", "sfw_megumin");
            }

            /* Bully */
            if (callbackData.equals("sfw_bully")) {
                sendMessageByUrl(chatId, "bully", "[Буллинг](", "sfw_bully");
            }

            /* Cuddle */
            if (callbackData.equals("sfw_cuddle")) {
                sendMessageByUrl(chatId, "cuddle", "[:3](", "sfw_cuddle");
            }

            /* Cry */
            if (callbackData.equals("sfw_cry")) {
                sendMessageByUrl(chatId, "cry", "[:(](", "sfw_cry");
            }

            /* Hug */
            if (callbackData.equals("sfw_hug")) {
                sendMessageByUrl(chatId, "hug", "[Обнимашки:3](", "sfw_hug");
            }
        }
    }

    private void saveFileFromMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        GetFile fileRequest = new GetFile();
        org.telegram.telegrambots.meta.api.objects.File fileTelegram = null;
        try {
            fileRequest.setFileId(update.getMessage().getDocument().getFileId());
            log.info(update.getMessage().getDocument().getFileName());
            fileTelegram = execute(fileRequest);
        } catch (Exception e) {
            sendMessage(chatId, "Размер архива не должен превышать 20 мб");
            log.error("saveFileFromMessage", e);
        }

        if (fileTelegram != null) {
            try {
                File file = downloadFile(fileTelegram);
                String path = archivePath + "/" + update.getMessage().getDocument().getFileName();
                Files.copy(file, new File(path));
                archiveService.unzip(path, "image");

            } catch (TelegramApiException | IOException e) {
                sendMessage(chatId, "Ошибка при загрузке файла");
                log.error("saveFileFromMessage", e);
            }
            sendMessage(chatId, "Архив " + update.getMessage().getDocument().getFileName() + " Успешно загружен.");
        }
    }

    private void startMessage(long chatId) {
        SendMessage message = new SendMessage(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        message.setText("Что выберите сегодня?");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton sfw = new InlineKeyboardButton();
        InlineKeyboardButton nsfw = new InlineKeyboardButton();
        InlineKeyboardButton keyboard = new InlineKeyboardButton();
        sfw.setText("SFW");
        sfw.setCallbackData("sfw");
        nsfw.setText("NSFW");
        nsfw.setCallbackData("nsfw");
//        keyboard.setText("About");
//        keyboard.setCallbackData("about");
        rowInline.add(sfw);
        rowInline.add(nsfw);
//        rowInline.add(keyboard);
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Set button for SendPhoto
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

    // Set button for SendPhoto
    public void setButtons(SendPhoto message, String buttonName, String callbackData) {
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

    // Send message by URL
    private void sendMessageByUrl(long chatId, String imageName, String description, String callbackData) {
        SendMessage message = new SendMessage(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        message.setParseMode(ParseMode.MARKDOWN);
        String url = parseService.getImage("sfw", imageName).getUrl();
        message.setText(description + url + ")");
        try {
            setButtons(message, "Хочу еще", callbackData);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Send image
    private void sendImage(long chatId, String category, String callbackData) {
        SendPhoto message = new SendPhoto(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        String image = fileService.getImage(category);
        message.setPhoto(new InputFile(new File(image)));

        log.info("\nFilename: " + image + "\nCategory: " + category);

        try {
            setButtons(message, "Хочу еще", callbackData);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Send image
    private void sendImage(long chatId, String category) {
        SendPhoto message = new SendPhoto(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        String image = fileService.getImage(category);
        message.setPhoto(new InputFile(new File(image)));

        log.info("\nFilename: " + image + "\nCategory: " + category);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Return bot name
    public String getBotUsername() {
        return botConfig.getBotUserName();
    }

    //Return bot token
    public String getBotToken() {
        return botConfig.getToken();
    }
}
