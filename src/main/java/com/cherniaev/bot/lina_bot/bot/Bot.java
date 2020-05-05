package com.cherniaev.bot.lina_bot.bot;

import com.cherniaev.bot.lina_bot.flow.MsgHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    private MsgHandler msgHandler;

    public Bot() {
        super();
        msgHandler = new MsgHandler();
    }

    public void onUpdateReceived(Update update) {
        Message getMsg = update.getMessage();
        Integer id = getMsg.getFrom().getId();
        try {
            execute(msgHandler.handleMsg(getMsg, id));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "@alisha_sytnychuk_bot";
    }

    public String getBotToken() {
        return "1223136582:AAGfLUCNbT6d8rPhoFCQvAR8ezKfr3Zin-8";
    }
}
