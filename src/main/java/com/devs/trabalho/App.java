package com.devs.trabalho;

import com.devs.trabalho.controller.entrada.EntradaController;
import com.devs.trabalho.exceptions.usuario.UsuarioNotLoggedException;
import com.devs.trabalho.utils.GerenciadorTelas;
import com.devs.trabalho.utils.Tela;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage stage;

    /**
     * Método da classe Application.
     * É executado assim que launch() é chamado no main
     * */
    @Override
    public void start(Stage stage) {
        App.stage = stage;

        // Seta no gerenciador de telas o stage criado como o principal
        GerenciadorTelas.setStagePrincipal(stage);

        // Cria a tela de entrada
        Tela<EntradaController> tela = new Tela<>("entrada/entrada");

        // Exibe a tela de entrada no stage principal
        tela.exibirNoStage(stage, "Sistema de Gestão", true);
    }

    public static void main(String[] args) {
        /** método que chama o start a cima */
        launch();
    }
}