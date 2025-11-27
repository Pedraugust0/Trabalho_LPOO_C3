package com.devs.trabalho.service;

import com.devs.trabalho.dao.produto.ProdutoDAO;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.exceptions.produto.ProdutoNotFoundException;
import com.devs.trabalho.exceptions.produto.ProdutoNotInAEstoqueException;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.model.produtos.Produto;
import com.devs.trabalho.utils.GerenciadorSessao;
import org.hibernate.HibernateException;

import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    public void cadastrar(Produto produto) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            if (produto.getEstoque() == null) {
                throw new ProdutoNotInAEstoqueException("O produto deve estar vinculado a um estoque.");
            }

            produtoDAO.save(produto);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao cadastrar produto", e);
        }
    }

    public void atualizar(Produto produto) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            if (produto.getEstoque() == null) {
                throw new ProdutoNotInAEstoqueException("O produto deve estar vinculado a um estoque.");
            }

            produtoDAO.update(produto);

        } catch (HibernateException e) {
            throw new SistemaException("Falha ao atualizar produto", e);
        }
    }

    public void remover(Produto produto) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            produtoDAO.deleteById(produto.getId());

        } catch (HibernateException e) {
            throw new SistemaException("Falha ao remover produto", e);
        }
    }

    public Produto findById(Long id) throws ProdutoNotFoundException, SistemaException {
        try {
            Produto produto = produtoDAO.findById(id, GerenciadorSessao.getUsuarioLogado());
            if (produto == null) {
                throw new ProdutoNotFoundException("Produto não encontrado pelo ID: " + id);
            }
            return produto;
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao buscar produto", e);
        }
    }

    public List<Produto> listarTodos() throws SistemaException {
        try {
            return produtoDAO.findAll(GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar produtos", e);
        }
    }

    public List<Produto> listarPorEstoque(Estoque estoque) throws SistemaException {
        try {
            return produtoDAO.findAllByEstoque(estoque, GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar produtos do estoque", e);
        }
    }
}