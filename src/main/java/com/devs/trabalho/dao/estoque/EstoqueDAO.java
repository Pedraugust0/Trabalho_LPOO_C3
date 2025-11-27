package com.devs.trabalho.dao.estoque;

import com.devs.trabalho.dao.BaseDAO;
import com.devs.trabalho.dao.HibernateUtil;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.model.usuario.Usuario;
import org.hibernate.Session;

import java.util.List;

public class EstoqueDAO extends BaseDAO<Estoque> {

    public EstoqueDAO() {
        super(Estoque.class);
    }

    public Estoque findById(Long id, Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM Estoque e WHERE e.id = :id AND e.usuarioDono = :usuarioLogado", classe)
                    .setParameter("id", id)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Estoque> findAll(Usuario usuarioLogado) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            return session.createQuery("FROM Estoque e WHERE e.usuarioDono = :usuarioLogado", classe)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if(session != null) session.close();
        }
    }

    public Estoque findByIdWithProdutos(Long id, Usuario usuarioLogado) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Estoque e LEFT JOIN FETCH e.produtos WHERE e.id = :id AND e.usuarioDono = :usuarioLogado", Estoque.class)
                    .setParameter("id", id)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Estoque> findAllWithProdutos(Usuario usuarioLogado) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            // O "DISTINCT" faz com que o JOIN não cause o produto cartesiano
            // Ex (sem o distinct):
            /*
            Estoque Produto
            1         1
            1         2
            1         3
            */
            // Ex (com o distinct)
            /*
            Estoque Produto1 Produto2 Produto3 Produto4
            1          1        2         3      4
             */
            String hql = "SELECT DISTINCT e FROM Estoque e LEFT JOIN FETCH e.produtos WHERE e.usuarioDono = :usuarioLogado";

            return session.createQuery(hql, Estoque.class)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }
}