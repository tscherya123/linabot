package com.cherniaev.bot.lina_bot.bot;


import com.cherniaev.bot.lina_bot.dao.api.UserDao;
import com.cherniaev.bot.lina_bot.dao.impl.HibernateUserDaoImpl;
import com.cherniaev.bot.lina_bot.pojo.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    private UserDao userDao;

    public Bot() {
        super();
        userDao = new HibernateUserDaoImpl();
    }

    // com.cherniaev.bot.lina_bot.bot get message
    public void onUpdateReceived(Update update) {
        Message getMsg = update.getMessage();
        getMsg.getFrom();
        Integer id = getMsg.getFrom().getId();
        User user = new User();

        try {
            user = userDao.findById(id);
            try {
                execute(handleUserMessage(getMsg, user));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } catch (RuntimeException e) {
            user.setId(getMsg.getFrom().getId());
            user.setUsername(getMsg.getFrom().getUserName());
            user.setLastName(getMsg.getFrom().getLastName());
            user.setFirstName(getMsg.getFrom().getFirstName());

            userDao.create(user);
            try {
                execute(handleFirstUserMessage(getMsg, user));
            } catch (TelegramApiException ex) {
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return "@alisha_sytnychuk_bot";
    }

    public String getBotToken() {
        return "1223136582:AAGfLUCNbT6d8rPhoFCQvAR8ezKfr3Zin-8";
    }

    private SendMessage handleFirstUserMessage(Message getMsg, User user) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(getMsg.getChatId());

        sendMsg.setText("Приятно познакомится, " + user.getFirstName() + " " + user.getLastName()
        + "! Меня зовут Алина. Напиши пожалуйста что-то мне ещё. неважно что :)");

        return sendMsg;
    }

    private SendMessage handleUserMessage(Message getMsg, User user) {

        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(getMsg.getChatId());

        String userInfo = "Твой id: " + user.getId()
            + "\nТвое имя: " + user.getFirstName()
            + "\nТвоя фамилия: " + user.getLastName()
            + "\nТвой юзернейм: " + user.getUsername()
            ;

        sendMsg.setText("Круто! А вот что я знаю про тебя!\n" + userInfo);

        return sendMsg;
    }
}
