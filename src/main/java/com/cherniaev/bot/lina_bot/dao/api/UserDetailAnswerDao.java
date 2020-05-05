package com.cherniaev.bot.lina_bot.dao.api;

import com.cherniaev.bot.lina_bot.pojo.UserDetailAnswer;

import java.util.List;

public interface UserDetailAnswerDao {
    void create(UserDetailAnswer userDetailAnswer);

    void update(UserDetailAnswer userDetailAnswer);

    void remove(UserDetailAnswer userDetailAnswer);

    List<UserDetailAnswer> findAll();

    UserDetailAnswer findById(Integer id);
}
