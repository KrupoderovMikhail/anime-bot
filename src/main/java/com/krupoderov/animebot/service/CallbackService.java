package com.krupoderov.animebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.IOException;

public interface CallbackService {

    public SendMessage getMessage(String callback, long chatId);

    public EditMessageText getEditMessage(String callback, long chatId, int messageId);

    public SendPhoto getPhoto(String callback, long chatId) throws IOException;
}
