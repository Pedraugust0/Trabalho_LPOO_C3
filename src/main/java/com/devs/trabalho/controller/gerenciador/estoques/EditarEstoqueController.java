package com.devs.trabalho.controller.gerenciador.estoques;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.service.EstoqueService;
import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditarEstoqueController extends FormularioBaseController<Estoque> {

    @FXML private TextField idField;
    @FXML private TextField nomeField;

    private final EstoqueService estoqueService;
    private Estoque estoqueOriginal;

    public EditarEstoqueController() {
        this.estoqueService = new EstoqueService();
    }

    public void setEstoqueEdicao(Estoque estoque) {
        this.estoqueOriginal = estoque;
        this.idField.setText(String.valueOf(estoque.getId()));
        this.nomeField.setText(estoque.getNome());
    }

    @Override
    @FXML
    protected void salvar(ActionEvent event) {
        if (this.nomeField.getText().isEmpty() || this.nomeField.getText().isBlank()) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "O nome do estoque é obrigatório.");
            return;
        }

        this.estoqueOriginal.setNome(this.nomeField.getText());

        try {
            this.estoqueService.atualizar(this.estoqueOriginal);
            this.salvarConfirmado = true;
            this.entidadeSalva = this.estoqueOriginal;
            fecharJanela(event);
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Falha ao atualizar estoque.");
            e.printStackTrace();
        }
    }
}