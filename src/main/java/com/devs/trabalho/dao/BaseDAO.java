package com.devs.trabalho.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.List;

public abstract class BaseDAO<T> {

    protected final Class<T> classe;

    public BaseDAO(Class<T> classe) {
        this.classe = classe;
    }

    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.save(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.delete(session.merge(entity));

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    public T findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.get(this.classe, id);
        } finally {
            if(session != null) session.close();
        }
    }

    public List<T> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM " + this.classe.getName(), classe).list();
        } finally {
            if(session != null) session.close();
        }
    }
}