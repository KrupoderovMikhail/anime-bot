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

            if (callbackData.equals("test")) {
                sendCallback(messageId, chatId, "На кого посмотрим? \uD83D\uDC41 \uD83D\uDC41", "Girl", Type.NSFW.getLabel() + TestCategory.GIRL.getLabel(), "Trap", Type.NSFW.getLabel() + TestCategory.TRAP.getLabel(), "Monster", Type.NSFW.getLabel() + TestCategory.MONSTER.getLabel(), "Milf", Type.NSFW.getLabel() + TestCategory.MILF.getLabel(), "Loli", Type.NSFW.getLabel() + TestCategory.LOLI.getLabel());
            }

            if (callbackData.equals("sfw")) {
                sendCallback(messageId, chatId, "Выберите категорию", "Waifu", "sfw_waifu", "Neko", "sfw_neko", "Shinobu", "sfw_shinobu", "Megumin", "sfw_megumin", "Next >", "sfw_next1");
            }

            if (callbackData.equals("sfw_next1")) {
                sendCallback(messageId, chatId, "Выберите категорию", "Bully", "sfw_bully", "Cuddle", "sfw_cuddle", "Cry", "sfw_cry", "Hug", "sfw_hug", "Next >", "sfw_next2");
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

            /* Test */

            /* Girl */
            if (callbackData.equals(Type.NSFW.getLabel() + TestCategory.GIRL.getLabel())) {
                sendCallback(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                        "Sex", GirlCategory.SEX.getLabel(),
                        "Yuri", GirlCategory.YURI.getLabel(),
                        "Blowjob", GirlCategory.BLOWJOB.getLabel(),
                        "BDSM", GirlCategory.BDSM.getLabel(),
                        "School", GirlCategory.SCHOOL.getLabel(),
                        "Назад", "test");
            }

            // Sex
            if (callbackData.equals(GirlCategory.SEX.getLabel())) {
                sendImage(chatId, GirlCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.SEX.getLabel());
            }

            // Yuri
            if (callbackData.equals(GirlCategory.YURI.getLabel())) {
                sendImage(chatId, GirlCategory.YURI.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.YURI.getLabel());
            }

            // Blowjob
            if (callbackData.equals(GirlCategory.BLOWJOB.getLabel())) {
                sendImage(chatId, GirlCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.BLOWJOB.getLabel());
            }

            // BDSM
            if (callbackData.equals(GirlCategory.BDSM.getLabel())) {
                sendImage(chatId, GirlCategory.BDSM.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.BDSM.getLabel());
            }

            // School
            if (callbackData.equals(GirlCategory.SCHOOL.getLabel())) {
                sendImage(chatId, GirlCategory.SCHOOL.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.SCHOOL.getLabel());
            }

            /* Trap */
            if (callbackData.equals(Type.NSFW.getLabel() + TestCategory.TRAP.getLabel())) {
                sendCallback(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                        "Sex", TrapCategory.SEX.getLabel(),
                        "BDSM", TrapCategory.BDSM.getLabel(),
                        "Blowjob", TrapCategory.BLOWJOB.getLabel(),
                        "School", TrapCategory.SCHOOL.getLabel(),
                        "Gang Bang", TrapCategory.GANGBANG.getLabel(),
                        "Назад", "test");
            }

            // Sex
            if (callbackData.equals(TrapCategory.SEX.getLabel())) {
                sendImage(chatId, TrapCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.SEX.getLabel());
            }

            // BDSM
            if (callbackData.equals(TrapCategory.BDSM.getLabel())) {
                sendImage(chatId, TrapCategory.BDSM.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.BDSM.getLabel());
            }

            // Blowjob
            if (callbackData.equals(TrapCategory.BLOWJOB.getLabel())) {
                sendImage(chatId, TrapCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.BLOWJOB.getLabel());
            }

            // School
            if (callbackData.equals(TrapCategory.SCHOOL.getLabel())) {
                sendImage(chatId, TrapCategory.SCHOOL.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.SCHOOL.getLabel());
            }

            // Gang Bang
            if (callbackData.equals(TrapCategory.GANGBANG.getLabel())) {
                sendImage(chatId, TrapCategory.GANGBANG.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.GANGBANG.getLabel());
            }


            /* Monster */
            if (callbackData.equals(Type.NSFW.getLabel() + TestCategory.MONSTER.getLabel())) {
                sendCallback(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                        "Blowjob", MonsterCategory.BLOWJOB.getLabel(),
                        "Sex", MonsterCategory.SEX.getLabel(),
                        "BDSM", MonsterCategory.HANDJOB.getLabel(),
                        "Handjob", MonsterCategory.HANDJOB.getLabel(),
                        "Ahegao", MonsterCategory.AHEGAO.getLabel(),
                        "Назад", "test");
            }

            // Blowjob
            if (callbackData.equals(MonsterCategory.BLOWJOB.getLabel())) {
                sendImage(chatId, MonsterCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.BLOWJOB.getLabel());
            }

            // Sex
            if (callbackData.equals(MonsterCategory.SEX.getLabel())) {
                sendImage(chatId, MonsterCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.SEX.getLabel());
            }

            // BDSM
            if (callbackData.equals(MonsterCategory.BDSM.getLabel())) {
                sendImage(chatId, MonsterCategory.BDSM.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.BDSM.getLabel());
            }

            // Handjob
            if (callbackData.equals(MonsterCategory.HANDJOB.getLabel())) {
                sendImage(chatId, MonsterCategory.HANDJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.HANDJOB.getLabel());
            }

            // Ahegao
            if (callbackData.equals(MonsterCategory.AHEGAO.getLabel())) {
                sendImage(chatId, MonsterCategory.AHEGAO.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.AHEGAO.getLabel());
            }


            /* Milf */
            if (callbackData.equals(Type.NSFW.getLabel() + TestCategory.MILF.getLabel())) {
                sendCallback(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                        "Blowjob", MilfCategory.SEX.getLabel(),
                        "Sex", MilfCategory.SEX.getLabel(),
                        "Teacher", MilfCategory.TEACHER.getLabel(),
                        "Big Tits", MilfCategory.BIGTITS.getLabel(),
                        "Cuni", MilfCategory.CUNI.getLabel(),
                        "Назад", "test");
            }

            // Blowjob
            if (callbackData.equals(MilfCategory.BLOWJOB.getLabel())) {
                sendImage(chatId, MilfCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.BLOWJOB.getLabel());
            }

            // Sex
            if (callbackData.equals(MilfCategory.SEX.getLabel())) {
                sendImage(chatId, MilfCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.SEX.getLabel());
            }

            // Teacher
            if (callbackData.equals(MilfCategory.TEACHER.getLabel())) {
                sendImage(chatId, MilfCategory.TEACHER.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.TEACHER.getLabel());
            }

            // BigTits
            if (callbackData.equals(MilfCategory.BIGTITS.getLabel())) {
                sendImage(chatId, MilfCategory.BIGTITS.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.BIGTITS.getLabel());
            }

            // Cuni
            if (callbackData.equals(MilfCategory.CUNI.getLabel())) {
                sendImage(chatId, MilfCategory.CUNI.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.CUNI.getLabel());
            }

            /* Loli */
            if (callbackData.equals(Type.NSFW.getLabel() + TestCategory.LOLI.getLabel())) {
                sendCallback(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                        "Blowjob", LoliCategory.BLOWJOB.getLabel(),
                        "Sex", LoliCategory.SEX.getLabel(),
                        "Sholicon", LoliCategory.SHOLICON.getLabel(),
                        "Solo", LoliCategory.SOLO.getLabel( ),
                        "Yuri", LoliCategory.YURI.getLabel(),
                        "Назад", "test");
            }

            // Blowjob
            if (callbackData.equals(LoliCategory.BLOWJOB.getLabel())) {
                sendImage(chatId, LoliCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.BLOWJOB.getLabel());
            }

            // Sex
            if (callbackData.equals(LoliCategory.SEX.getLabel())) {
                sendImage(chatId, LoliCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.SEX.getLabel());
            }

            // Sholicon
            if (callbackData.equals(LoliCategory.SHOLICON.getLabel())) {
                sendImage(chatId, LoliCategory.SHOLICON.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.SHOLICON.getLabel());
            }

            // Solo
            if (callbackData.equals(LoliCategory.SOLO.getLabel())) {
                sendImage(chatId, LoliCategory.SOLO.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.SOLO.getLabel());
            }

            // Yuri
            if (callbackData.equals(LoliCategory.YURI.getLabel())) {
                sendImage(chatId, LoliCategory.YURI.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.YURI.getLabel());
            }

        }
    }

    private void sendCallback(long messageId,
                              long chatId,
                              String message,
                              String firstButtonText,
                              String firstButtonCallback,
                              String secondButtonText,
                              String secondButtonCallback,
                              String thirdButtonText,
                              String thirdButtonCallback,
                              String fourButtonText,
                              String fourButtonCallback,
                              String fiveButtonText,
                              String fiveButtonCallback) {
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

        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendCallback(long messageId,
                              long chatId,
                              String message,
                              String firstButtonText,
                              String firstButtonCallback,
                              String secondButtonText,
                              String secondButtonCallback,
                              String thirdButtonText,
                              String thirdButtonCallback,
                              String fourButtonText,
                              String fourButtonCallback,
                              String fiveButtonText,
                              String fiveButtonCallback,
                              String sixButtonText,
                              String sixButtonCallback
    ) {
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

        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
                sendMessage(chatId, "Размер архива не должен превышать 20 мб");
                log.error("saveFileFromMessage", e);
            }

            if (fileTelegram != null) {
                try {
                    File file = downloadFile(fileTelegram);
                    String path = archivePath + "/" + update.getMessage().getDocument().getFileName();
                    Files.copy(file, new File(path));
                    archiveService.unzip(path, imagesPath);
                    sendMessage(chatId, "Архив " + update.getMessage().getDocument().getFileName() + " Успешно загружен.");
                } catch (TelegramApiException | IOException e) {
                    sendMessage(chatId, "Ошибка при загрузке файла");
                    log.error("saveFileFromMessage", e);
                }
            }
        } else if(update.getMessage().getDocument().getFileName().contains(".rar") |
                update.getMessage().getDocument().getFileName().contains(".tar") |
                update.getMessage().getDocument().getFileName().contains(".7z")
        ) {
            long chatId = update.getMessage().getChatId();
            sendMessage(chatId, "Отправьте zip архив \uD83D\uDC12");
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

        InlineKeyboardButton button = new InlineKeyboardButton();

        rowInline.add(button);
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        button.setText(buttonName);
        button.setCallbackData(callbackData);
    }

    // Set button for SendPhoto
    public void setButtons(SendPhoto message,
                           String firstButtonText,
                           String secondButtonText,
                           String firstButtonCallback,
                           String secondButtonCallback) {
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

    // Set button for SendPhoto
    public void setButtons(SendPhoto message,
                           String firstButtonText,
                           String secondButtonText,
                           String thirdButtonText,
                           String firstButtonCallback,
                           String secondButtonCallback,
                           String thirdButtonCallback) {
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
    private void sendImage(long chatId, String category, String buttonName, String callbackData) throws IOException {
        SendPhoto message = new SendPhoto(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        String image = fileService.getImage(category);
        message.setPhoto(new InputFile(new File(image)));

        log.info("\nFilename: " + image + "\nCategory: " + category);

        try {
            setButtons(message, buttonName, callbackData);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Send image
    private void sendImage(long chatId,
                           String category,
                           String firstButtonName,
                           String secondButtonName,
                           String firstCallbackData,
                           String secondCallbackData) throws IOException {
        SendPhoto message = new SendPhoto(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        String image = fileService.getImage(category);
        message.setPhoto(new InputFile(new File(image)));

        log.info("\nFilename: " + image + "\nCategory: " + category);
        log.info("callback: " + firstCallbackData + " " + secondCallbackData);

        try {
            setButtons(message, firstButtonName, secondButtonName, firstCallbackData, secondCallbackData);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendImage(long chatId,
                           String category,
                           String firstButtonName,
                           String secondButtonName,
                           String thirdButtonName,
                           String firstCallbackData,
                           String secondCallbackData,
                           String thirdButtonCallbackData) throws IOException {
        SendPhoto message = new SendPhoto(); // Create a message object object
        message.setChatId(String.valueOf(chatId));
        String image = fileService.getImage(category);
        message.setPhoto(new InputFile(new File(image)));

        log.info("\nFilename: " + image + "\nCategory: " + category);
        log.info("callback: " + firstCallbackData + " " + secondCallbackData);

        try {
            setButtons(message, firstButtonName, secondButtonName, thirdButtonName, firstCallbackData, secondCallbackData, thirdButtonCallbackData);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendImageWithButton(long chatId, String buttonName, String callbackData) throws IOException {
        sendMessage(chatId, "Извините! Данная функция в разработке :3");
        SendPhoto message = new SendPhoto();
        message.setChatId(String.valueOf(chatId));
        String image = fileService.getImage("dev");
        message.setPhoto(new InputFile(new File(image)));

        try {
            setButtons(message, buttonName, callbackData);
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
