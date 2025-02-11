package com.cherniaev.bot.linabot.model;

import com.cherniaev.bot.linabot.flow.BotState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "chatId")
    private Long chatId;
    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "stateId")
    private Integer stateId;
    @Column(name = "isAdmin")
    private boolean isAdmin;
    @Column(name = "experience")
    private Integer experience;
    @Column(name = "trainingTimes")
    private Integer trainingTimes;
    @Column(name = "isCountingCalories")
    private boolean isCountingCalories;
    @Column(name = "isMeditating")
    private boolean isMeditating;
    @Column(name = "level")
    private Integer level;

    @Override
    public String toString() {
//        return "User{" +
//            "id=" + id +
//            ", chatId=" + chatId +
//            ", username='" + username + '\'' +
//            ", firstName='" + firstName + '\'' +
//            ", lastName='" + lastName + '\'' +
//            ", phone='" + phone + '\'' +
//            ", email='" + email + '\'' +
//            ", stateId=" + stateId +
//            ", isAdmin=" + isAdmin +
//            ", experience=" + experience +
//            ", trainingTimes=" + trainingTimes +
//            ", isCountingCalories=" + isCountingCalories +
//            ", isMeditating=" + isMeditating +
//            ", level=" + level +
//            '}';
//
        return "id: " + id +
            "\nИмя: " + firstName +
            "\nФамилия: " + lastName +
            "\nСсылка на тг: @" + username +
            "\nТел: " + phone +
            "\nПочта: " + email +
            "\nAdmin: " + (isAdmin ? "да" : "нет") +
            "\nОтветы:\n" +
            "Опыт: " + experience +
            "\nКоличество тренировок в неделю: " + trainingTimes +
            "\nСчитает калории: " + (isCountingCalories ? "да" : "нет") +
            "\nМедитирует: " + (isMeditating ? "да" : "нет") +
            "\nУровень: " +
            (level == null  ? "Неопределен" :
                level.equals(1) ? "Начинающий" :
                    level.equals(3) ? "Профи" :
                        level.equals(2) ? "Продвинутый" : "") +
            "\nЭтап=" + BotState.byId(stateId).name();
    }
}
