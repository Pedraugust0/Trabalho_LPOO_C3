package com.devs.trabalho.service;

import com.devs.trabalho.dao.funcionario.FuncionarioDAO;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.exceptions.funcionario.FuncionarioNotFoundException;
import com.devs.trabalho.model.funcionario.Funcionario;
import com.devs.trabalho.utils.GerenciadorSessao;
import org.hibernate.HibernateException;
import java.util.List;

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService() {
        this.funcionarioDAO = new FuncionarioDAO();
    }

    public void cadastrar(Funcionario funcionario) throws SistemaException {
        try {
            funcionario.setUsuarioDono(GerenciadorSessao.getUsuarioLogado());

            funcionarioDAO.save(funcionario);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao cadastrar funcionário", e);
        }
    }

    public void atualizar(Funcionario funcionario) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            funcionarioDAO.update(funcionario);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao atualizar funcionário", e);
        }
    }

    public void remover(Funcionario funcionario) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            funcionarioDAO.delete(funcionario);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao remover funcionário", e);
        }
    }

    public Funcionario findById(Long id) throws FuncionarioNotFoundException, SistemaException {
        try {
            Funcionario funcionario = funcionarioDAO.findById(id, GerenciadorSessao.getUsuarioLogado());
            if (funcionario == null) {
                throw new FuncionarioNotFoundException("Funcionário não encontrado pelo ID: " + id);
            }
            return funcionario;
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao buscar funcionário", e);
        }
    }

    public Funcionario findByIdWithContatos(Long id) throws FuncionarioNotFoundException, SistemaException {
        try {
            Funcionario funcionario = funcionarioDAO.findByIdWithContatos(id, GerenciadorSessao.getUsuarioLogado());
            if (funcionario == null) {
                throw new FuncionarioNotFoundException("Funcionário não encontrado pelo ID: " + id);
            }
            return funcionario;
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao buscar funcionário com contatos", e);
        }
    }

    public List<Funcionario> listarTodos() throws SistemaException {
        try {
            return funcionarioDAO.findAll(GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar funcionários", e);
        }
    }

    public List<Funcionario> listarTodosWithContatos() throws SistemaException {
        try {
            return funcionarioDAO.findAllWithContatos(GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar funcionários", e);
        }
    }
}