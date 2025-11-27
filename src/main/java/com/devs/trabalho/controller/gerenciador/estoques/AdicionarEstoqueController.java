package com.devs.trabalho.controller.gerenciador.estoques;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.service.EstoqueService;
import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AdicionarEstoqueController extends FormularioBaseController<Estoque> {

    @FXML private TextField nomeField;

    private final EstoqueService estoqueService;

    public AdicionarEstoqueController() {
        this.estoqueService = new EstoqueService();
    }

    @Override
    @FXML
    protected void salvar(ActionEvent event) {
        if (this.nomeField.getText().isBlank()) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "O nome do estoque é obrigatório.");
            return;
        }

        this.entidadeSalva = new Estoque(this.nomeField.getText());

        try {
            this.estoqueService.cadastrar(this.entidadeSalva);
            this.salvarConfirmado = true;
            fecharJanela(event);
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Falha ao cadastrar estoque.");
            e.printStackTrace();
        }
    }
}