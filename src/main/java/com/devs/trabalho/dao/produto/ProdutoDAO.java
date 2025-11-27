package com.devs.trabalho.dao.produto;

import com.devs.trabalho.dao.BaseDAO;
import com.devs.trabalho.dao.HibernateUtil;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.model.produtos.Produto;
import com.devs.trabalho.model.usuario.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProdutoDAO extends BaseDAO<Produto> {

    public ProdutoDAO() {
        super(Produto.class);
    }

    /**
     * Método para deletar produto.
     * Busca o objeto no banco antes de deletar para evitar erro de "Detached Instance".
     */
    public void deleteById(Long id) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Busca o produto para deletar o estoque caso exista
            // necessário buscar para não causar erro de Detached Instance
            Produto produtoBanco = session.get(Produto.class, id);

            if (produtoBanco != null) {
                if (produtoBanco.getEstoque() != null) {
                    produtoBanco.getEstoque().getProdutos().remove(produtoBanco);
                }

                session.delete(produtoBanco);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e; // Lança o Erro de volta para a service tratar
        } finally {
            if (session != null) session.close();
        }
    }

    public Produto findById(Long id, Usuario usuarioLogado) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Produto p WHERE p.id = :id AND p.estoque.usuarioDono = :usuarioLogado";
            return session.createQuery(hql, classe)
                    .setParameter("id", id)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Produto> findAll(Usuario usuarioLogado) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Produto p WHERE p.estoque.usuarioDono = :usuarioLogado";
            return session.createQuery(hql, classe)
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if(session != null) session.close();
        }
    }

    public List<Produto> findAllByEstoque(Estoque estoque, Usuario usuarioLogado) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            String hql = "FROM Produto p WHERE p.estoque.id = :estoqueId AND p.estoque.usuarioDono = :usuarioLogado";
            return session.createQuery(hql, Produto.class)
                    .setParameter("estoqueId", estoque.getId())
                    .setParameter("usuarioLogado", usuarioLogado)
                    .list();
        } finally {
            if(session != null) session.close();
        }
    }
}