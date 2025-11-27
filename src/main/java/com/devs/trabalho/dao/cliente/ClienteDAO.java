package com.devs.trabalho.dao.cliente;

import com.devs.trabalho.dao.BaseDAO;
import com.devs.trabalho.dao.HibernateUtil;
import com.devs.trabalho.model.cliente.Cliente;
import com.devs.trabalho.model.usuario.Usuario;
import org.hibernate.Session;

import java.util.List;

public class ClienteDAO extends BaseDAO<Cliente> {

    public ClienteDAO() {
        super(Cliente.class);
    }

    public Cliente findById(Long id, Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM Cliente c WHERE c.id = :id AND c.usuarioDono = :usuarioLogado", classe)
                    .setParameter("id", id)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Cliente> findAll(Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM Cliente c WHERE c.usuarioDono = :usuarioLogado", classe)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if(session != null) session.close();
        }
    }

    public Cliente findByIdWithContatos(Long id, Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Cliente c LEFT JOIN FETCH c.contatos WHERE c.id = :id and c.usuarioDono = :usuarioLogado", Cliente.class)
                    .setParameter("id", id)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Cliente> findAllWithContatos(Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.contatos WHERE c.usuarioDono = :usuarioLogado", Cliente.class)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if(session != null) session.close();
        }
    }
}