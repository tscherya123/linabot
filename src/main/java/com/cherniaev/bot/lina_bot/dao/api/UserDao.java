package com.cherniaev.bot.lina_bot.dao.api;

import com.cherniaev.bot.lina_bot.pojo.User;

import java.util.List;

public interface UserDao {

    void create(User user);

    void update(User user);

    void remove(User user);

    List<User> findAll();

    User findById(Integer id);

    User findByUsername(String username);
}
