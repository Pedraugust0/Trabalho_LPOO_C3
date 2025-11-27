package com.devs.trabalho.service;

import com.devs.trabalho.dao.estoque.EstoqueDAO;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.exceptions.estoque.EstoqueNotFoundException;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.utils.GerenciadorSessao;
import org.hibernate.HibernateException;
import java.util.List;

public class EstoqueService {

    private final EstoqueDAO estoqueDAO;

    public EstoqueService() {
        this.estoqueDAO = new EstoqueDAO();
    }

    public void cadastrar(Estoque estoque) throws SistemaException {
        try {
            estoque.setUsuarioDono(GerenciadorSessao.getUsuarioLogado());

            estoqueDAO.save(estoque);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao cadastrar estoque", e);
        }
    }

    public void atualizar(Estoque estoque) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            estoqueDAO.update(estoque);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao atualizar estoque", e);
        }
    }

    public void remover(Estoque estoque) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            estoqueDAO.delete(estoque);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao remover estoque", e);
        }
    }

    public Estoque findById(Long id) throws EstoqueNotFoundException, SistemaException {
        try {
            Estoque estoque = estoqueDAO.findById(id, GerenciadorSessao.getUsuarioLogado());
            if (estoque == null) {
                throw new EstoqueNotFoundException("Estoque não encontrado pelo ID: " + id);
            }
            return estoque;
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao buscar estoque", e);
        }
    }

    public Estoque findByIdWithProdutos(Long id) throws EstoqueNotFoundException, SistemaException {
        try {
            Estoque estoque = estoqueDAO.findByIdWithProdutos(id, GerenciadorSessao.getUsuarioLogado());
            if (estoque == null) {
                throw new EstoqueNotFoundException("Estoque não encontrado pelo ID: " + id);
            }
            return estoque;
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao buscar estoque com produtos", e);
        }
    }

    public List<Estoque> listarTodos() throws SistemaException {
        try {
            return estoqueDAO.findAll(GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar estoques", e);
        }
    }

    public List<Estoque> listarTodosComProdutos() throws SistemaException {
        try {
            return estoqueDAO.findAllWithProdutos(GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar estoques", e);
        }
    }
}