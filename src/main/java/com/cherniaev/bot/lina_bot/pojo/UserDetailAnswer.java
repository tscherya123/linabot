package com.cherniaev.bot.lina_bot.pojo;

import com.cherniaev.bot.lina_bot.flow.Experience;
import com.cherniaev.bot.lina_bot.flow.Level;
import com.cherniaev.bot.lina_bot.flow.MsgState;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "userDetailAnswer")
public class UserDetailAnswer {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "experience")
    private Experience experience;
    @Column(name = "trainingTimes")
    private Integer trainingTimes;
    @Column(name = "isCountingCalories")
    private boolean isCountingCalories;
    @Column(name = "isMeditating")
    private boolean isMeditating;
    @Column(name = "level")
    private Level level;
    @Column(name = "state")
    private MsgState state;
}
