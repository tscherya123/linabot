package com.cherniaev.bot.linabot.flow;

import com.cherniaev.bot.linabot.components.LinaBot;
import com.cherniaev.bot.linabot.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@AllArgsConstructor
@Getter
@Setter
public class BotContext {
    private final LinaBot bot;
    private final User user;
    private final String input;
    private final Update update;
    private ReplyKeyboard replyKeyboard;

    public BotContext(LinaBot bot, User user, String input, Update update) {
        this.bot = bot;
        this.user = user;
        this.input = input;
        this.update = update;
    }

    public static BotContext of(LinaBot bot, User user, String text, Update update) {
        return new BotContext(bot, user, text, update);
    }
}
