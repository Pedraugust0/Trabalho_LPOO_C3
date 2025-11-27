package com.devs.trabalho.dao; // Ou onde você preferir

import com.devs.trabalho.exceptions.SistemaException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Cria um sessionFactory
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Falha ao inicializar a SessionFactory do Hibernate: " + ex);
            throw new ExceptionInInitializerError("Erro ao comunicar com o banco de dados");
        }
    }

    /**
     * Retorna um sessionFactory (Usa o buildSessionFactory para criar um session factory)
     * Somente é criado um sessionFactory,
     * caso um já exista e o método seja chamado novamente,
     * ele retorna o atributo de classe na linha 9.
     * */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = buildSessionFactory();
            } catch (Exception e) {
                throw new SistemaException("Erro ao iniciar Hibernate", e);
            }
        }
        return sessionFactory;
    }
}