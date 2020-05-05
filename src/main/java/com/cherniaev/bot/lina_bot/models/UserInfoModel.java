package com.cherniaev.bot.lina_bot.models;

import com.cherniaev.bot.lina_bot.flow.Experience;
import com.cherniaev.bot.lina_bot.flow.Level;
import com.cherniaev.bot.lina_bot.flow.MsgState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfoModel {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Experience experience;
    private Integer trainingTimes;
    private boolean isCountingCalories;
    private boolean isMeditating;
    private Level level;
    private MsgState state;

    @Override
    public String toString() {
        return "id: " + id +
            "\nИмя: " + firstName +
            "\nФамилия: " + lastName +
            "\nСсылка на тг: @" + username +
            "\nТел: " + phone +
            "\nПочта: " + email +
            "\nОтветы:\n" +
            "Опыт: " + experience +
            "\nКоличество тренировок в неделю: " + trainingTimes +
            "\nСчитает калории: " + (isCountingCalories ? "да" : "нет") +
            "\nМедитирует: " + (isMeditating ? "да" : "нет") +
            "\nУровень: " +
            (level == null  ? "Неопределен" :
                    level.equals(Level.BEGINNER) ? "Начинающий" :
                        level.equals(Level.EXPERT) ? "Профи" :
                            level.equals(Level.ADVANCED) ? "Продвинутый" : "");
    }
}
