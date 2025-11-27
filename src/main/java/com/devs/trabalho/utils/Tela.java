package com.devs.trabalho.utils;

import com.devs.trabalho.exceptions.SistemaException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe utilitária que representa uma Tela
 * @param <Controller> qual o controller que essa tela utiliza
 */
public class Tela<Controller> {

    private Scene scene;
    private Controller controller;
    private String caminhoFXML;

    private static final String BASE_PATH = "/com/devs/trabalho/fxml/";

    /**
     * @param caminhoArquivoSemExtensao caminho para o arquivo fmxl sem o .fxml no final
     * */
    public Tela(String caminhoArquivoSemExtensao) {
        this.caminhoFXML = BASE_PATH + caminhoArquivoSemExtensao + ".fxml";

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();

            this.controller = loader.getController();
            this.scene = new Scene(root);

        } catch (IOException | NullPointerException e) {
            // NullPointerException acontece se o caminho estiver errado
            throw new SistemaException("Erro ao carregar tela: " + caminhoFXML, e);
        }
    }

    /**
     * Exibir esta tela em um Stage específico.
     *
     * @param stage stage que a scene da Tela será exibida
     * @param tituloJanela Titulo da Tela
     * @param centralizar se a tela deverá aparecer centralizada na tela do usuário
     */
    public void exibirNoStage(Stage stage, String tituloJanela, boolean centralizar) {
        stage.setScene(this.scene);

        if (tituloJanela != null) {
            stage.setTitle(tituloJanela);
        }

        stage.show();
        stage.sizeToScene();

        if(centralizar) {
            stage.centerOnScreen();
        }
    }

    public Scene getScene() {
        return scene;
    }

    public Controller getController() {
        return controller;
    }
}