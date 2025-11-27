package com.devs.trabalho.controller.gerenciador.contatos;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.model.pessoa.Contato;
import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AdicionarContatoController extends FormularioBaseController<Contato> {

    @FXML
    private TextField tipoContatoField;

    @FXML
    private TextField valorContatoField;

    @Override
    @FXML
    protected void salvar(ActionEvent event) {
        String tipoContato = this.tipoContatoField.getText();
        String valorContato = this.valorContatoField.getText();

        try {
            if (tipoContato.isBlank()) {
                if (valorContato.isBlank()) {
                    throw new IllegalArgumentException("Campo tipo e valor contato estão vazios");
                } else {
                    throw new IllegalArgumentException("Campo tipo contato está vazio");
                }
            } else if (valorContato.isBlank()) {
                throw new IllegalArgumentException("Campo valor está vazio");
            }

            this.salvarConfirmado = true;
            this.entidadeSalva = new Contato(tipoContato, valorContato);
            this.fecharJanela(event);

        } catch (IllegalArgumentException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro de Cadastro", e.getMessage());
        }
    }

    public TextField getTipoContatoField() {
        return tipoContatoField;
    }

    public void setTipoContatoField(TextField tipoContatoField) {
        this.tipoContatoField = tipoContatoField;
    }

    public TextField getValorContatoField() {
        return valorContatoField;
    }

    public void setValorContatoField(TextField valorContatoField) {
        this.valorContatoField = valorContatoField;
    }
}
