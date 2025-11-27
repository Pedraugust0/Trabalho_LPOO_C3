package com.devs.trabalho.utils;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Classe utilitária estilo popup mais complexa
 * Permite bloquear outras telas até que esta seja fechada
 *
 * @param <Controller> Qual controller essa janela utiliza
*/
public class JanelaPopUp<Controller> extends Tela<Controller> {

    private Stage popupStage;

    public JanelaPopUp(String caminhoArquivoSemExtensao, String titulo, Window janelaPai) {
        super(caminhoArquivoSemExtensao);

        this.popupStage = new Stage();
        this.popupStage.setScene(this.getScene());

        this.popupStage.setTitle(titulo);

        // Faz ela bloquear em relação ao pai
        this.popupStage.initModality(Modality.WINDOW_MODAL);
        // Seta o pai
        this.popupStage.initOwner(janelaPai);

        this.popupStage.setResizable(false);
    }

    public Stage getPopupStage() {
        return this.popupStage;
    }

}
