package com.cherniaev.bot.lina_bot.dao.api;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Dao {
    private final SessionFactory sessionFactory;
    private static final Logger LOGGER = Logger.getLogger(Dao.class.getName());

    public Dao() {
        try {
            Configuration config = new Configuration()
                .configure("hibernate.cfg.xml")
                .setProperty("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));
            sessionFactory = config.buildSessionFactory();
            LOGGER.log(Level.INFO, "JDBC_DATABASE_URL:" + System.getenv("JDBC_DATABASE_URL"));
        } catch (Throwable ex) {
            LOGGER.log(Level.WARNING, "JDBC_DATABASE_URL trouble" + System.getenv("JDBC_DATABASE_URL"), ex);
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

    public Session getCurrentSession() throws HibernateException {
        return sessionFactory.openSession();
    }
}
