package com.cherniaev.bot.lina_bot.dao.impl;

import com.cherniaev.bot.lina_bot.dao.api.Dao;
import com.cherniaev.bot.lina_bot.dao.api.UserDao;
import com.cherniaev.bot.lina_bot.pojo.User;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import javax.persistence.TypedQuery;
import java.util.List;

@NoArgsConstructor
public class HibernateUserDaoImpl extends Dao implements UserDao {

    @Override
    public void create(User user) {
        super.create(user);
    }

    @Override
    public void update(User user) {
        super.update(user);
    }

    @Override
    public void remove(User user) {
        super.remove(user);
    }

    @Override
    public List<User> findAll() {
        return super.findAll();
    }

    @Override
    public User findById(Integer id) {
        if (id == null) {
            throw new NullPointerException();
        }
        Session session = null;
        User result = null;
        try {
            session = super.getCurrentSession();
            TypedQuery<User> typedQuery = session
                .createQuery("FROM User user where user.id = :id");
            typedQuery.setParameter("id", id);
            result = typedQuery.getSingleResult();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }

    @Override
    public User findByUsername(String username) {
        if (username == null) {
            throw new NullPointerException();
        }
        Session session = null;
        User result = null;
        try {
            session = super.getCurrentSession();
            TypedQuery<User> typedQuery = session
                .createQuery("FROM User user where user.username = :username");
            typedQuery.setParameter("username", username);
            result = typedQuery.getSingleResult();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return result;
    }
}
