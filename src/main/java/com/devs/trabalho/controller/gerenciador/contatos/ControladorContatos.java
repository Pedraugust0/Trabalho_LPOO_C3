package com.devs.trabalho.controller.gerenciador.contatos;

import com.devs.trabalho.model.pessoa.Contato;
import javafx.scene.Parent;

/**
 * Interface para controladores que precisam controlar os contatos
 * Necessário implementar essa interface nos controladores que controlam contatos
 * para poder chamar o método dentro do ItemContatoController e poder tratar todas as
 * classes que gerenciam um contato como uma só.
 * */
public interface ControladorContatos {
    /**
     * Remove o contato da lista
     * @param contato É o contato a ser removido
     * @param linhaVisual É a linha que ele vai remover
     */
    void removerContatoLista(Contato contato, Parent linhaVisual);
}
