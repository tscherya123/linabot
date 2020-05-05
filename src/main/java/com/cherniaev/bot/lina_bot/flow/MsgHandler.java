package com.cherniaev.bot.lina_bot.flow;

import com.cherniaev.bot.lina_bot.dao.api.UserDao;
import com.cherniaev.bot.lina_bot.dao.api.UserDetailAnswerDao;
import com.cherniaev.bot.lina_bot.dao.impl.HibernateUserDaoImpl;
import com.cherniaev.bot.lina_bot.dao.impl.HibernateUserDetailAnswerDaoImpl;
import com.cherniaev.bot.lina_bot.models.UserInfoModel;
import com.cherniaev.bot.lina_bot.pojo.User;
import com.cherniaev.bot.lina_bot.pojo.UserDetailAnswer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cherniaev.bot.lina_bot.flow.PHRASES.ADVANCED_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.BEGINNER_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.CALORIES_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.CONTACT_WRONG_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.EMAIL_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.EMAIL_WRONG_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.EXPERIENCE_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.EXPERT_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.HELLO_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.MEDITATION_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.OFFER_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.TIMES_MSG;
import static com.cherniaev.bot.lina_bot.flow.PHRASES.WRONG_MSG;

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
            case EMAIL:
                return handleEmailMsg(msg, id);
            case OFFER:
                return handleOfferMsg(msg, id);
            case CONTACT:
                return handleContactMsg(msg, id);
            case TIMES:
                return handleTimesMsg(msg, id);
            case EXPERIENCE:
                return handleExperienceMsg(msg, id);
            case CALORIES:
                return handleCaloriesMsg(msg, id);
            case MEDITATION:
                return handleMeditationMsg(msg, id);
            case RESULT:
                return handleResultMsg(msg, id);
        }
        return null;
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(boolean isSelective,
        boolean isResizeKeyboard, boolean isOneTimeKeyboard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(isSelective);
        replyKeyboardMarkup.setResizeKeyboard(isResizeKeyboard);
        replyKeyboardMarkup.setOneTimeKeyboard(isOneTimeKeyboard);
        return replyKeyboardMarkup;
    }

    private void askContact(SendMessage sendMsg) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(true, true, false);

        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Поделится контактом >").setRequestContact(true);
        keyboardRow1.add(keyboardButton);
        keyboard.add(keyboardRow1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMsg.setReplyMarkup(replyKeyboardMarkup);
        sendMsg.setText(HELLO_MSG);
    }

    private void askEmail(SendMessage sendMsg) {
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setSelective(false);
        sendMsg.setReplyMarkup(replyKeyboardRemove);
        sendMsg.setText(EMAIL_MSG);
    }

    private void askOffer(SendMessage sendMsg) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(true, true, false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow1.add("Да");
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMsg.setReplyMarkup(replyKeyboardMarkup);

        sendMsg.setText(OFFER_MSG);
    }

    private void askExperience(SendMessage sendMsg) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(true, true, false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow1.add(Experience.SIX_MONTH.toString());
        keyboardRow2.add(Experience.YEAR.toString());
        keyboardRow3.add(Experience.MORE.toString());
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        keyboard.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMsg.setReplyMarkup(replyKeyboardMarkup);
        sendMsg.setText(EXPERIENCE_MSG);
    }

    private void askTimes(SendMessage sendMsg) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(true, true, false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow1.add("1");
        keyboardRow2.add("3");
        keyboardRow3.add("5");
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        keyboard.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMsg.setReplyMarkup(replyKeyboardMarkup);

        sendMsg.setText(TIMES_MSG);
    }

    private void askCalories(SendMessage sendMsg) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(true, true, false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow1.add("Да");
        keyboardRow2.add("Нет");
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMsg.setReplyMarkup(replyKeyboardMarkup);

        sendMsg.setText(CALORIES_MSG);
    }

    private void askMeditation(SendMessage sendMsg) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(true, true, false);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow1.add("Да");
        keyboardRow2.add("Нет");
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMsg.setReplyMarkup(replyKeyboardMarkup);

        sendMsg.setText(MEDITATION_MSG);
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
        userDetailAnswer.setState(MsgState.CONTACT);
        userDetailAnswerDao.create(userDetailAnswer);

        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        askContact(sendMsg);
        return sendMsg;
    }



    private SendMessage handleContactMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        if (!msg.getContact().getPhoneNumber().isEmpty() && msg.getContact().getUserID().equals(id)) {
            Contact contact = msg.getContact();
            User user = userDao.findById(id);
            user.setFirstName(contact.getFirstName());
            user.setLastName(contact.getLastName());
            user.setPhone(contact.getPhoneNumber());
            userDao.update(user);
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setState(MsgState.EMAIL);
            userDetailAnswerDao.update(userDetailAnswer);

            askEmail(sendMsg);
        } else {
            sendMsg.setText(CONTACT_WRONG_MSG);
        }
        return sendMsg;
    }

    private boolean isEmail(String email) {
        if (email.length() > 254) {
            return false;
        }
        Pattern ptr = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = ptr.matcher(email);
        return matcher.find();
    }

    private SendMessage handleEmailMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        if (isEmail(msg.getText())) {
            User user = userDao.findById(id);
            user.setEmail(msg.getText());
            userDao.update(user);
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setState(MsgState.OFFER);
            userDetailAnswerDao.update(userDetailAnswer);

            askOffer(sendMsg);
        } else {
            sendMsg.setText(EMAIL_WRONG_MSG);
        }
        return sendMsg;
    }

    private SendMessage handleOfferMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        if (msg.getText().equals("Да")) {
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setState(MsgState.TIMES);
            userDetailAnswerDao.update(userDetailAnswer);

            askTimes(sendMsg);
        } else {
            sendMsg.setText(WRONG_MSG);
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
            userDetailAnswer.setState(MsgState.EXPERIENCE);
            userDetailAnswerDao.update(userDetailAnswer);
            askExperience(sendMsg);
        } else {
            sendMsg.setText(WRONG_MSG + "\n\n" + TIMES_MSG);
        }
        return sendMsg;
    }

    private SendMessage handleExperienceMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        if (msg.getText().equals(Experience.SIX_MONTH.toString())
            || msg.getText().equals(Experience.YEAR.toString())
            || msg.getText().equals(Experience.MORE.toString())) {
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setState(MsgState.CALORIES);

            userDetailAnswer.setExperience(msg.getText().equals(Experience.SIX_MONTH.toString())
                ? Experience.SIX_MONTH
                : msg.getText().equals(Experience.YEAR.toString())
                    ? Experience.YEAR
                    : Experience.MORE);
            userDetailAnswerDao.update(userDetailAnswer);
            askCalories(sendMsg);
        } else {
            sendMsg.setText(WRONG_MSG + "\n\n" + EXPERIENCE_MSG);
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

            askMeditation(sendMsg);
        } else {
            sendMsg.setText(WRONG_MSG + "\n\n" + CALORIES_MSG);
        }
        return sendMsg;
    }
    private SendMessage handleMeditationMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        if (msg.getText().equals("Да")
                || msg.getText().equals("Нет")) {
            UserDetailAnswer userDetailAnswer = userDetailAnswerDao.findById(id);
            userDetailAnswer.setMeditating(msg.getText().equals("Да"));
            userDetailAnswer.setState(MsgState.RESULT);
            userDetailAnswerDao.update(userDetailAnswer);
            sendMsg = handleResultMsg(msg, id);
        } else {
            sendMsg.setText(WRONG_MSG + "\n\n" + MEDITATION_MSG);
        }
        return sendMsg;
    }

    private SendMessage handleResultMsg(Message msg, Integer id) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(msg.getChatId());
        String username = userDao.findById(id).getUsername();

        if (msg.getText().equals("/удалилоха")
            && (username.equals("Anton_sher")
            || username.equals("slim_fetty"))) {
            User anton_sher = userDao.findByUsername("Anton_sher");
            userDao.remove(anton_sher);
            userDetailAnswerDao.remove(userDetailAnswerDao.findById(anton_sher.getId()));
        } else if (msg.getText().equals("/allresults")
            && (username.equals("alisha_sytnychuk")
                || username.equals("slim_fetty")
                || username.equals("Anton_sher"))) {
            List<User> users = userDao.findAll();
            List<UserInfoModel> infoModels = new ArrayList<UserInfoModel>();
            users.forEach(user -> {
                UserDetailAnswer userDetail = userDetailAnswerDao.findById(user.getId());
                UserInfoModel model = new UserInfoModel(user.getId(), user.getUsername(),
                    user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail(),
                    userDetail.getExperience(), userDetail.getTrainingTimes(), userDetail.isCountingCalories(),
                    userDetail.isMeditating(), userDetail.getLevel(), userDetail.getState());
                infoModels.add(model);
            });
            StringBuilder builder = new StringBuilder();
            builder.append("-------------\n");
            infoModels.forEach(info -> builder.append("Участник ").append(info.toString()).append("\n").append("-------------\n"));

            sendMsg.setText(builder.toString());
        } else {
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

            switch (userDetailAnswer.getLevel()) {

                case BEGINNER:
                    sendMsg.setText(BEGINNER_MSG);
                    break;
                case ADVANCED:
                    sendMsg.setText(ADVANCED_MSG);
                    break;
                case EXPERT:
                    sendMsg.setText(EXPERT_MSG);
                    break;
            }
        }


//        sendMsg.setText(sendMsg.getText() + "\n\n\n" + userDao.findById(id) + "\n\n\n" + userDetailAnswerDao.findById(id));
        return sendMsg;
    }
}
