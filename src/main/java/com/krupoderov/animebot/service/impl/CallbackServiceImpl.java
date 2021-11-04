package com.krupoderov.animebot.service.impl;

import com.krupoderov.animebot.enumeration.*;
import com.krupoderov.animebot.service.CallbackService;
import com.krupoderov.animebot.service.ImageService;
import com.krupoderov.animebot.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.IOException;

@Service
@Slf4j
public class CallbackServiceImpl implements CallbackService {

    private final MessageService messageService;
    private final ImageService imageService;

    public CallbackServiceImpl(MessageService messageService, ImageService imageService) {
        this.messageService = messageService;
        this.imageService = imageService;
    }

    @Override
    public SendMessage getMessage(String callback, long chatId) {
        SendMessage message = new SendMessage();
        if (callback.equals("menu")) {
            log.info("Menu");
            log.info("callback: " + callback);
            message = messageService.getStartMessage(chatId);
        }

        /* SFW */

        /* Waifu */
        if (callback.equals("sfw_waifu")) {
            message = messageService.getMessageByUrl(chatId, "waifu", "[Одетая вайфу](", "sfw_waifu");
        }

        /* Neko */
        if (callback.equals("sfw_neko")) {
            message = messageService.getMessageByUrl(chatId, "neko", "[Одетая кошкодевочка](", "sfw_neko");
        }

        /* Shinobu */
        if (callback.equals("sfw_shinobu")) {
            message = messageService.getMessageByUrl(chatId, "shinobu", "[Синобутян](", "sfw_shinobu");
        }

        /* Megumin */
        if (callback.equals("sfw_megumin")) {
            message = messageService.getMessageByUrl(chatId, "megumin", "[Мегумин](", "sfw_megumin");
        }

        /* Bully */
        if (callback.equals("sfw_bully")) {
            message = messageService.getMessageByUrl(chatId, "bully", "[Буллинг](", "sfw_bully");
        }

        /* Cuddle */
        if (callback.equals("sfw_cuddle")) {
            message = messageService.getMessageByUrl(chatId, "cuddle", "[:3](", "sfw_cuddle");
        }

        /* Cry */
        if (callback.equals("sfw_cry")) {
            message = messageService.getMessageByUrl(chatId, "cry", "[:(](", "sfw_cry");
        }

        /* Hug */
        if (callback.equals("sfw_hug")) {
            message = messageService.getMessageByUrl(chatId, "hug", "[Обнимашки:3](", "sfw_hug");
        }

        return message;
    }

    @Override
    public EditMessageText getEditMessage(String callback, long chatId, int messageId) {
        EditMessageText message = new EditMessageText();
        if (callback.equals("nsfw")) {
            message = messageService.getNsfwEditMessageByCallback(callback, chatId, messageId);
        }

        if (callback.equals("test")) {
            message = messageService.getEditMessage(messageId, chatId, "На кого посмотрим? \uD83D\uDC41 \uD83D\uDC41", "Girl", Type.NSFW.getLabel() + TestCategory.GIRL.getLabel(), "Trap", Type.NSFW.getLabel() + TestCategory.TRAP.getLabel(), "Monster", Type.NSFW.getLabel() + TestCategory.MONSTER.getLabel(), "Milf", Type.NSFW.getLabel() + TestCategory.MILF.getLabel(), "Loli", Type.NSFW.getLabel() + TestCategory.LOLI.getLabel());
        }

        if (callback.equals("sfw")) {
            message = messageService.getEditMessage(messageId, chatId, "Выберите категорию", "Waifu", "sfw_waifu", "Neko", "sfw_neko", "Shinobu", "sfw_shinobu", "Megumin", "sfw_megumin", "Next >", "sfw_next1");
        }

        if (callback.equals("sfw_next1")) {
            message = messageService.getEditMessage(messageId, chatId, "Выберите категорию", "Bully", "sfw_bully", "Cuddle", "sfw_cuddle", "Cry", "sfw_cry", "Hug", "sfw_hug", "Next >", "sfw_next2");
        }


        /* Loli */
        if (callback.equals(Type.NSFW.getLabel() + TestCategory.LOLI.getLabel())) {
            message = messageService.getEditMessage(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                    "Blowjob", LoliCategory.BLOWJOB.getLabel(),
                    "Sex", LoliCategory.SEX.getLabel(),
                    "Sholicon", LoliCategory.SHOLICON.getLabel(),
                    "Solo", LoliCategory.SOLO.getLabel(),
                    "Yuri", LoliCategory.YURI.getLabel(),
                    "Назад", "test");
        }

        /* Milf */
        if (callback.equals(Type.NSFW.getLabel() + TestCategory.MILF.getLabel())) {
            message = messageService.getEditMessage(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                    "Blowjob", MilfCategory.SEX.getLabel(),
                    "Sex", MilfCategory.SEX.getLabel(),
                    "Teacher", MilfCategory.TEACHER.getLabel(),
                    "Big Tits", MilfCategory.BIGTITS.getLabel(),
                    "Cuni", MilfCategory.CUNI.getLabel(),
                    "Назад", "test");
        }

        /* Monster */
        if (callback.equals(Type.NSFW.getLabel() + TestCategory.MONSTER.getLabel())) {
            message = messageService.getEditMessage(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                    "Blowjob", MonsterCategory.BLOWJOB.getLabel(),
                    "Sex", MonsterCategory.SEX.getLabel(),
                    "BDSM", MonsterCategory.HANDJOB.getLabel(),
                    "Handjob", MonsterCategory.HANDJOB.getLabel(),
                    "Ahegao", MonsterCategory.AHEGAO.getLabel(),
                    "Назад", "test");
        }

        /* Trap */
        if (callback.equals(Type.NSFW.getLabel() + TestCategory.TRAP.getLabel())) {
            message = messageService.getEditMessage(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                    "Sex", TrapCategory.SEX.getLabel(),
                    "BDSM", TrapCategory.BDSM.getLabel(),
                    "Blowjob", TrapCategory.BLOWJOB.getLabel(),
                    "School", TrapCategory.SCHOOL.getLabel(),
                    "Gang Bang", TrapCategory.GANGBANG.getLabel(),
                    "Назад", "test");
        }

        /* Girl */
        if (callback.equals(Type.NSFW.getLabel() + TestCategory.GIRL.getLabel())) {
            message = messageService.getEditMessage(messageId, chatId, "Какую категорию выберем? \uD83C\uDF1A",
                    "Sex", GirlCategory.SEX.getLabel(),
                    "Yuri", GirlCategory.YURI.getLabel(),
                    "Blowjob", GirlCategory.BLOWJOB.getLabel(),
                    "BDSM", GirlCategory.BDSM.getLabel(),
                    "School", GirlCategory.SCHOOL.getLabel(),
                    "Назад", "test");
        }

        return message;
    }

    @Override
    public SendPhoto getPhoto(String callback, long chatId) throws IOException {
        SendPhoto photo = new SendPhoto();
        /* NSFW */

        /* Traps */
        if (callback.equals(Type.NSFW.getLabel() + Category.TRAP.getLabel())) {
            photo = imageService.getPhoto(chatId, Category.TRAP.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.TRAP.getLabel(), Command.MENU.getLabel());
        }

        /* Anal */
        if (callback.equals(Type.NSFW.getLabel() + Category.ANAL.getLabel())) {
            photo = imageService.getPhoto(chatId, Category.ANAL.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.ANAL.getLabel(), Command.MENU.getLabel());
        }

        /* Double penetration */
        if (callback.equals(Type.NSFW.getLabel() + Category.DP.getLabel())) {
            photo = imageService.getPhoto(chatId, Category.DP.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.DP.getLabel(), Command.MENU.getLabel());
        }

        /* Solo female */
        if (callback.equals(Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel())) {
            photo = imageService.getPhoto(chatId, Category.SOLO_FEMALE.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.SOLO_FEMALE.getLabel(), Command.MENU.getLabel());
        }

        /* Vaginal */
        if (callback.equals(Type.NSFW.getLabel() + Category.VAGINAL.getLabel())) {
            photo = imageService.getPhoto(chatId, Category.VAGINAL.getLabel(), "Хочу еще", "Menu", Type.NSFW.getLabel() + Category.VAGINAL.getLabel(), Command.MENU.getLabel());
        }

        /* Girl */

        // Sex
        if (callback.equals(GirlCategory.SEX.getLabel())) {
            photo = imageService.getPhoto(chatId, GirlCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.SEX.getLabel());
        }

        // Yuri
        if (callback.equals(GirlCategory.YURI.getLabel())) {
            photo = imageService.getPhoto(chatId, GirlCategory.YURI.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.YURI.getLabel());
        }

        // Blowjob
        if (callback.equals(GirlCategory.BLOWJOB.getLabel())) {
            photo = imageService.getPhoto(chatId, GirlCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.BLOWJOB.getLabel());
        }

        // BDSM
        if (callback.equals(GirlCategory.BDSM.getLabel())) {
            photo = imageService.getPhoto(chatId, GirlCategory.BDSM.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.BDSM.getLabel());
        }

        // School
        if (callback.equals(GirlCategory.SCHOOL.getLabel())) {
            photo = imageService.getPhoto(chatId, GirlCategory.SCHOOL.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.GIRL.getLabel(), GirlCategory.SCHOOL.getLabel());
        }

        /* Trap */

        // Sex
        if (callback.equals(TrapCategory.SEX.getLabel())) {
            photo = imageService.getPhoto(chatId, TrapCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.SEX.getLabel());
        }

        // BDSM
        if (callback.equals(TrapCategory.BDSM.getLabel())) {
            photo = imageService.getPhoto(chatId, TrapCategory.BDSM.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.BDSM.getLabel());
        }

        // Blowjob
        if (callback.equals(TrapCategory.BLOWJOB.getLabel())) {
            photo = imageService.getPhoto(chatId, TrapCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.BLOWJOB.getLabel());
        }

        // School
        if (callback.equals(TrapCategory.SCHOOL.getLabel())) {
            photo = imageService.getPhoto(chatId, TrapCategory.SCHOOL.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.SCHOOL.getLabel());
        }

        // Gang Bang
        if (callback.equals(TrapCategory.GANGBANG.getLabel())) {
            photo = imageService.getPhoto(chatId, TrapCategory.GANGBANG.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.TRAP.getLabel(), TrapCategory.GANGBANG.getLabel());
        }

        /* Monster */

        // Blowjob
        if (callback.equals(MonsterCategory.BLOWJOB.getLabel())) {
            photo = imageService.getPhoto(chatId, MonsterCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.BLOWJOB.getLabel());
        }

        // Sex
        if (callback.equals(MonsterCategory.SEX.getLabel())) {
            photo = imageService.getPhoto(chatId, MonsterCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.SEX.getLabel());
        }

        // BDSM
        if (callback.equals(MonsterCategory.BDSM.getLabel())) {
            photo = imageService.getPhoto(chatId, MonsterCategory.BDSM.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.BDSM.getLabel());
        }

        // Handjob
        if (callback.equals(MonsterCategory.HANDJOB.getLabel())) {
            photo = imageService.getPhoto(chatId, MonsterCategory.HANDJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.HANDJOB.getLabel());
        }

        // Ahegao
        if (callback.equals(MonsterCategory.AHEGAO.getLabel())) {
            photo = imageService.getPhoto(chatId, MonsterCategory.AHEGAO.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MONSTER.getLabel(), MonsterCategory.AHEGAO.getLabel());
        }

        /* Milf */

        // Blowjob
        if (callback.equals(MilfCategory.BLOWJOB.getLabel())) {
            photo = imageService.getPhoto(chatId, MilfCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.BLOWJOB.getLabel());
        }

        // Sex
        if (callback.equals(MilfCategory.SEX.getLabel())) {
            photo = imageService.getPhoto(chatId, MilfCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.SEX.getLabel());
        }

        // Teacher
        if (callback.equals(MilfCategory.TEACHER.getLabel())) {
            photo = imageService.getPhoto(chatId, MilfCategory.TEACHER.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.TEACHER.getLabel());
        }

        // BigTits
        if (callback.equals(MilfCategory.BIGTITS.getLabel())) {
            photo = imageService.getPhoto(chatId, MilfCategory.BIGTITS.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.BIGTITS.getLabel());
        }

        // Cuni
        if (callback.equals(MilfCategory.CUNI.getLabel())) {
            photo = imageService.getPhoto(chatId, MilfCategory.CUNI.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.MILF.getLabel(), MilfCategory.CUNI.getLabel());
        }

        /* Loli */

        // Blowjob
        if (callback.equals(LoliCategory.BLOWJOB.getLabel())) {
            photo = imageService.getPhoto(chatId, LoliCategory.BLOWJOB.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.BLOWJOB.getLabel());
        }

        // Sex
        if (callback.equals(LoliCategory.SEX.getLabel())) {
            photo = imageService.getPhoto(chatId, LoliCategory.SEX.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.SEX.getLabel());
        }

        // Sholicon
        if (callback.equals(LoliCategory.SHOLICON.getLabel())) {
            photo = imageService.getPhoto(chatId, LoliCategory.SHOLICON.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.SHOLICON.getLabel());
        }

        // Solo
        if (callback.equals(LoliCategory.SOLO.getLabel())) {
            photo = imageService.getPhoto(chatId, LoliCategory.SOLO.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.SOLO.getLabel());
        }

        // Yuri
        if (callback.equals(LoliCategory.YURI.getLabel())) {
            photo = imageService.getPhoto(chatId, LoliCategory.YURI.getLabel(), "Меню", "Назад", "Хочу еще", Command.MENU.getLabel(), TestCategory.LOLI.getLabel(), LoliCategory.YURI.getLabel());
        }
        return photo;
    }
}
