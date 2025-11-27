package com.devs.trabalho.controller.gerenciador.funcionarios;

import com.devs.trabalho.controller.gerenciador.GerenciadorBaseController;
import com.devs.trabalho.controller.gerenciador.GerenciadorHomeController;
import com.devs.trabalho.controller.popup.ConfirmacaoController;
import com.devs.trabalho.model.funcionario.Funcionario;
import com.devs.trabalho.model.pessoa.Contato;
import com.devs.trabalho.service.FuncionarioService;
import com.devs.trabalho.utils.GerenciadorTelas;
import com.devs.trabalho.utils.JanelaPopUp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GerenciadorFuncionariosController extends GerenciadorHomeController implements Initializable, GerenciadorBaseController {

    private FuncionarioService funcionarioService;
    private static List<Funcionario> funcionarios;

    @FXML protected TableView<Funcionario> tableGerenciador;

    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<String> comboTipoPesquisa;

    /**
     * Método da interface Initializable.
     * Inicializa o serviço, as listas e configura as colunas e pesquisa.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.funcionarioService = new FuncionarioService();
        funcionarios = new ArrayList<>();

        configurarPesquisa();
        configurarColunas();
        atualizarTabela();
    }

    /**
     * Chamar a tela de adicionar um novo Funcionário ao sistema
     * */
    @Override
    public void adicionar(ActionEvent event) throws IOException {
        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<AdicionarFuncionarioController> janelaPopUp = new JanelaPopUp<>("gerenciador/funcionarios/adicionar", "Novo Funcionário", janelaAtual);
        janelaPopUp.getPopupStage().showAndWait();
        if (janelaPopUp.getController().getResult() != null) atualizarTabela();
    }

    /**
     * Chamar a tela de editar um Funcionário no sistema
     * */
    @Override
    public void editar(ActionEvent event) {
        Funcionario funcionario = getFuncionarioSelecionado();
        if(funcionario == null) return;

        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<EditarFuncionarioController> popup = new JanelaPopUp<>("gerenciador/funcionarios/editar", "Editar Funcionário", janelaAtual);
        popup.getController().setFuncionarioEdicao(funcionario);
        popup.getPopupStage().showAndWait();

        if (popup.getController().getResult() != null) atualizarTabela();
    }

    /**
     * Chamar a tela de remover um Funcionário no sistema
     * */
    @Override
    public void remover(ActionEvent event) {
        if(getFuncionarioSelecionado() == null) return;

        JanelaPopUp<ConfirmacaoController> popup = new JanelaPopUp<>("popup/confirmacao", "Confirmar Exclusão", GerenciadorTelas.getStagePrincipal());
        popup.getController().setDescricao("Você realmente deseja excluir este registro?");
        popup.getPopupStage().showAndWait();

        if(popup.getController().getResult()) {
            this.funcionarioService.remover(getFuncionarioSelecionado());
            this.atualizarTabela();
        }
    }

    /**
     * Pega o conteúdo do ComboBox de filtro e o conteúdo do TextField de pesquisa
     * e filtra a tabela com base no termo digitado.
     **/
    @Override
    public void consultar(ActionEvent event) {
        String termo = this.campoPesquisa.getText().toLowerCase().trim();
        String tipoFiltro = this.comboTipoPesquisa.getValue();

        if (termo.isBlank()) {
            atualizarTabela();
            return;
        }

        List<Funcionario> listaFiltrada = new ArrayList<>();

        if (funcionarios != null) {
            listaFiltrada = funcionarios.stream().filter(funcionario -> {
                switch (tipoFiltro) {
                    case "Nome":
                        return funcionario.getNome().toLowerCase().contains(termo);

                    case "Função":
                        return funcionario.getFuncao() != null && funcionario.getFuncao().toLowerCase().contains(termo);

                    case "Endereço":
                        return funcionario.getEndereco() != null && funcionario.getEndereco().toLowerCase().contains(termo);

                    case "Contato":
                        if (funcionario.getContatos() == null) return false;
                        return funcionario.getContatos().stream().anyMatch(contato -> contato.getValorContato().toLowerCase().contains(termo));

                    default:
                        return false;
                }
            }).collect(Collectors.toList());
        }

        this.tableGerenciador.setItems(FXCollections.observableArrayList(listaFiltrada));
    }

    /**
     * Mostar todos os campos de um funcionário na tela
     * */
    @FXML
    public void inspecionar(ActionEvent actionEvent) {
        Funcionario funcionario = getFuncionarioSelecionado();
        if(funcionario == null) return;

        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<InspecionarFuncionarioController> popup = new JanelaPopUp<>("gerenciador/funcionarios/inspecionar", "Inspecionar Funcionário", janelaAtual);
        popup.getController().setFuncionarioSelecionado(funcionario);
        popup.getPopupStage().showAndWait();
    }

    /**
     * Configura o combobox com os campos de pesquisa do funcionário
     * */
    private void configurarPesquisa() {
        ObservableList<String> opcoes = FXCollections.observableArrayList(
                "Nome",
                "Função",
                "Endereço",
                "Contato"
        );
        this.comboTipoPesquisa.setItems(opcoes);
        this.comboTipoPesquisa.getSelectionModel().selectFirst();
    }

    /**
     * Seta as colunas da tabela com base nos atributos da classe Funcionario
     * */
    private void configurarColunas() {
        this.tableGerenciador.getColumns().clear();

        // Coluna Nome
        TableColumn<Funcionario, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(150);

        // Coluna Função
        TableColumn<Funcionario, String> colFuncao = new TableColumn<>("Função");
        colFuncao.setCellValueFactory(new PropertyValueFactory<>("funcao"));
        colFuncao.setPrefWidth(120);

        // Coluna Salário
        TableColumn<Funcionario, String> colSalario = new TableColumn<>("Salário");
        colSalario.setCellValueFactory(cellData -> {
            Double salario = cellData.getValue().getSalario();
            return new SimpleStringProperty(salario != null ? String.format("R$ %.2f", salario) : "");
        });
        colSalario.setPrefWidth(100);

        // Coluna Endereço
        TableColumn<Funcionario, String> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colEndereco.setPrefWidth(200);

        // TIPO do CONTATO 1
        TableColumn<Funcionario, String> colTipo1 = new TableColumn<>("Tipo (1)");
        colTipo1.setCellValueFactory(cellData -> {
            Contato c = getContatoPorIndice(cellData.getValue(), 0);
            return new SimpleStringProperty(c != null ? c.getTipoContato() : "");
        });
        colTipo1.setPrefWidth(75);

        // VALOR do CONTATO 1
        TableColumn<Funcionario, String> colValor1 = new TableColumn<>("Contato (1)");
        colValor1.setCellValueFactory(cellData -> {
            Contato c = getContatoPorIndice(cellData.getValue(), 0);
            return new SimpleStringProperty(c != null ? c.getValorContato() : "");
        });
        colValor1.setPrefWidth(150);

        // TIPO do CONTATO 2
        TableColumn<Funcionario, String> colTipo2 = new TableColumn<>("Tipo (2)");
        colTipo2.setCellValueFactory(cellData -> {
            Contato c = getContatoPorIndice(cellData.getValue(), 1);
            return new SimpleStringProperty(c != null ? c.getTipoContato() : "");
        });
        colTipo2.setPrefWidth(75);

        // VALOR do CONTATO 2
        TableColumn<Funcionario, String> colValor2 = new TableColumn<>("Contato (2)");
        colValor2.setCellValueFactory(cellData -> {
            Contato c = getContatoPorIndice(cellData.getValue(), 1);
            return new SimpleStringProperty(c != null ? c.getValorContato() : "");
        });
        colValor2.setPrefWidth(150);

        this.tableGerenciador.getColumns().addAll(colNome, colFuncao, colSalario, colEndereco, colTipo1, colValor1, colTipo2, colValor2);
    }

    /**
     * Pega o contato pelo índice no array de contatos
     * */
    private Contato getContatoPorIndice(Funcionario funcionario, int indice) {
        if (funcionario.getContatos() == null || funcionario.getContatos().isEmpty()) {
            return null;
        }

        List<Contato> lista = new ArrayList<>(funcionario.getContatos());
        if (indice < lista.size()) {
            return lista.get(indice);
        }
        return null;
    }

    /**
     * Atualizar a tabela mostrando todos os funcionários
     * */
    private void atualizarTabela() {
        funcionarios = this.funcionarioService.listarTodosWithContatos();
        ObservableList<Funcionario> observableList = FXCollections.observableArrayList(funcionarios);
        this.tableGerenciador.setItems(observableList);
    }

    /**
     * @return Retorna o funcionário que o usuário clicou na tabela
     * */
    private Funcionario getFuncionarioSelecionado() {
        return this.tableGerenciador.getSelectionModel().getSelectedItem();
    }
}