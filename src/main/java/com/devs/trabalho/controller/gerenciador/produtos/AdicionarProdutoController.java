package com.devs.trabalho.controller.gerenciador.produtos;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.model.produtos.Produto;
import com.devs.trabalho.service.ProdutoService;
import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdicionarProdutoController extends FormularioBaseController<Produto> {

    @FXML private TextField nomeField;
    @FXML private TextArea descricaoArea;
    @FXML private TextField quantidadeField;
    @FXML private TextField valorField;

    private ProdutoService produtoService;
    private Estoque estoqueVinculado;

    public AdicionarProdutoController() {
        this.produtoService = new ProdutoService();
    }

    /**
     * Define a qual estoque este novo produto pertencerá.
     */
    public void setEstoqueVinculado(Estoque estoque) {
        this.estoqueVinculado = estoque;
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

            Produto novoProduto = new Produto();
            novoProduto.setNome(nomeField.getText());
            novoProduto.setDescricao(descricaoArea.getText());
            novoProduto.setQuantidade(qtd);
            novoProduto.setValorUnitario(valor);
            novoProduto.setEstoque(this.estoqueVinculado);

            this.produtoService.cadastrar(novoProduto);

            this.salvarConfirmado = true;
            this.entidadeSalva = novoProduto;
            fecharJanela(event);

        } catch (NumberFormatException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Quantidade ou Valor inválidos.");
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Falha ao salvar produto.");
            e.printStackTrace();
        }
    }
}