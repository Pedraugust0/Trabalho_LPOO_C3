package com.devs.trabalho.controller.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Classe para poder para confirmar ações do usuário
 * */
public class ConfirmacaoController {

    /** Atributo para mostrar se foi confirmado ou se não */
    private boolean resposta;

    /** Atributo para a descriçaõ ex: (quer excluir o usuário X?)*/
    @FXML
    private Label labelDescricao;

    /** Método chamado pelo botão de confirmar*/
    @FXML
    public void confirmar(ActionEvent event) {
        this.resposta = true;

        this.fecharJanela(event);
    }

    /** Método chamado pelo botão de cancelar*/
    @FXML
    public void cancelar(ActionEvent event) {
        this.resposta = false;

        this.fecharJanela(event);
    }

    /**
     * Método auxiliar para fechar a janela.
     * @param event Componente que invocou o fecharJanela (botão de confirmar e cancelar)
     * */
    private void fecharJanela(ActionEvent event) {
        //Pega o botão (casting)
        Button button = (Button) event.getSource();
        //Através do botão pega a janela (stage)
        Stage stage = (Stage) button.getScene().getWindow();

        stage.close();
    }

    /**
     * Método para ser chamado pelo controlador que invocou o popup de confirmação.
     * @return retorna se o usuário confirmou ou não.
     * */
    public boolean getResult() {
        return this.resposta;
    }

    public void setDescricao(String descricao) {
        this.labelDescricao.setText(descricao);
    }
}
