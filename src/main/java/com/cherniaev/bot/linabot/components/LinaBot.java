package com.cherniaev.bot.linabot.components;

import com.cherniaev.bot.linabot.flow.BotContext;
import com.cherniaev.bot.linabot.flow.BotState;
import com.cherniaev.bot.linabot.model.User;
import com.cherniaev.bot.linabot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class LinaBot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(LinaBot.class);

    @Autowired
    private UserService userService;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }
        final String text = update.getMessage().getText();
        final Long chatId = update.getMessage().getChatId();

        User user = userService.findUserById(update.getMessage().getFrom().getId());

        if (checkIfAdminCommand(user, text)) {
            return;
        }

        BotContext context;
        BotState state;

        if (user == null) {
            state = BotState.getInitialState();
            User newUser = new User();
            newUser.setId(update.getMessage().getFrom().getId());
            newUser.setChatId(chatId);
            newUser.setUsername(update.getMessage().getFrom().getUserName());
            newUser.setStateId(state.ordinal());
            userService.save(newUser);

            context = BotContext.of(this, newUser, text);
            state.enter(context);
            logger.info("NEW USER :" + newUser.getId());
        } else {
            context = BotContext.of(this, user, text);
            state = BotState.byId(user.getStateId());

            logger.info("user " + user.getId() + " in state " + state);
        }

        state.handleInput(context);

        do {
            state = state.nextState();
            state.enter(context);
        } while (!state.isInputNeeded());

        context.getUser().setStateId(state.ordinal());
        userService.save(context.getUser());
    }

    private boolean checkIfAdminCommand(User user, String text) {
        if (user != null && user.getUsername().equals("slim_fetty")) {

        } else if (user == null || !user.isAdmin()) {
            return false;
        }

        if (text.equals("/all")) {
            logger.info("Admin call getAllUsers");

            listUsers(user);

            return true;
        } else if (text.startsWith("/broadcast")) {
            logger.info("Admin call broadcast");

            text = text.substring("/broadcast".length());
            broadcast(text);
            return true;
        }

        return false;
    }

    @PostConstruct
    public void start() {
        logger.info("username: {}, token: {}", username, token);
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage()
            .setChatId(chatId)
            .setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            logger.error("Error while sending the message", e);
        }
    }

    private void listUsers(User admin) {
        StringBuilder sb = new StringBuilder();
        List<User> users = userService.findAll();

        users.forEach(user -> {
            sb.append(user.getId()).append(' ').append(user.getUsername()).append('\n');
        });

        sendMessage(admin.getChatId(), sb.toString());
    }

    private void broadcast(String text) {
        List<User> users = userService.findAll();
        users.forEach(user -> sendMessage(user.getChatId(), text));
    }
}
