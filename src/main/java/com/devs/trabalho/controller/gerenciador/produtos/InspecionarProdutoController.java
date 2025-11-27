package com.devs.trabalho.controller.gerenciador.produtos;

import com.devs.trabalho.model.produtos.Produto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InspecionarProdutoController {

    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextArea descricaoArea;
    @FXML private TextField quantidadeField;
    @FXML private TextField valorField;
    @FXML private TextField totalField;

    private Produto produtoSelecionado;

    public void setProdutoSelecionado(Produto produto) {
        this.produtoSelecionado = produto;
        carregarDados();
    }

    private void carregarDados() {
        if (produtoSelecionado == null) return;

        this.idField.setText(String.valueOf(produtoSelecionado.getId()));
        this.nomeField.setText(produtoSelecionado.getNome());

        String desc = produtoSelecionado.getDescricao();
        this.descricaoArea.setText(desc != null ? desc : "Sem descrição.");

        int qtd = produtoSelecionado.getQuantidade() != null ? produtoSelecionado.getQuantidade() : 0;
        this.quantidadeField.setText(String.valueOf(qtd));

        double valor = produtoSelecionado.getValorUnitario() != null ? produtoSelecionado.getValorUnitario() : 0.0;
        this.valorField.setText(String.format("R$ %.2f", valor));

        this.totalField.setText(String.format("R$ %.2f", qtd * valor));
    }

    @FXML
    public void fechar(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}