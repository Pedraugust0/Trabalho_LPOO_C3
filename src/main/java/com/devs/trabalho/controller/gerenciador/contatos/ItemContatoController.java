package com.devs.trabalho.controller.gerenciador.contatos;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.model.pessoa.Contato;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;

/**
 * Classe controladora que "representa uma linha" do container de contatos
 * Logo, 3 linhas seriam 3 desse controlador
 * */
public class ItemContatoController {

    @FXML
    private Label lblTexto;

    private ControladorContatos controladorPai;
    private Contato contatoAdicionado;
    private Parent linhaVisual;

    /**
     * Seta os contatos atuais
     */
    public void setDados(ControladorContatos pai, Contato contato, Parent view) {
        this.controladorPai = pai;
        this.contatoAdicionado = contato;
        this.linhaVisual = view;

        // Atualiza o texto na tela
        this.lblTexto.setText(contato.getTipoContato() + ": " + contato.getValorContato());
    }

    @FXML
    public void remover() {
        controladorPai.removerContatoLista(this.contatoAdicionado, this.linhaVisual);
    }
}