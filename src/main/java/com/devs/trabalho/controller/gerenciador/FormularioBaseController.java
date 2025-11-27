package com.devs.trabalho.controller.gerenciador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Classe auxiliar para padronizar os controllers que agem como formulário
 * @param <T> Representa a classe da entidade usada no formulário
 */
public abstract class FormularioBaseController<T> {

    /** Ao Herdar, para o método salvar funcionar, vocẽ precisa atribuir algo a este atributo */
    protected T entidadeSalva;

    /** Atributo usado para avisar se a entidade foi salva ou não */
    protected boolean salvarConfirmado;

    /**
     * Método auxiliar para retornar o resultado do formulário.
     *
     * @return Retorna a entidade apenas se salvo for true, se não ele retorna null.
     * */
    public T getResult() {
        return this.salvarConfirmado ? this.entidadeSalva : null;
    }

    @FXML
    protected abstract void salvar(ActionEvent event);

    /**
     * Cancelar o formulário de cadastro.
     * @param event Componente que invocou o método (botão de cancelar)
     * */
    @FXML
    protected void cancelar(ActionEvent event) {
        this.entidadeSalva = null;
        this.salvarConfirmado = false;

        this.fecharJanela(event);
    }

    /**
     * Fechar a janela.
     * @param event Componente que invocou o método (botão de salvar)
     * */
    protected void fecharJanela(ActionEvent event) {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();

        stage.close();
    }
}
