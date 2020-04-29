package com.cherniaev.bot.lina_bot.dao.api;

import com.cherniaev.bot.lina_bot.pojo.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;

public class Dao {
    private final SessionFactory sessionFactory;

    public Dao() {
        try {
            Configuration config = new Configuration()
                .configure("hibernate.cfg.xml");
            sessionFactory = config.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public void create(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        Session session = null;
        try {
            session = getCurrentSession();
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
                throw new RuntimeException(ex);
            }
        } finally {
            session.close();
        }
    }

    public void update(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        Session session = null;
        try {
            session = getCurrentSession();
            session.beginTransaction();
            session.update(object);
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
                throw new RuntimeException(ex);
            }
        } finally {
            session.close();
        }
    }

    public void remove(Object object) {
        if (object == null) {
            throw new NullPointerException();
        }
        Session session = null;
        try {
            session = getCurrentSession();
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
        } catch (Exception ex) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
                throw new RuntimeException(ex);
            }
        } finally {
            session.close();
        }
    }

    public List<User> findAll() {
        Session session = null;
        List<User> result = null;
        try {
            session = getCurrentSession();
            Query<User> query = session.createQuery("FROM User");
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

    public Session getCurrentSession() throws HibernateException {
        return sessionFactory.openSession();
    }
}
