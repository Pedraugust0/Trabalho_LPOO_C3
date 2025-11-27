package com.devs.trabalho.dao.usuario;

import com.devs.trabalho.dao.BaseDAO;
import com.devs.trabalho.dao.HibernateUtil;
import com.devs.trabalho.model.usuario.Usuario;
import org.hibernate.Session;

public class UsuarioDAO extends BaseDAO<Usuario> {

    public UsuarioDAO() {
        super(Usuario.class);
    }

    public Usuario findByLogin(String login) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Usuario usuario = (Usuario) session.createQuery("FROM Usuario u WHERE u.login = :login ", Usuario.class)
                .setParameter("login", login)
                .uniqueResult();

        session.close();

        return usuario;
    }

    public Usuario findByIdWithContatos(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Usuario u LEFT JOIN FETCH u.contatos WHERE u.id = :id", Usuario.class)
                    .setParameter("id", id)
                    .uniqueResult();
        } finally {
            if(session != null) session.close();
        }
    }
}