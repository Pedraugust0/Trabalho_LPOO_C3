package com.devs.trabalho.controller.gerenciador.estoques;

import com.devs.trabalho.controller.gerenciador.produtos.AdicionarProdutoController;
import com.devs.trabalho.controller.gerenciador.produtos.EditarProdutoController;
import com.devs.trabalho.controller.gerenciador.produtos.InspecionarProdutoController;
import com.devs.trabalho.controller.popup.ConfirmacaoController;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.model.produtos.Produto;
import com.devs.trabalho.service.ProdutoService;
import com.devs.trabalho.utils.GerenciadorTelas;
import com.devs.trabalho.utils.JanelaPopUp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InspecionarEstoqueController {

    @FXML private Label lblNomeEstoque;
    @FXML private Label lblValorTotal;

    // Tabela de Produtos
    @FXML private TableView<Produto> tableProdutos;
    @FXML private TextField campoPesquisaProduto;

    private Estoque estoqueSelecionado;
    private ProdutoService produtoService;
    private List<Produto> produtosCache; // Cache para filtro

    public InspecionarEstoqueController() {
        this.produtoService = new ProdutoService();
        this.produtosCache = new ArrayList<>();
    }

    public void setEstoqueSelecionado(Estoque estoque) {
        this.estoqueSelecionado = estoque;

        // Configura cabeçalho
        this.lblNomeEstoque.setText(estoque.getNome());
        atualizarValorTotal();

        // Configura Tabela
        configurarColunasProdutos();
        atualizarTabelaProdutos();
    }

    @FXML
    public void adicionarProduto(ActionEvent event) {
        Window janelaAtual = this.lblNomeEstoque.getScene().getWindow();

        JanelaPopUp<AdicionarProdutoController> popup = new JanelaPopUp<>(
                "gerenciador/produtos/adicionar",
                "Novo Produto",
                janelaAtual
        );

        // PASSAMOS O ESTOQUE PARA O CONTROLLER DE PRODUTO SABER ONDE SALVAR
        popup.getController().setEstoqueVinculado(this.estoqueSelecionado);

        popup.getPopupStage().showAndWait();

        if (popup.getController().getResult() != null) {
            atualizarTabelaProdutos();
            atualizarValorTotal();
        }
    }

    @FXML
    public void editarProduto(ActionEvent event) {
        Produto produto = tableProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) return;

        Window janelaAtual = this.lblNomeEstoque.getScene().getWindow();
        JanelaPopUp<EditarProdutoController> popup = new JanelaPopUp<>(
                "gerenciador/produtos/editar",
                "Editar Produto",
                janelaAtual
        );

        popup.getController().setProdutoEdicao(produto);
        popup.getPopupStage().showAndWait();

        if (popup.getController().getResult() != null) {
            atualizarTabelaProdutos();
            atualizarValorTotal();
        }
    }

    @FXML
    public void removerProduto(ActionEvent event) {
        Produto produto = tableProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) return;

        JanelaPopUp<ConfirmacaoController> popup = new JanelaPopUp<>(
                "popup/confirmacao",
                "Confirmar Exclusão",
                tableProdutos.getScene().getWindow()
        );
        popup.getController().setDescricao("Deseja excluir o produto '" + produto.getNome() + "'?");
        popup.getPopupStage().showAndWait();

        if (popup.getController().getResult()) {
            try {
                // 1. Remove do Banco (Usando deleteById agora)
                this.produtoService.remover(produto);

                // 2. Atualiza a Tabela (busca do banco de novo)
                atualizarTabelaProdutos();

                // 3. Recalcula o total com base na nova lista
                atualizarDadosEstoque();

            } catch (Exception e) {
                GerenciadorTelas.mostrarAlertaErro("Erro", "Falha ao remover produto.");
            }
        }
    }

    @FXML
    public void inspecionarProduto(ActionEvent event) {
        Produto produto = tableProdutos.getSelectionModel().getSelectedItem();
        if (produto == null) return;

        Window janelaAtual = this.lblNomeEstoque.getScene().getWindow();

        JanelaPopUp<InspecionarProdutoController> popup = new JanelaPopUp<>(
                "gerenciador/produtos/inspecionar",
                "Detalhes do Produto",
                janelaAtual
        );

        popup.getController().setProdutoSelecionado(produto);
        popup.getPopupStage().showAndWait();
    }

    @FXML
    public void buscarProduto() {
        String termo = campoPesquisaProduto.getText().toLowerCase().trim();

        if (termo.isEmpty()) {
            tableProdutos.setItems(FXCollections.observableArrayList(produtosCache));
            return;
        }

        List<Produto> filtrados = produtosCache.stream()
                .filter(p -> p.getNome().toLowerCase().contains(termo) ||
                        (p.getDescricao() != null && p.getDescricao().toLowerCase().contains(termo)))
                .collect(Collectors.toList());

        tableProdutos.setItems(FXCollections.observableArrayList(filtrados));
    }

    private void configurarColunasProdutos() {
        tableProdutos.getColumns().clear();

        TableColumn<Produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Produto, String> colQtd = new TableColumn<>("Qtd");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colQtd.setPrefWidth(60);

        TableColumn<Produto, String> colValor = new TableColumn<>("Valor Unit.");
        colValor.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getValorUnitario()))
        );

        TableColumn<Produto, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(cellData -> {
            double total = cellData.getValue().getValorUnitario() * cellData.getValue().getQuantidade();
            return new SimpleStringProperty(String.format("R$ %.2f", total));
        });

        tableProdutos.getColumns().addAll(colNome, colQtd, colValor, colTotal);
        tableProdutos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void atualizarTabelaProdutos() {
        try {
            // Busca atualizada do banco usando o Estoque atual como filtro
            this.produtosCache = this.produtoService.listarPorEstoque(this.estoqueSelecionado);

            // Atualiza a lista interna do objeto Estoque também (para o calculo do total bater)
            this.estoqueSelecionado.setProdutos(this.produtosCache);

            tableProdutos.setItems(FXCollections.observableArrayList(this.produtosCache));
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Falha ao listar produtos.");
        }
    }

    private void atualizarValorTotal() {
        // Recalcula usando o método do Model que criamos
        double total = this.estoqueSelecionado.getValorTotalEstoque();
        this.lblValorTotal.setText(String.format("Valor Total em Estoque: R$ %.2f", total));
    }

    // Método auxiliar para forçar atualização visual após remoção
    private void atualizarDadosEstoque() {
        double novoTotal = 0.0;
        if (this.produtosCache != null) {
            novoTotal = this.produtosCache.stream()
                    .mapToDouble(p -> p.getValorUnitario() * p.getQuantidade())
                    .sum();
        }
        this.lblValorTotal.setText(String.format("Valor Total em Estoque: R$ %.2f", novoTotal));
    }

    @FXML
    public void fechar() {
        Stage stage = (Stage) lblNomeEstoque.getScene().getWindow();
        stage.close();
    }
}