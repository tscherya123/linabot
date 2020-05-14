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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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

        if (text != null && checkIfAdminCommand(user, text)) {
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

            context = BotContext.of(this, newUser, text, update);
            state.enter(context);
            logger.info("NEW USER :" + newUser.getId());
        } else {
            context = BotContext.of(this, user, text, update);
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
        if (user != null && user.getUsername() != null && user.getUsername().equals("slim_fetty")) {

        } else if (user == null || !user.isAdmin()) {
            return false;
        }

        if (text.equals("/all")) {
            logger.info("Admin call getAllUsers");
            listUsers(user);
            return true;
        } else if (text.equals("/all_small")) {
            logger.info("Admin call getAllUsers");
            listUsersSmall(user);
            return true;
        } else if (text.startsWith("/broadcast ")) {
            logger.info("Admin call broadcast");

            text = text.substring("/broadcast ".length());
            broadcast(text);
            return true;
        } else if (text.startsWith("/addadmin ")) {
            logger.info("Admin call addadmin");

            text = text.substring("/addadmin ".length());
            addadmin(user, text);
            return true;
        } else if (text.startsWith("/removeadmin ")) {
            logger.info("Admin call removeadmin");

            text = text.substring("/removeadmin ".length());
            removeadmin(user, text);
            return true;
        } else if (text.startsWith("/admins")) {
            logger.info("Admin call admins");
            admins(user);
            return true;
        } else if (text.startsWith("/userinfo")) {
            logger.info("Admin call userinfo");
            try {
                text = text.substring("/userinfo ".length());
            } catch (StringIndexOutOfBoundsException e) {
                sendMessage(user.getChatId(), "Пожалуйста укажите айди через пробел");
            }
            userinfo(user, text);
            return true;
        } else if (text.startsWith("/help")) {
            logger.info("Admin call removeadmin");
            help(user);
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
        List<User> users = userService.findAll();
        for (int i = 0; i < users.size() / 10 + 1; i += 10) {
            StringBuilder sb = new StringBuilder();
            int lastIndexUser = i + 10;
            if (lastIndexUser > users.size()) {
                lastIndexUser = users.size() - 1;
            }
            sb.append("--- Лист № ").append(i/10 + 1).append(" ---\n");
            users.subList(i, lastIndexUser).forEach(user -> {
                sb.append("Участник ").append(user.toString()).append("\n").append("-------------\n");
            });
            sendMessage(admin.getChatId(), sb.toString());
        }
    }

    private void listUsersSmall(User admin) {
        List<User> users = userService.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("-------------\n");
        sb.append("Количество людей, запустивших бота: ").append(users.size()).append("\n");
        sb.append("--ИМЯ---ТЕЛ--").append(users.size()).append("\n");
        users.forEach(user -> {
            sb.append("-------------\n");
            sb.append(user.getFirstName()).append(" ").append(user.getPhone());
        });
        sendMessage(admin.getChatId(), sb.toString());
    }

    private void broadcast(String text) {
        List<User> users = userService.findAll();
        users.forEach(user -> sendMessage(user.getChatId(), text));
    }

    private void addadmin(User admin, String text) {
        AtomicBoolean isFounded = new AtomicBoolean(false);
        userService.findAll().forEach(user -> {
            if (user.getUsername() != null && user.getUsername().equals(text)) {
                user.setAdmin(true);
                userService.save(user);
                isFounded.set(true);
                return;
            }
        });
        if (!isFounded.get()) {
            sendMessage(admin.getChatId(), "Пользователь не общался с ботом.");
        } else {
            sendMessage(admin.getChatId(), "Пользователь удачно стал админом.");
        }

    }

    private void removeadmin(User admin, String text) {
        AtomicBoolean isFounded = new AtomicBoolean(false);
        userService.findAll().forEach(user -> {
            if (user.getUsername().equals(text)) {
                user.setAdmin(false);
                userService.save(user);
                isFounded.set(true);
                return;
            }
        });
        if (!isFounded.get()) {
            sendMessage(admin.getChatId(), "Пользователь не общался с ботом.");
        } else {
            sendMessage(admin.getChatId(), "Пользователь удачно перестал быть админом.");
        }
    }

    private void admins(User admin) {
        StringBuilder sb = new StringBuilder();
        List<User> admins = userService.findAll().stream().filter(user -> user.isAdmin()).collect(Collectors.toList());
        sb.append("-------------\n");
        admins.forEach(admin1 -> {
            sb.append("Админ ").append(admin1.toString()).append("\n").append("-------------\n");
        });
        sendMessage(admin.getChatId(), sb.toString());
    }

    private void userinfo(User admin, String text) {
        try {
            Integer id = Integer.valueOf(text);
            User user = userService.findUserById(id);
            if (user == null) {
                throw new IllegalArgumentException();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("-------------\n");
            sb.append("Пользователь ").append(user.toString()).append("\n").append("-------------\n");
            sendMessage(admin.getChatId(), sb.toString());
        } catch (RuntimeException e) {
            sendMessage(admin.getChatId(), "Id неверный");
        }
    }

    private void help(User admin) {
        String helpMsg = "Комманды для админов: \n"
            + "/all - список участников\n"
            + "/all_small - краткий список запустивших бота. Имя + Телефон"
            + "/userinfo user_id - информация о пользователе по его id\n"
            + "/broadcast text - отправит text (или что угодно другое) всем участникам\n"
            + "/addadmin nickname - сделает пользователя с ником nickname админом\n"
            + "/removeadmin nickname - удалит пользователя с ником nickname из админов\n"
            + "/admins - список админов\n"
            + "/help - покажет это сообщение\n\n\n"
            + "Создатель бота: @slim_fetty";

        sendMessage(admin.getChatId(), helpMsg);
    }
}
