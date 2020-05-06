package com.cherniaev.bot.linabot.flow;

import com.cherniaev.bot.linabot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.cherniaev.bot.linabot.flow.FlowUtils.*;
import static com.cherniaev.bot.linabot.flow.PHRASES.*;
import static com.cherniaev.bot.linabot.flow.Utils.createReplyKeyboardMarkup;
import static com.cherniaev.bot.linabot.flow.Utils.isEmailValid;

public enum BotState {

    START {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, HELLO_MSG);
        }

        @Override
        public BotState nextState() {
            return REQUEST_CONTACT;
        }
    },
    REQUEST_CONTACT {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup(true, true, false);

            ArrayList<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow keyboardRow1 = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("Поделится контактом >").setRequestContact(true);
            keyboardRow1.add(keyboardButton);
            keyboard.add(keyboardRow1);
            replyKeyboardMarkup.setKeyboard(keyboard);

            context.setReplyKeyboard(replyKeyboardMarkup);

            sendMessage(context, CONTACT_REQUEST_MSG);
        }

        @Override
        public void handleInput(BotContext context) {
            Contact contact = context.getUpdate().getMessage().getContact();
            if (contact != null && !contact.getPhoneNumber().isEmpty() && contact.getUserID().equals(context.getUser().getId())) {
                context.getUser().setPhone(contact.getPhoneNumber());
                context.getUser().setFirstName(contact.getFirstName());
                context.getUser().setLastName(contact.getLastName());
                next = REQUEST_EMAIL;
            } else {
                next = REQUEST_CONTACT;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    REQUEST_EMAIL {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
            replyKeyboardRemove.setSelective(false);
            context.setReplyKeyboard(replyKeyboardRemove);
            sendMessage(context, EMAIL_REQUEST_MSG);
        }

        @Override
        public void handleInput(BotContext context) {

            if (isEmailValid(context.getInput())) {
                context.getUser().setEmail(context.getInput());
                next = SEND_PRESENT;
            } else {
                next = REQUEST_EMAIL;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    SEND_PRESENT(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, PRESENT_MSG);
        }

        @Override
        public BotState nextState() {
            return REQUEST_TO_START_POOL;
        }
    },
    REQUEST_TO_START_POOL {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.setReplyKeyboard(createReplyKeyboardMarkup(REQUEST_TO_START_POOL_ANSWERS.getAnswers()));
            sendMessage(context, REQUEST_TO_START_POOL_MSG);
        }

        @Override
        public void handleInput(BotContext context) {
            if (!context.getInput().equals("Да")) {
                next = REQUEST_TO_START_POOL;
            } else {
                next = TIMES;
            }
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    TIMES {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.setReplyKeyboard(createReplyKeyboardMarkup(TIMES_ANSWERS.getAnswers()));
            sendMessage(context, TIMES_MSG);
        }

        @Override
        public void handleInput(BotContext context) {
            List<String> answers = TIMES_ANSWERS.getAnswers();
            if (!answers.contains(context.getInput())) {
                next = TIMES;
                return;
            }
            context.getUser().setTrainingTimes(answers.indexOf(context.getInput()) + 1);
            next = EXPERIENCE;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    EXPERIENCE {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.setReplyKeyboard(createReplyKeyboardMarkup(EXPERIENCE_ANSWERS.getAnswers()));
            sendMessage(context, EXPERIENCE_MSG);
        }

        @Override
        public void handleInput(BotContext context) {
            List<String> answers = EXPERIENCE_ANSWERS.getAnswers();
            if (!answers.contains(context.getInput())) {
                next = EXPERIENCE;
                return;
            }
            context.getUser().setExperience(answers.indexOf(context.getInput()) + 1);
            next = CALORIES;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    CALORIES {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.setReplyKeyboard(createReplyKeyboardMarkup(CALORIES_ANSWERS.getAnswers()));
            sendMessage(context, CALORIES_MSG);
        }

        @Override
        public void handleInput(BotContext context) {
            List<String> answers = CALORIES_ANSWERS.getAnswers();
            if (!answers.contains(context.getInput())) {
                next = CALORIES;
                return;
            }
            context.getUser().setCountingCalories((answers.indexOf(context.getInput())) == 0);
            next = MEDITATION;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    MEDITATION {
        private BotState next;

        @Override
        public void enter(BotContext context) {
            context.setReplyKeyboard(createReplyKeyboardMarkup(MEDITATION_ANSWERS.getAnswers()));
            sendMessage(context, MEDITATION_MSG);
        }

        @Override
        public void handleInput(BotContext context) {
            List<String> answers = MEDITATION_ANSWERS.getAnswers();
            if (!answers.contains(context.getInput())) {
                next = MEDITATION;
                return;
            }
            context.getUser().setMeditating((answers.indexOf(context.getInput())) == 0);
            next = SEND_RESULT;
        }

        @Override
        public BotState nextState() {
            return next;
        }
    },
    SEND_RESULT(false) {
        @Override
        public void enter(BotContext context) {
            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
            replyKeyboardRemove.setSelective(false);
            context.setReplyKeyboard(replyKeyboardRemove);

            if (context.getUser().getLevel() == null) {
                User user = context.getUser();
                boolean isExpert = user.getExperience().equals(3)
                    && user.getTrainingTimes().equals(3)
                    && user.isCountingCalories()
                    && user.isMeditating();

                boolean isNovice = user.getExperience().equals(1)
                    && user.getTrainingTimes().equals(1)
                    && !user.isCountingCalories()
                    && !user.isMeditating();
                user.setLevel(isExpert ? 3 : isNovice ? 1 : 2);
            }

            switch (context.getUser().getLevel()) {

                case 1:
                    sendMessage(context, BEGINNER_MSG);
                    break;
                case 2:
                    sendMessage(context, ADVANCED_MSG);
                    break;
                case 3:
                    sendMessage(context, EXPERT_MSG);
                    break;
            }
        }

        @Override
        public BotState nextState() {
            return WAITING;
        }
    },
    WAITING {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "На этом пока всё, спасибо!");
        }

        @Override
        public BotState nextState() {
            return WAITING;
        }
    };

    private static BotState[] states;
    private final boolean isInputNeeded;
    private static final Logger logger = LoggerFactory.getLogger(BotState.class);


    BotState() {
        isInputNeeded = true;
    }

    BotState(boolean isInputNeeded) {
        this.isInputNeeded = isInputNeeded;
    }

    public static BotState getInitialState() {
        return byId(0);
    }

    public static BotState byId(int id) {
        if (states == null) {
            states = BotState.values();
        }
        return states[id];
    }

    protected void sendMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
            .setChatId(context.getUser().getChatId())
            .setText(text);
        message.setReplyMarkup(context.getReplyKeyboard());
        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error while sending the message", e);
        }
    }

    public boolean isInputNeeded() {
        return isInputNeeded;
    }

    public void handleInput(BotContext context) {
        //impl in childes
    }

    public abstract void enter(BotContext context);
    public abstract BotState nextState();
}
