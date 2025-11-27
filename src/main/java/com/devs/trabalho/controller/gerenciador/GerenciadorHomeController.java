package com.devs.trabalho.controller.gerenciador;

import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller da HOMEPAGE do Gerenciador de Entidades.
 * Serve como base para a navegação do menu lateral.
 * */
public class GerenciadorHomeController {

    @FXML
    private Button buttonTelaClientes;

    @FXML
    private Button buttonTelaEstoques;

    @FXML
    private Button buttonTelaEventos;

    @FXML
    private Button buttonTelaFuncionarios;

    public GerenciadorHomeController() {}

    /**
     * Chamado pelos botões do menu lateral para navegar entre as telas
     * @param event evento que chamou o método trocarTela
     * */
    @FXML
    void trocarTela(ActionEvent event) {
        // Pelo evento pega o botão que o invocou (casting)
        Button buttonPressionado = (Button) event.getSource();

        if (buttonPressionado == buttonTelaClientes) {
            GerenciadorTelas.irParaGerenciadorClientes(false);

        } else if (buttonPressionado == buttonTelaEstoques) {
            GerenciadorTelas.irParaGerenciadorEstoques(false);

        } else if (buttonPressionado == buttonTelaEventos) {
            GerenciadorTelas.irParaGerenciadorEventos(false);

        } else if (buttonPressionado == buttonTelaFuncionarios) {
            GerenciadorTelas.irParaGerenciadorFuncionarios(false);
        }
    }

    public Button getButtonTelaClientes() {
        return buttonTelaClientes;
    }

    public Button getButtonTelaEstoques() {
        return buttonTelaEstoques;
    }

    public Button getButtonTelaEventos() {
        return buttonTelaEventos;
    }

    public Button getButtonTelaFuncionarios() {
        return buttonTelaFuncionarios;
    }
}