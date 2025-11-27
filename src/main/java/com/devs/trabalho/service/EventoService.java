package com.devs.trabalho.service;

import com.devs.trabalho.dao.evento.EventoDAO;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.exceptions.evento.EventoNotFoundException;
import com.devs.trabalho.model.eventos.Evento;
import com.devs.trabalho.utils.GerenciadorSessao;
import org.hibernate.HibernateException;
import java.util.List;

public class EventoService {

    private final EventoDAO eventoDAO;

    public EventoService() {
        this.eventoDAO = new EventoDAO();
    }

    public void cadastrar(Evento evento) throws SistemaException {
        try {
            evento.setUsuarioDono(GerenciadorSessao.getUsuarioLogado());
            eventoDAO.save(evento);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao cadastrar evento", e);
        }
    }

    public void atualizar(Evento evento) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();
            eventoDAO.update(evento);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao atualizar evento", e);
        }
    }

    public void remover(Evento evento) throws SistemaException {
        try {
            GerenciadorSessao.getUsuarioLogado();
            eventoDAO.delete(evento);
        } catch (HibernateException e) {
            throw new SistemaException("Falha ao remover evento", e);
        }
    }

    public Evento findById(Long id) throws EventoNotFoundException, SistemaException {
        try {
            Evento evento = eventoDAO.findById(id, GerenciadorSessao.getUsuarioLogado());
            if (evento == null) {
                throw new EventoNotFoundException("Evento não encontrado pelo ID: " + id);
            }
            return evento;
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao buscar evento", e);
        }
    }

    public List<Evento> listarTodos() throws SistemaException {
        try {
            return eventoDAO.findAll(GerenciadorSessao.getUsuarioLogado());
        } catch (HibernateException e) {
            throw new SistemaException("Erro ao listar eventos", e);
        }
    }
}