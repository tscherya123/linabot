package com.cherniaev.bot.linabot.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public enum BotState {

    START {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Hello");
        }

        @Override
        public BotState nextState() {
            return REQUEST_CONTACT;
        }
    },
    REQUEST_CONTACT {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Pls share contact");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setPhone(context.getInput());
        }

        @Override
        public BotState nextState() {
            return REQUEST_EMAIL;
        }
    },
    REQUEST_EMAIL {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Pls share email");
        }

        @Override
        public void handleInput(BotContext context) {
            context.getUser().setEmail(context.getInput());
        }

        @Override
        public BotState nextState() {
            return SEND_PRESENT;
        }
    },
    SEND_PRESENT(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Your present!");
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
            sendMessage(context, "Do you wanna pool?");
        }

        @Override
        public void handleInput(BotContext context) {
            if (!context.getInput().equals("yes")) {
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
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "How much?");
        }

        @Override
        public BotState nextState() {
            return EXPERIENCE;
        }
    },
    EXPERIENCE {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "How long?");
        }

        @Override
        public BotState nextState() {
            return CALORIES;
        }
    },
    CALORIES {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Count calories?");
        }

        @Override
        public BotState nextState() {
            return MEDITATION;
        }
    },
    MEDITATION {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Meditation?");
        }

        @Override
        public BotState nextState() {
            return SEND_RESULT;
        }
    },
    SEND_RESULT(false) {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "Your result!");
        }

        @Override
        public BotState nextState() {
            return WAITING;
        }
    },
    WAITING {
        @Override
        public void enter(BotContext context) {
            sendMessage(context, "See you!");
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
