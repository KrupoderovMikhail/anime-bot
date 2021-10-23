package com.krupoderov.animebot.service;

import com.krupoderov.animebot.config.BotConfig;
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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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

    public BotService(BotConfig botConfig, ParseService parseService) {
        this.botConfig = botConfig;
        this.parseService = parseService;
    }

    //Accepts and processes messages received in private messages or in the channel where the bot administrator is located
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
//
//            SendMessage sendMessage = new SendMessage();
//            sendMessage.setChatId(chatId);
//            sendMessage.setText("Test");
//            // Create ReplyKeyboardMarkup object
//            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//            // Create the keyboard (list of keyboard rows)
//            List<KeyboardRow> keyboard = new ArrayList<>();
//            // Create a keyboard row
//            KeyboardRow row = new KeyboardRow();
//            // Set each button, you can also use KeyboardButton objects if you need something else than text
//            row.add("/start");
//
//            // Add the first row to the keyboard
//            keyboard.add(row);
////            // Create another keyboard row
////            row = new KeyboardRow();
////            // Set each button for the second line
////            row.add("Row 2 Button 1");
////            row.add("Row 2 Button 2");
////            row.add("Row 2 Button 3");
//            // Add the second row to the keyboard
////            keyboard.add(row);
//            // Set the keyboard to the markup
//            keyboardMarkup.setKeyboard(keyboard);
//            // Add it to the message
//            sendMessage.setReplyMarkup(keyboardMarkup);
//            try {
//                execute(sendMessage); // Sending our message object to user
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }

            /*****************************************************************/
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
                sfw.setText("SFW");
                sfw.setCallbackData("sfw");
                nsfw.setText("NSFW");
                nsfw.setCallbackData("nsfw");
                rowInline.add(sfw);
                rowInline.add(nsfw);
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
//            else if (update.getMessage().getText().equals("/hide")) {
//                SendMessage message = new SendMessage(); // Create a message object object
//                message.setChatId(chatId);
//                message.setText("Понял, убираю клавиатуру :)");
//                ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
//                keyboardRemove.setRemoveKeyboard(true);
//                keyboardRemove.setSelective(true);
//                message.setReplyMarkup(keyboardRemove);
//                try {
//                    execute(message); // Call method to send the photo
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
            /*****************************************************************/

        } else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("nsfw")) {
                String answer = "Выберите категорию";
                EditMessageText new_message = new EditMessageText();
                new_message.setChatId(chatId);
                new_message.setMessageId(toIntExact(message_id));
                new_message.setText(answer);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton waifu = new InlineKeyboardButton();
                InlineKeyboardButton neko = new InlineKeyboardButton();
                InlineKeyboardButton trap = new InlineKeyboardButton();
                InlineKeyboardButton blowjob = new InlineKeyboardButton();

                waifu.setText("Waifu");
                waifu.setCallbackData("nsfw_waifu");

                neko.setText("Neko");
                neko.setCallbackData("nsfw_neko");

                trap.setText("Trap");
                trap.setCallbackData("nsfw_trap");

                blowjob.setText("Blowjob");
                blowjob.setCallbackData("nsfw_blowjob");

                rowInline.add(waifu);
                rowInline.add(neko);
                rowInline.add(trap);
                rowInline.add(blowjob);
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
                String url = parseService.getImage("nsfw","neko").getUrl();
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
            if (call_data.equals("nsfw_trap")) {
                SendMessage message = new SendMessage(); // Create a message object object
                message.setChatId(String.valueOf(chatId));
                message.setParseMode(ParseMode.MARKDOWN);
                String url = parseService.getImage("nsfw", "trap").getUrl();
                message.setText("[Трапик](" + url + ")");

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton trap = new InlineKeyboardButton();

                trap.setText("Хочу еще");
                trap.setCallbackData("nsfw_trap");

                rowInline.add(trap);

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
                String url = parseService.getImage("sfw","neko").getUrl();
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
                String url = parseService.getImage("sfw","shinobu").getUrl();
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
                String url = parseService.getImage("sfw","megumin").getUrl();
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
                String url = parseService.getImage("sfw","bully").getUrl();
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
                String url = parseService.getImage("sfw","cuddle").getUrl();
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
                String url = parseService.getImage("sfw","cry").getUrl();
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
                String url = parseService.getImage("sfw","hug").getUrl();
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
