package com.cherniaev.bot.lina_bot.dao.impl;

import com.cherniaev.bot.lina_bot.dao.api.Dao;
import com.cherniaev.bot.lina_bot.dao.api.UserDetailAnswerDao;
import com.cherniaev.bot.lina_bot.pojo.UserDetailAnswer;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class HibernateUserDetailAnswerDaoImpl extends Dao implements UserDetailAnswerDao {
    @Override
    public void create(UserDetailAnswer userDetailAnswer) {
        super.create(userDetailAnswer);
    }

    @Override
    public void update(UserDetailAnswer userDetailAnswer) {
        super.update(userDetailAnswer);
    }

    @Override
    public void remove(UserDetailAnswer userDetailAnswer) {
        super.remove(userDetailAnswer);
    }

    @Override
    public List<UserDetailAnswer> findAll() {
        Session session = null;
        List<UserDetailAnswer> result = null;
        try {
            session = getCurrentSession();
            Query<UserDetailAnswer> query = session.createQuery("FROM UserDetailAnswer");
            result = query.getResultList();
        } catch (NoResultException ex) {
            throw ex;
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
    public UserDetailAnswer findById(Integer id) {
        if (id == null) {
            throw new NullPointerException();
        }
        Session session = null;
        UserDetailAnswer result = null;
        try {
            session = super.getCurrentSession();
            TypedQuery<UserDetailAnswer> typedQuery = session
                    .createQuery("FROM UserDetailAnswer userDetailAnswer where userDetailAnswer.id = :id");
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
}
