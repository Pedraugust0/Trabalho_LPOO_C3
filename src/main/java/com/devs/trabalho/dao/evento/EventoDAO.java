package com.devs.trabalho.dao.evento;

import com.devs.trabalho.dao.BaseDAO;
import com.devs.trabalho.dao.HibernateUtil;
import com.devs.trabalho.model.eventos.Evento;
import com.devs.trabalho.model.usuario.Usuario;
import org.hibernate.Session;

import java.util.List;

public class EventoDAO extends BaseDAO<Evento> {

    public EventoDAO() {
        super(Evento.class);
    }

    public Evento findById(Long id, Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM Evento e WHERE e.id = :id AND e.usuarioDono = :usuarioLogado", classe)
                    .setParameter("id", id)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Evento> findAll(Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM Evento e WHERE e.usuarioDono = :usuarioLogado", classe)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if(session != null) session.close();
        }
    }
}