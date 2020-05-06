package com.cherniaev.bot.linabot.flow;

import org.apache.commons.validator.EmailValidator;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean isEmailValid(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(boolean isSelective,
        boolean isResizeKeyboard, boolean isOneTimeKeyboard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(isSelective);
        replyKeyboardMarkup.setResizeKeyboard(isResizeKeyboard);
        replyKeyboardMarkup.setOneTimeKeyboard(isOneTimeKeyboard);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup createReplyKeyboardMarkup(List<String> answers) {
        ReplyKeyboardMarkup result = createReplyKeyboardMarkup(true, true, false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        answers.forEach(answer -> {
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(answer);
            keyboard.add(keyboardRow);
        });
        result.setKeyboard(keyboard);
        return result;
    }
}
