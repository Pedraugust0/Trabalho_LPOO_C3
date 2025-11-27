package com.devs.trabalho.service;

import com.devs.trabalho.dao.usuario.UsuarioDAO;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.exceptions.usuario.AutenticacaoException;
import com.devs.trabalho.exceptions.usuario.UsuarioNotFoundException;
import com.devs.trabalho.exceptions.usuario.UsuarioUniqueLoginException;
import com.devs.trabalho.model.usuario.Usuario;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.NoResultException;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void cadastrar(Usuario usuario) throws UsuarioUniqueLoginException, SistemaException{

        try {
            usuarioDAO.save(usuario);

        } catch(ConstraintViolationException e) {
            throw new UsuarioUniqueLoginException("O login informado já existe no sistema");

        } catch (HibernateException e) {
            throw new SistemaException("Falha ao acessar o banco de dados", e);
        }

    }

    public Usuario autenticar(String login, String senha) throws SistemaException , UsuarioNotFoundException, AutenticacaoException {
        Usuario usuarioEncontrado;

        try {
            usuarioEncontrado = this.findByLogin(login);
        } catch(HibernateException e) {
            throw new SistemaException("Falha ao acessar o banco de dados", e);
        }

        if(!usuarioEncontrado.getSenha().equals(senha)) {
            throw new AutenticacaoException("login ou senha inválido");
        }

        return usuarioEncontrado;
    }

    public Usuario findById(Long id) throws UsuarioNotFoundException {
        Usuario usuario = usuarioDAO.findById(id);

        if(usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado pelo ID");
        }

        return usuario;
    }

    public Usuario findByLogin(String login) throws UsuarioNotFoundException {
        try {
            Usuario usuarioEncontrado = usuarioDAO.findByLogin(login);

            if(usuarioEncontrado == null) throw new UsuarioNotFoundException("Usuário não encontrado pelo Login");

            return usuarioEncontrado;

        } catch(NoResultException e) {
            throw new UsuarioNotFoundException("Usuário não encontrado pelo Login");
        }
    }

}