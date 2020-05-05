package com.cherniaev.bot.lina_bot.flow;

import com.cherniaev.bot.lina_bot.dao.api.UserDao;
import com.cherniaev.bot.lina_bot.dao.api.UserDetailAnswerDao;
import com.cherniaev.bot.lina_bot.dao.impl.HibernateUserDaoImpl;
import com.cherniaev.bot.lina_bot.dao.impl.HibernateUserDetailAnswerDaoImpl;
import com.cherniaev.bot.lina_bot.pojo.User;
import com.cherniaev.bot.lina_bot.pojo.UserDetailAnswer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

public class MsgHandler {
    private UserDao userDao;
    private UserDetailAnswerDao userDetailAnswerDao;

    public MsgHandler() {
        userDao = new HibernateUserDaoImpl();
        userDetailAnswerDao = new HibernateUserDetailAnswerDaoImpl();
    }

    public SendMessage handleMsg(Message msg, Integer id) {
        MsgState state;
        try {
            userDao.findById(id);
            UserDetailAnswer detail = userDetailAnswerDao.findById(id);
            state = detail.getState();

        } catch (RuntimeException e) {
            state = MsgState.HELLO;
        }

        switch (state) {
            case HELLO:
                return handleHelloMsg(msg, id);
            case EXPERIENCE:
                return handleExperienceMsg(msg, id);
            case TIMES:
                return handleTimesMsg(msg, id);
            case CALORIES:
                return handleCaloriesMsg(msg, id);
            case MEDITATION:
                return handleMeditationMsg(msg, id);
            case RESULT:
                return handleResultMsg(msg, id);
        }
        return null;
    }

    private SendMessage handleHelloMsg(Message msg, Integer id) {
        User newUser = new User();
        newUser.setId(msg.getFrom().getId());
        newUser.setUsername(msg.getFrom().getUserName());
        newUser.setLastName(msg.getFrom().getLastName());
        newUser.setFirstName(msg.getFrom().getFirstName());

        userDao.create(newUser);

        UserDetailAnswer userDetailAnswer = new UserDetailAnswer();
        userDetailAnswer.setId(id);
        userDetailAnswer.setState(MsgState.EXPERIENCE);
        userDetailAnswerDao.create(userDetailAnswer);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboardRow1.add(Experience.SIX_MONTH.toString());
        keyboardRow2.add(Experience.YEAR.toString());
        keyboardRow3.add(Experience.MORE.toString());
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        keyboard.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(keyboard);

        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        sendMsg.setText("Приятно познакомится, " + newUser.getFirstName()
                + "! Меня зовут Алина. Выбери пожалуйста, какой у тебя опыт тренировок.");

        sendMsg.setReplyMarkup(replyKeyboardMarkup);

        return sendMsg;
    }

    private SendMessage handleExperienceMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        if (msg.getText().equals(Experience.SIX_MONTH.toString())
                || msg.getText().equals(Experience.YEAR.toString())
                || msg.getText().equals(Experience.MORE.toString())) {
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setState(MsgState.TIMES);

            userDetailAnswer.setExperience(msg.getText().equals(Experience.SIX_MONTH.toString())
                    ? Experience.SIX_MONTH
                    : msg.getText().equals(Experience.YEAR.toString())
                        ? Experience.YEAR
                        : Experience.MORE);
            userDetailAnswerDao.update(userDetailAnswer);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            ArrayList<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();
            KeyboardRow keyboardRow3 = new KeyboardRow();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            keyboardRow1.add("1");
            keyboardRow2.add("3");
            keyboardRow3.add("5");
            keyboard.add(keyboardRow1);
            keyboard.add(keyboardRow2);
            keyboard.add(keyboardRow3);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMsg.setReplyMarkup(replyKeyboardMarkup);

            sendMsg.setText("Выбери пожалуйста сколько раз в неделю тренируешься?");
        } else {
            sendMsg.setText("Некорректный ответ. Выбери пожалуйста, какой у тебя опыт тренировок.");
        }
        return sendMsg;
    }
    private SendMessage handleTimesMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());

        if (msg.getText().equals("1")
        || msg.getText().equals("3")
        || msg.getText().equals("5")) {
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setTrainingTimes(Integer.parseInt(msg.getText()));
            userDetailAnswer.setState(MsgState.CALORIES);
            userDetailAnswerDao.update(userDetailAnswer);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            ArrayList<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            keyboardRow1.add("Да");
            keyboardRow2.add("Нет");
            keyboard.add(keyboardRow1);
            keyboard.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMsg.setReplyMarkup(replyKeyboardMarkup);

            sendMsg.setText("Считаешь калории?");
        } else {
            sendMsg.setText("Некорректный ответ. Выбери пожалуйста сколько раз в неделю тренируешься?");
        }
        return sendMsg;
    }
    private SendMessage handleCaloriesMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());

        if (msg.getText().equals("Да")
                || msg.getText().equals("Нет")) {
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setCountingCalories(msg.getText().equals("Да"));
            userDetailAnswer.setState(MsgState.MEDITATION);
            userDetailAnswerDao.update(userDetailAnswer);

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            ArrayList<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardRow keyboardRow2 = new KeyboardRow();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            keyboardRow1.add("Да");
            keyboardRow2.add("Нет");
            keyboard.add(keyboardRow1);
            keyboard.add(keyboardRow2);
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMsg.setReplyMarkup(replyKeyboardMarkup);

            sendMsg.setText("Ты медетируешь?");
        } else {
            sendMsg.setText("Некорректный ответ. Считаешь калории?");
        }
        return sendMsg;
    }
    private SendMessage handleMeditationMsg(Message msg, Integer id) {

        if (msg.getText().equals("Да")
                || msg.getText().equals("Нет")) {
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setMeditating(msg.getText().equals("Да"));
            userDetailAnswer.setState(MsgState.RESULT);
            userDetailAnswerDao.update(userDetailAnswer);
            return handleResultMsg(msg, id);
        } else {
            SendMessage sendMsg = new SendMessage();
            sendMsg.setChatId(msg.getChatId());
            sendMsg.setText("Некорректный ответ. Ты медетируешь?");
            return sendMsg;
        }
    }
    private SendMessage handleResultMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());

        UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
        if (userDetailAnswer.getLevel() == null) {
            boolean isExpert = userDetailAnswer.getExperience().equals(Experience.MORE)
                    && userDetailAnswer.getTrainingTimes().equals(5)
                    && userDetailAnswer.isCountingCalories()
                    && userDetailAnswer.isMeditating();

            boolean isNovice = userDetailAnswer.getExperience().equals(Experience.SIX_MONTH)
                    && userDetailAnswer.getTrainingTimes().equals(1)
                    && !userDetailAnswer.isCountingCalories()
                    && !userDetailAnswer.isMeditating();

            userDetailAnswer.setLevel(isExpert ? Level.EXPERT : isNovice ? Level.BEGINNER : Level.ADVANCED);
            userDetailAnswerDao.update(userDetailAnswer);
        }
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setSelective(false);
        sendMsg.setReplyMarkup(replyKeyboardRemove);

        User user = userDao.findById(id);
        UserDetailAnswer userDetailAnswer2 = userDetailAnswerDao.findById(id);

        sendMsg.setText("Твой результат: " + userDetailAnswer.getLevel()
                + "\nПереходи в мой паблик по ссылке: https://t.me/Be_your_own_heroine"
                + "\n\n\nИнфа из базы данных о тебе: "
                + "\n USER: \n\n" + user.toString() + "\n\nUSER_DETAIL_ANSWER: \n\n"
                + userDetailAnswer2.toString() + "\n\n Спасибо за тестирование бота!");

        return sendMsg;
    }
}
