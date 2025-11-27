package com.devs.trabalho.service;

import com.devs.trabalho.dao.cliente.ClienteDAO;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.exceptions.cliente.ClienteNotFoundException;
import com.devs.trabalho.model.cliente.Cliente;
import com.devs.trabalho.utils.GerenciadorSessao;
import org.hibernate.HibernateException;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public void cadastrar(Cliente cliente) throws SistemaException {
        try {
            cliente.setUsuarioDono(GerenciadorSessao.getUsuarioLogado());

            clienteDAO.save(cliente);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao cadastrar cliente", e);
        }
    }

    public void atualizar(Cliente cliente) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            clienteDAO.update(cliente);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao atualizar cliente", e);
        }
    }

    public void remover(Cliente cliente) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();

            clienteDAO.delete(cliente);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao remover cliente", e);
        }
    }

    public Cliente findById(Long id) throws ClienteNotFoundException, SistemaException {
        try {
            Cliente cliente = clienteDAO.findById(id, GerenciadorSessao.getUsuarioLogado());
            if (cliente == null) {
                throw new ClienteNotFoundException("Cliente não encontrado pelo ID: " + id);
            }
            return cliente;
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao buscar cliente", e);
        }
    }

    public Cliente findByIdWithContatos(Long id) throws ClienteNotFoundException, SistemaException {
        try {
            Cliente cliente = clienteDAO.findByIdWithContatos(id, GerenciadorSessao.getUsuarioLogado());
            if (cliente == null) {
                throw new ClienteNotFoundException("Cliente não encontrado pelo ID: " + id);
            }
            return cliente;
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao buscar cliente com contatos", e);
        }
    }

    public List<Cliente> listarTodos() throws SistemaException {
        try {
            return clienteDAO.findAll(GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar clientes", e);
        }
    }

    public List<Cliente> listarTodosWithContatos() throws SistemaException {
        try {
            return clienteDAO.findAllWithContatos(GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar clientes", e);
        }
    }
}