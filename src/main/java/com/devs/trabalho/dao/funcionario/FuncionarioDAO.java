package com.devs.trabalho.dao.funcionario;

import com.devs.trabalho.dao.BaseDAO;
import com.devs.trabalho.dao.HibernateUtil;
import com.devs.trabalho.model.funcionario.Funcionario;
import com.devs.trabalho.model.usuario.Usuario;
import org.hibernate.Session;

import java.util.List;

public class FuncionarioDAO extends BaseDAO<Funcionario> {

    public FuncionarioDAO() {
        super(Funcionario.class);
    }

    public Funcionario findById(Long id, Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM Funcionario f WHERE f.id = :id AND f.usuarioDono = :usuarioLogado", classe)
                    .setParameter("id", id)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Funcionario> findAll(Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM Funcionario f WHERE f.usuarioDono = :usuarioLogado", classe)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if(session != null) session.close();
        }
    }

    public Funcionario findByIdWithContatos(Long id, Usuario usuarioLogado) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Funcionario f LEFT JOIN FETCH f.contatos WHERE f.id = :id AND f.usuarioDono = :usuarioLogado", Funcionario.class)
                    .setParameter("id", id)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Funcionario> findAllWithContatos(Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("SELECT DISTINCT f FROM Funcionario f LEFT JOIN FETCH f.contatos WHERE f.usuarioDono = :usuarioLogado", Funcionario.class)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if(session != null) session.close();
        }
    }
}