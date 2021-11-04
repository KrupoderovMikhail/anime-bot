package com.krupoderov.animebot.service.bot;

import com.google.common.io.Files;
import com.krupoderov.animebot.config.BotConfig;
import com.krupoderov.animebot.service.ArchiveService;
import com.krupoderov.animebot.service.CallbackService;
import com.krupoderov.animebot.service.MessageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;


/* Basic functionality for interacting with Telegram */
@Component
@Slf4j
public class BotService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final ArchiveService archiveService;
    private final MessageService messageService;
    private final CallbackService callbackService;

    @Value("${file.path.archive}")
    private String archivePath;

    @Value("${file.path.images}")
    private String imagesPath;

    public BotService(BotConfig botConfig, ArchiveService archiveService, MessageService messageService, CallbackService callbackService) {
        this.botConfig = botConfig;
        this.archiveService = archiveService;
        this.messageService = messageService;
        this.callbackService = callbackService;
    }

    //Accepts and processes messages received in private messages or in the channel where the bot administrator is located
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start") ||
                    update.getMessage().getText().equals("start") ||
                    update.getMessage().getText().equals("Хочу картинку")
            ) {
                sendMessage(messageService.getStartMessage(chatId));
            } else if (update.getMessage().getText().equals("/getChatId")) {
                sendMessage(messageService.getMessage(chatId, "Твой chatId: " + chatId));
            }

        } else if (update.hasMessage() && update.getMessage().hasDocument() &&
                update.getMessage().getChatId().toString().equals(botConfig.getOwnerId()) |
                        update.getMessage().getChatId().toString().equals(botConfig.getAdminId())
        ) {
            saveFileFromMessage(update);

        } else if (update.hasCallbackQuery()) {
            // Set variables
            String callback = update.getCallbackQuery().getData();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            sendMessage(callbackService.getMessage(callback, chatId));
            sendMessage(callbackService.getEditMessage(callback, chatId, messageId));
            sendPhoto(callbackService.getPhoto(callback, chatId));

        }
    }

    private void saveFileFromMessage(Update update) {
        if (update.getMessage().getDocument().getFileName().contains(".zip")) {
            long chatId = update.getMessage().getChatId();
            GetFile fileRequest = new GetFile();
            org.telegram.telegrambots.meta.api.objects.File fileTelegram = null;
            try {
                fileRequest.setFileId(update.getMessage().getDocument().getFileId());
                log.info(update.getMessage().getDocument().getFileName());
                fileTelegram = execute(fileRequest);
            } catch (Exception e) {
                sendMessage(messageService.getMessage(chatId, "Размер архива не должен превышать 20 мб"));
                log.error("saveFileFromMessage", e);
            }

            if (fileTelegram != null) {
                try {
                    File file = downloadFile(fileTelegram);
                    String path = archivePath + "/" + update.getMessage().getDocument().getFileName();
                    Files.copy(file, new File(path));
                    archiveService.unzip(path, imagesPath);
                    sendMessage(messageService.getMessage(chatId, "Архив " + update.getMessage().getDocument().getFileName() + " Успешно загружен."));
                } catch (TelegramApiException | IOException e) {
                    sendMessage(messageService.getMessage(chatId, "Ошибка при загрузке файла"));
                    log.error("saveFileFromMessage", e);
                }
            }
        } else if (update.getMessage().getDocument().getFileName().contains(".rar") |
                update.getMessage().getDocument().getFileName().contains(".tar") |
                update.getMessage().getDocument().getFileName().contains(".7z")
        ) {
            long chatId = update.getMessage().getChatId();
            sendMessage(messageService.getMessage(chatId, "Отправьте zip архив \uD83D\uDC12"));
        }

    }

    private void sendMessage(SendMessage message) {
        if (message.getChatId() != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Issue for send message: " + e);
            }
        }
    }

    private void sendMessage(EditMessageText message) {
        if (message.getChatId() != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error("Issue for send message: " + e);
            }
        }
    }

    private void sendPhoto(SendPhoto photo) {
        if (photo.getChatId() != null) {
            try {
                execute(photo);
            } catch (TelegramApiException e) {
                log.error("Issue for send photo: " + e);
            }
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
