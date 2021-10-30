package com.krupoderov.animebot.service;

import com.google.common.io.Files;
import com.krupoderov.animebot.config.BotConfig;
import com.krupoderov.animebot.enumeration.Category;
import com.krupoderov.animebot.enumeration.Type;
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
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("menu")) {
                log.info("Menu");
                log.info("callback: " + call_data);
                startMessage(chatId);
            }

            if (call_data.equals("nsfw")) {
                String answer = "Выберите категорию";
                EditMessageText new_message = new EditMessageText();
                new_message.setChatId(chatId);
                new_message.setMessageId(toIntExact(message_id));
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

            if (call_data.equals("sfw")) {
                String answer = "Выберите категорию";
                EditMessageText new_message = new EditMessageText();
                new_message.setChatId(chatId);
                new_message.setMessageId(toIntExact(message_id));
                new_message.setText(answer);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                // waifu neko shinobu megumin
                InlineKeyboardButton waifu = new InlineKeyboardButton();
                InlineKeyboardButton neko = new InlineKeyboardButton();
                InlineKeyboardButton shinobu = new InlineKeyboardButton();
                InlineKeyboardButton megumin = new InlineKeyboardButton();
                InlineKeyboardButton next1 = new InlineKeyboardButton();

                waifu.setText("Waifu");
                waifu.setCallbackData("sfw_waifu");
                neko.setText("Neko");
                neko.setCallbackData("sfw_neko");
                shinobu.setText("Shinobu");
                shinobu.setCallbackData("sfw_shinobu");
                megumin.setText("Megumin");
                megumin.setCallbackData("sfw_megumin");

                next1.setText("Next >");
                next1.setCallbackData("sfw_next1");

                rowInline.add(waifu);
                rowInline.add(neko);
                rowInline.add(shinobu);
                rowInline.add(megumin);
                rowInline.add(next1);

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

            if (call_data.equals("sfw_next1")) {
                String answer = "Выберите категорию";
                EditMessageText new_message = new EditMessageText();
                new_message.setChatId(chatId);
                new_message.setMessageId(toIntExact(message_id));
                new_message.setText(answer);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                // bully cuddle cry hug
                InlineKeyboardButton bully = new InlineKeyboardButton();
                InlineKeyboardButton cuddle = new InlineKeyboardButton();
                InlineKeyboardButton cry = new InlineKeyboardButton();
                InlineKeyboardButton hug = new InlineKeyboardButton();
                InlineKeyboardButton next2 = new InlineKeyboardButton();

                bully.setText("Bully");
                bully.setCallbackData("sfw_bully");
                cuddle.setText("Cuddle");
                cuddle.setCallbackData("sfw_cuddle");
                cry.setText("Cry");
                cry.setCallbackData("sfw_cry");
                hug.setText("Hug");
                hug.setCallbackData("sfw_hug");
                next2.setText("Next >");
                next2.setCallbackData("sfw_next2");

                rowInline.add(bully);
                rowInline.add(cuddle);
                rowInline.add(cry);
                rowInline.add(hug);
                rowInline.add(next2);

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
            if (call_data.equals(Type.NSFW.getLabel() + Category.TRAP.getLabel())) {
                sendImage(chatId, Category.TRAP.getLabel(), Type.NSFW.getLabel() + Category.TRAP.getLabel());
            }

            /* Anal */
            if (call_data.equals(Type.NSFW.getLabel() + Category.ANAL.getLabel())) {
                sendImage(chatId, Category.ANAL.getLabel(), Type.NSFW.getLabel() + Category.ANAL.getLabel());
            }

            /* Double penetration */
            if (call_data.equals(Type.NSFW.getLabel() + Category.DP.getLabel())) {
                sendImage(chatId, Category.DP.getLabel(), Type.NSFW.getLabel() + Category.DP.getLabel());
            }

            /* Solo female */
            if (call_data.equals(Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel())) {
                sendImage(chatId, Category.SOLO_FEMALE.getLabel(), Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel());
            }

            /* Vaginal */
            if (call_data.equals(Type.NSFW.getLabel() + Category.VAGINAL.getLabel())) {
                sendImage(chatId, Category.VAGINAL.getLabel(), Type.NSFW.getLabel() + Category.VAGINAL.getLabel());
            }

            /* SFW */

            /* Waifu */
            if (call_data.equals("sfw_waifu")) {
                sendMessageByUrl(chatId, "waifu", "[Одетая вайфу](", "sfw_waifu");
            }

            /* Neko */
            if (call_data.equals("sfw_neko")) {
                sendMessageByUrl(chatId, "neko", "[Одетая кошкодевочка](", "sfw_neko");
            }

            /* Shinobu */
            if (call_data.equals("sfw_shinobu")) {
                sendMessageByUrl(chatId, "shinobu", "[Синобутян](", "sfw_shinobu");
            }

            /* Megumin */
            if (call_data.equals("sfw_megumin")) {
                sendMessageByUrl(chatId, "megumin", "[Мегумин](", "sfw_megumin");
            }

            /* Bully */
            if (call_data.equals("sfw_bully")) {
                sendMessageByUrl(chatId, "bully", "[Буллинг](", "sfw_bully");
            }

            /* Cuddle */
            if (call_data.equals("sfw_cuddle")) {
                sendMessageByUrl(chatId, "cuddle", "[:3](", "sfw_cuddle");
            }

            /* Cry */
            if (call_data.equals("sfw_cry")) {
                sendMessageByUrl(chatId, "cry", "[:(](", "sfw_cry");
            }

            /* Hug */
            if (call_data.equals("sfw_hug")) {
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
