package com.cherniaev.bot.linabot.services;

import com.cherniaev.bot.linabot.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User findUserById(Integer id);
    List<User> findAll();
    void save(User user);
    void delete(Integer id);
}
