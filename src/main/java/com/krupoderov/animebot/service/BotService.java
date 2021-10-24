package com.krupoderov.animebot.service;

import com.krupoderov.animebot.config.BotConfig;
import com.krupoderov.animebot.enumeration.Category;
import com.krupoderov.animebot.enumeration.Type;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
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

    public BotService(BotConfig botConfig, ParseService parseService, FileService fileService) {
        this.botConfig = botConfig;
        this.parseService = parseService;
        this.fileService = fileService;
    }

    //Accepts and processes messages received in private messages or in the channel where the bot administrator is located
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().getText().equals("/start") ||
                    update.getMessage().getText().equals("start") ||
                    update.getMessage().getText().equals("Хочу картинку")
            ) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(chatId);
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
                keyboard.setText("Keyboard");
                keyboard.setCallbackData("keyboard");
                rowInline.add(sfw);
                rowInline.add(nsfw);
                rowInline.add(keyboard);
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

        } else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("keyboard")) {
                log.info("keyboard");
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

                replyKeyboardMarkup.setSelective(true); // показывать клавиатуру определенным пользователям
                replyKeyboardMarkup.setResizeKeyboard(true); // "подгонка" клавиатуры
                replyKeyboardMarkup.setOneTimeKeyboard(false); // не скрывать клавиатуру

                List<KeyboardRow> keyboardRowList = new ArrayList<>();
//        List<KeyboardRow> keyboardRowList2 = new ArrayList<>();
                KeyboardRow keyboardFirstRow = new KeyboardRow(); // Строка клавиатуры (1-я)
//        KeyboardRow keyboardSecondRow = new KeyboardRow(); // Строка клавиатуры (2-я)

                keyboardFirstRow.add(new KeyboardButton("/start"));

                keyboardRowList.add(keyboardFirstRow); // добавить кнопку в список
                replyKeyboardMarkup.setKeyboard(keyboardRowList); // добавить лист в массив кнопок
                message.setReplyMarkup(replyKeyboardMarkup); // связать сообщения с клавиатурой

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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

            if (call_data.equals("nsfw_waifu")) {
                log.info("Категория: Waifu");
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("nsfw", "waifu").getUrl();
                message.setText("[Вайфу](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton waifu = new InlineKeyboardButton();

                waifu.setText("Хочу еще");
                waifu.setCallbackData("nsfw_waifu");

                rowInline.add(waifu);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("nsfw_neko")) {
                log.info("Категория: Neko");
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("nsfw", "neko").getUrl();
                message.setText("[Кошкодевочка](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton neko = new InlineKeyboardButton();

                neko.setText("Хочу еще");
                neko.setCallbackData("nsfw_neko");

                rowInline.add(neko);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

//            if (call_data.equals("nsfw_trap")) {
//                SendMessage message = new SendMessage(); // Create a message object object
//                message.setChatId(String.valueOf(chatId));
//                message.setParseMode(ParseMode.MARKDOWN);
//                String url = parseService.getImage("nsfw", "trap").getUrl();
//                message.setText("[Трапик](" + url + ")");
//
//                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
//                List<InlineKeyboardButton> rowInline = new ArrayList<>();
//
//                InlineKeyboardButton trap = new InlineKeyboardButton();
//
//                trap.setText("Хочу еще");
//                trap.setCallbackData("nsfw_trap");
//
//                rowInline.add(trap);
//
//                // Set the keyboard to the markup
//                rowsInline.add(rowInline);
//                // Add it to the message
//                markupInline.setKeyboard(rowsInline);
//                message.setReplyMarkup(markupInline);
//                try {
//                    execute(message);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }

            /* Blowjob */
            if (call_data.equals("nsfw_blowjob")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("nsfw", "blowjob").getUrl();
                message.setText("[Минет](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton blowjob = new InlineKeyboardButton();

                blowjob.setText("Хочу еще");
                blowjob.setCallbackData("nsfw_blowjob");

                rowInline.add(blowjob);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            /* Traps */
            if (call_data.equals("nsfw_trap")) {
                String category = Category.TRAP.getLabel();
                SendPhoto message = new SendPhoto(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                String image = fileService.getImage(category);
                message.setPhoto(new InputFile(new File(image)));

                log.info("\nFilename: " + image + "\nCategory: " + category);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton trap = new InlineKeyboardButton();

                trap.setText("Хочу еще");
                trap.setCallbackData("nsfw_trap");

                rowInline.add(trap);
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            /* Anal */
            if (call_data.equals(Type.NSFW.getLabel() + Category.ANAL.getLabel())) {
                String category = Category.ANAL.getLabel();
                SendPhoto message = new SendPhoto();
                message.setChatId(String.valueOf(chatId));
                String image = fileService.getImage(category);
                message.setPhoto(new InputFile(new File(image)));

                log.info("\nFilename: " + image + "\nCategory: " + category);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton anal = new InlineKeyboardButton();

                anal.setText("Хочу еще");
                anal.setCallbackData(Type.NSFW.getLabel() + Category.ANAL.getLabel());

                rowInline.add(anal);
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            /* Double penetration */
            if (call_data.equals(Type.NSFW.getLabel() + Category.DP.getLabel())) {
                String category = Category.DP.getLabel();
                SendPhoto message = new SendPhoto();
                message.setChatId(String.valueOf(chatId));
                String image = fileService.getImage(category);
                message.setPhoto(new InputFile(new File(image)));

                log.info("\nFilename: " + image + "\nCategory: " + category);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton dp = new InlineKeyboardButton();

                dp.setText("Хочу еще");
                dp.setCallbackData(Type.NSFW.getLabel() + Category.DP.getLabel());

                rowInline.add(dp);
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            /* Solo female */
            if (call_data.equals(Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel())) {
                String category = Category.SOLO_FEMALE.getLabel();
                SendPhoto message = new SendPhoto();
                message.setChatId(String.valueOf(chatId));
                String image = fileService.getImage(category);
                message.setPhoto(new InputFile(new File(image)));

                log.info("\nFilename: " + image + "\nCategory: " + category);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sf = new InlineKeyboardButton();

                sf.setText("Хочу еще");
                sf.setCallbackData(Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel());

                rowInline.add(sf);
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            /* Vaginal */
            if (call_data.equals(Type.NSFW.getLabel() + Category.VAGINAL.getLabel())) {
                String category = Category.VAGINAL.getLabel();
                SendPhoto message = new SendPhoto();
                message.setChatId(String.valueOf(chatId));
                String image = fileService.getImage(category);
                message.setPhoto(new InputFile(new File(image)));

                log.info("\nFilename: " + image + "\nCategory: " + category);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton vaginal = new InlineKeyboardButton();

                vaginal.setText("Хочу еще");
                vaginal.setCallbackData(Type.NSFW.getLabel() + Category.VAGINAL.getLabel());

                rowInline.add(vaginal);
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("sfw_waifu")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("sfw", "waifu").getUrl();
                message.setText("[Одетая вайфу](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sfw_waifu = new InlineKeyboardButton();

                sfw_waifu.setText("Хочу еще");
                sfw_waifu.setCallbackData("sfw_waifu");

                rowInline.add(sfw_waifu);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("sfw_neko")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("sfw", "neko").getUrl();
                message.setText("[Одетая кошкодевочка](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sfw_neko = new InlineKeyboardButton();

                sfw_neko.setText("Хочу еще");
                sfw_neko.setCallbackData("sfw_neko");

                rowInline.add(sfw_neko);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("sfw_shinobu")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("sfw", "shinobu").getUrl();
                message.setText("[Синобутян](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sfw_shinobu = new InlineKeyboardButton();

                sfw_shinobu.setText("Хочу еще");
                sfw_shinobu.setCallbackData("sfw_shinobu");

                rowInline.add(sfw_shinobu);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("sfw_megumin")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("sfw", "megumin").getUrl();
                message.setText("[Мегумин](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sfw_megumin = new InlineKeyboardButton();

                sfw_megumin.setText("Хочу еще");
                sfw_megumin.setCallbackData("sfw_megumin");

                rowInline.add(sfw_megumin);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("sfw_bully")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("sfw", "bully").getUrl();
                message.setText("[Буллинг](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sfw_bully = new InlineKeyboardButton();

                sfw_bully.setText("Хочу еще");
                sfw_bully.setCallbackData("sfw_bully");

                rowInline.add(sfw_bully);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("sfw_cuddle")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("sfw", "cuddle").getUrl();
                message.setText("[:3](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sfw_cuddle = new InlineKeyboardButton();

                sfw_cuddle.setText("Хочу еще");
                sfw_cuddle.setCallbackData("sfw_cuddle");

                rowInline.add(sfw_cuddle);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("sfw_cry")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("sfw", "cry").getUrl();
                message.setText("[:(](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sfw_cry = new InlineKeyboardButton();

                sfw_cry.setText("Хочу еще");
                sfw_cry.setCallbackData("sfw_cry");

                rowInline.add(sfw_cry);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }

            if (call_data.equals("sfw_hug")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("sfw", "hug").getUrl();
                message.setText("[Обнимашки:3](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton sfw_hug = new InlineKeyboardButton();

                sfw_hug.setText("Хочу еще");
                sfw_hug.setCallbackData("sfw_hug");

                rowInline.add(sfw_hug);

                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
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
