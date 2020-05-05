package com.cherniaev.bot.lina_bot.app;

import com.cherniaev.bot.lina_bot.bot.Bot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegram = new TelegramBotsApi();
        Bot bot = new Bot();
        try {
            telegram.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            LOGGER.log(Level.SEVERE, "Exception occur", e);
        }
    }
}
