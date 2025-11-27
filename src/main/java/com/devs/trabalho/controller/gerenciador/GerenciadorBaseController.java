package com.devs.trabalho.controller.gerenciador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.io.IOException;

/**
 * Classe abstrata para padronizar os controladores que gerenciam uma entidade
 * */
public interface GerenciadorBaseController {

    @FXML
    abstract void adicionar(ActionEvent event) throws IOException;

    @FXML
    abstract void consultar(ActionEvent event);

    @FXML
    abstract void editar(ActionEvent event);

    @FXML
    abstract void remover(ActionEvent event);
}
