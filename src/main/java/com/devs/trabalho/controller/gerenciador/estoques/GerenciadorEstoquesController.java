package com.devs.trabalho.controller.gerenciador.estoques;

import com.devs.trabalho.controller.gerenciador.GerenciadorBaseController;
import com.devs.trabalho.controller.gerenciador.GerenciadorHomeController;
import com.devs.trabalho.controller.popup.ConfirmacaoController;
import com.devs.trabalho.model.estoques.Estoque;
import com.devs.trabalho.service.EstoqueService;
import com.devs.trabalho.utils.GerenciadorTelas;
import com.devs.trabalho.utils.JanelaPopUp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GerenciadorEstoquesController extends GerenciadorHomeController implements Initializable, GerenciadorBaseController {

    private EstoqueService estoqueService;
    private List<Estoque> estoques;

    @FXML protected TableView<Estoque> tableGerenciador;
    @FXML private Button buttonAdicionar;
    @FXML private Button buttonEditar;
    @FXML public Button buttonInspecionar;

    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<String> comboTipoPesquisa;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.estoqueService = new EstoqueService();
        this.estoques = new ArrayList<>();

        configurarPesquisa();
        configurarColunas();
        atualizarTabela();
    }

    @Override
    public void adicionar(ActionEvent event) throws IOException {
        Window janelaAtual = this.buttonAdicionar.getScene().getWindow();
        JanelaPopUp<AdicionarEstoqueController> popup = new JanelaPopUp<>(
                "gerenciador/estoques/adicionar",
                "Novo Estoque",
                janelaAtual
        );
        popup.getPopupStage().showAndWait();

        if (popup.getController().getResult() != null) atualizarTabela();
    }

    @Override
    public void editar(ActionEvent event) {
        Estoque estoque = getEstoqueSelecionado();
        if (estoque == null) return;

        Window janelaAtual = this.buttonEditar.getScene().getWindow();
        JanelaPopUp<EditarEstoqueController> popup = new JanelaPopUp<>(
                "gerenciador/estoques/editar",
                "Editar Estoque",
                janelaAtual
        );

        popup.getController().setEstoqueEdicao(estoque);
        popup.getPopupStage().showAndWait();

        if (popup.getController().getResult() != null) atualizarTabela();
    }

    @Override
    public void remover(ActionEvent event) {
        if (getEstoqueSelecionado() == null) return;

        JanelaPopUp<ConfirmacaoController> popup = new JanelaPopUp<>(
                "popup/confirmacao",
                "Confirmar Exclusão",
                GerenciadorTelas.getStagePrincipal()
        );

        popup.getController().setDescricao("Deseja excluir este estoque? Todos os produtos vinculados serão apagados!");
        popup.getPopupStage().showAndWait();

        if (popup.getController().getResult()) {
            this.estoqueService.remover(getEstoqueSelecionado());
            atualizarTabela();
        }
    }

    /**
     * Abre a tela de Inspeção de Estoque, onde os produtos são gerenciados.
     * Carrega o estoque com os produtos (Eager) para evitar LazyInitializationException.
     */
    @FXML
    public void inspecionar(ActionEvent actionEvent) {
        Estoque estoque = getEstoqueSelecionado();
        if (estoque == null) return;

        Window janelaAtual = this.buttonInspecionar.getScene().getWindow();

        try {
            // Busca com fetch para trazer os produtos junto
            Estoque estoqueCarregado = this.estoqueService.findByIdWithProdutos(estoque.getId());

            JanelaPopUp<InspecionarEstoqueController> popup = new JanelaPopUp<>(
                    "gerenciador/estoques/inspecionar",
                    "Gerenciar Produtos do Estoque",
                    janelaAtual
            );

            popup.getController().setEstoqueSelecionado(estoqueCarregado);
            popup.getPopupStage().showAndWait();

            // Atualiza a tabela ao voltar, pois o valor total pode ter mudado com a edição de produtos
            atualizarTabela();

        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Não foi possível carregar os produtos do estoque.");
            e.printStackTrace();
        }
    }

    @Override
    public void consultar(ActionEvent event) {
        String termo = this.campoPesquisa.getText().toLowerCase().trim();
        if (termo.isEmpty()) {
            atualizarTabela();
            return;
        }

        List<Estoque> filtrados = estoques.stream()
                .filter(e -> e.getNome().toLowerCase().contains(termo))
                .collect(Collectors.toList());
        /**
         * .obervableArrayList é um método que transforma os itens
         * em um objeto que possa ser colocado na tabela
         * */
        this.tableGerenciador.setItems(FXCollections.observableArrayList(filtrados));
    }

    private void configurarPesquisa() {
        this.comboTipoPesquisa.setItems(FXCollections.observableArrayList("Nome"));
        this.comboTipoPesquisa.getSelectionModel().selectFirst();
    }

    private void configurarColunas() {
        this.tableGerenciador.getColumns().clear();

        TableColumn<Estoque, String> colNome = new TableColumn<>("Nome do Estoque");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // Coluna Calculada: Valor Total (Soma dos produtos)
        TableColumn<Estoque, String> colTotal = new TableColumn<>("Valor Total em Produtos");
        colTotal.setCellValueFactory(cellData -> {
            Double total = cellData.getValue().getValorTotalEstoque();
            return new SimpleStringProperty(String.format("R$ %.2f", total));
        });

        // Coluna Calculada: Quantidade de Itens
        TableColumn<Estoque, String> colQtd = new TableColumn<>("Qtd. Produtos");
        colQtd.setCellValueFactory(cellData -> {
            int size = cellData.getValue().getProdutos() != null ? cellData.getValue().getProdutos().size() : 0;
            return new SimpleStringProperty(String.valueOf(size));
        });

        this.tableGerenciador.getColumns().addAll(colNome, colQtd, colTotal);
        this.tableGerenciador.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void atualizarTabela() {
        try {
            this.estoques = this.estoqueService.listarTodosComProdutos();

            this.tableGerenciador.setItems(FXCollections.observableArrayList(this.estoques));
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Falha ao listar estoques.");
            e.printStackTrace();
        }
    }

    private Estoque getEstoqueSelecionado() {
        return this.tableGerenciador.getSelectionModel().getSelectedItem();
    }
}