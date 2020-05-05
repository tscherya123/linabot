package com.cherniaev.bot.lina_bot.dao.api;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

    public Session getCurrentSession() throws HibernateException {
        return sessionFactory.openSession();
    }
}
