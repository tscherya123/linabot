package com.cherniaev.bot.linabot.flow;

import com.cherniaev.bot.linabot.components.LinaBot;
import com.cherniaev.bot.linabot.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BotContext {
    private final LinaBot bot;
    private final User user;
    private final String input;

    public static BotContext of(LinaBot bot, User user, String text) {
        return new BotContext(bot, user, text);
    }
}
