package com.devs.trabalho.controller.gerenciador.produtos;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.model.produtos.Produto;
import com.devs.trabalho.service.ProdutoService;
import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditarProdutoController extends FormularioBaseController<Produto> {

    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextArea descricaoArea;
    @FXML private TextField quantidadeField;
    @FXML private TextField valorField;

    private ProdutoService produtoService;
    private Produto produtoOriginal;

    public EditarProdutoController() {
        this.produtoService = new ProdutoService();
    }

    public void setProdutoEdicao(Produto produto) {
        this.produtoOriginal = produto;

        this.idField.setText(String.valueOf(produto.getId()));
        this.nomeField.setText(produto.getNome());
        this.descricaoArea.setText(produto.getDescricao());

        if (produto.getQuantidade() != null) {
            this.quantidadeField.setText(String.valueOf(produto.getQuantidade()));
        } else {
            this.quantidadeField.setText("0");
        }

        if (produto.getValorUnitario() != null) {
            this.valorField.setText(String.valueOf(produto.getValorUnitario()));
        } else {
            this.valorField.setText("0.0");
        }
    }

    @Override
    @FXML
    protected void salvar(ActionEvent event) {
        if (nomeField.getText().isEmpty()) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Nome é obrigatório.");
            return;
        }

        try {
            int qtd = Integer.parseInt(quantidadeField.getText());
            double valor = Double.parseDouble(valorField.getText().replace(",", "."));

            this.produtoOriginal.setNome(nomeField.getText());
            this.produtoOriginal.setDescricao(descricaoArea.getText());
            this.produtoOriginal.setQuantidade(qtd);
            this.produtoOriginal.setValorUnitario(valor);

            this.produtoService.atualizar(this.produtoOriginal);

            this.salvarConfirmado = true;
            this.entidadeSalva = this.produtoOriginal;

            fecharJanela(event);

        } catch (NumberFormatException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Quantidade ou Valor inválidos. Use apenas números.");
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Falha ao atualizar produto.");
        }
    }
}