package com.devs.trabalho.controller.gerenciador.clientes;

import com.devs.trabalho.controller.gerenciador.GerenciadorBaseController;
import com.devs.trabalho.controller.gerenciador.GerenciadorHomeController;
import com.devs.trabalho.controller.popup.ConfirmacaoController;
import com.devs.trabalho.model.cliente.Cliente;
import com.devs.trabalho.model.pessoa.Contato;
import com.devs.trabalho.service.ClienteService;
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
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GerenciadorClientesController extends GerenciadorHomeController implements Initializable, GerenciadorBaseController {

    private ClienteService clienteService;
    private static List<Cliente> clientes;

    @FXML protected TableView<Cliente> tableGerenciador;

    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<String> comboTipoPesquisa;

    /**
     * Método da interface Initializable.
     * É chamado sempre que todos os componentes anotados com @FXML forem totalmente carregados.
     * Não da para usar o construtor desse controller pois ele carrega antes dos componentes.
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clienteService = new ClienteService();
        clientes = new ArrayList<>();

        configurarPesquisa();
        configurarColunas();
        atualizarTabela();
    }

    /**
     * Chamar a tela de adicionar um novo Cliente ao sistema
     * */
    @Override
    public void adicionar(ActionEvent event) throws IOException {
        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<AdicionarClienteController> janelaPopUp = new JanelaPopUp<>("gerenciador/clientes/adicionar", "Novo Cliente", janelaAtual);
        janelaPopUp.getPopupStage().showAndWait();
        if (janelaPopUp.getController().getResult() != null) atualizarTabela();
    }

    /**
     * Chamar a tela de editar um Cliente no sistema
     * */
    @Override
    public void editar(ActionEvent event) {
        Cliente cliente = getClienteSelecionado();
        if(cliente == null) return;
        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<EditarClienteController> popup = new JanelaPopUp<>("gerenciador/clientes/editar", "Editar Cliente", janelaAtual);
        popup.getController().setClienteEdicao(cliente);
        popup.getPopupStage().showAndWait();
        if (popup.getController().getResult() != null) atualizarTabela();
    }

    /**
     * Chamar a tela de remover um Cliente no sistema
     * */
    @Override
    public void remover(ActionEvent event) {
        if(getClienteSelecionado() == null) return;
        JanelaPopUp<ConfirmacaoController> popup = new JanelaPopUp<>("popup/confirmacao", "Confirmar Exclusão", GerenciadorTelas.getStagePrincipal());
        popup.getController().setDescricao("Você realmente desja excluir esse registro?");
        popup.getPopupStage().showAndWait();
        if(popup.getController().getResult()) {
            this.clienteService.remover(getClienteSelecionado());
            this.atualizarTabela();
        }
    }

    /**
     * Pega o conteúdo do ComboBox de filtro e o conteúdo do TextField de pesquisa
     * E com base no termo do filtro ele mostra na tabela os clientes filtrados
     **/
    @Override
    public void consultar(ActionEvent event) {
        String termo = this.campoPesquisa.getText().toLowerCase().trim();
        String tipoFiltro = this.comboTipoPesquisa.getValue();

        //Se a pessoa clicar em consultar, mas não digitar nada, mostra todos os dados
        if (termo.isBlank()) {
            atualizarTabela();
            return;
        }

        List<Cliente> listaFiltrada = new ArrayList<>();

        if (clientes != null) {
            listaFiltrada = clientes.stream().filter(cliente -> {
                switch (tipoFiltro) {
                    case "Nome":
                        return cliente.getNome().toLowerCase().contains(termo);

                    case "Endereço":
                        return cliente.getEndereco() != null && cliente.getEndereco().toLowerCase().contains(termo);

                    case "Contato":
                        if (cliente.getContatos() == null) return false;
                        // itera sobre os contatos procurando o que conter o termo de pesquisa
                        return cliente.getContatos().stream().anyMatch(contato -> contato.getValorContato().toLowerCase().contains(termo));

                    default:
                        return false;
                }
                //Retorna em uma lista de clientes
            }).collect(Collectors.toList());
        }

        //Joga o resultado filtrado para a tabela na tela
        this.tableGerenciador.setItems(FXCollections.observableArrayList(listaFiltrada));
    }

    /**
     * Mostar todos os campos de um cliente na tela
     * */
    @FXML
    public void inspecionar(ActionEvent actionEvent) {
        Cliente cliente = getClienteSelecionado();
        if(cliente == null) return;
        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<InspecionarClienteController> popup = new JanelaPopUp<>("gerenciador/clientes/inspecionar", "Inspecionar Cliente", janelaAtual);
        popup.getController().setClienteSelecionado(cliente);
        popup.getPopupStage().showAndWait();
    }

    /**
     * Configura o combobox com os campos de pesquisa do cliente
     * */
    private void configurarPesquisa() {
        ObservableList<String> opcoes = FXCollections.observableArrayList(
                "Nome",
                "Endereço",
                "Contato" // Apenas o valor (email, telefone, etc)
        );
        //Adiciona ao combobox
        this.comboTipoPesquisa.setItems(opcoes);
        //Deixa pré-selecionado o primeiro da lista de itens
        this.comboTipoPesquisa.getSelectionModel().selectFirst();
    }

    /**
     * Seta as colunas da tabela com base nos atributos da classe Cliente
     * */
    private void configurarColunas() {
        //Limpa para não arriscar duplicar as colunas caso já existam
        this.tableGerenciador.getColumns().clear();

        // Coluna Nome
        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(150);

        // Coluna Endereço
        TableColumn<Cliente, String> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        colEndereco.setPrefWidth(200);



        // TIPO do CONTATO 1
        TableColumn<Cliente, String> colTipo1 = new TableColumn<>("Tipo (1)");
        colTipo1.setCellValueFactory(cellData -> {
            Contato c = getContatoPorIndice(cellData.getValue(), 0);
            return new SimpleStringProperty(c != null ? c.getTipoContato() : "");
        });
        colTipo1.setPrefWidth(75);

        // VALOR do CONTATO 1
        TableColumn<Cliente, String> colValor1 = new TableColumn<>("Contato (1)");
        colValor1.setCellValueFactory(cellData -> {
            Contato c = getContatoPorIndice(cellData.getValue(), 0);
            return new SimpleStringProperty(c != null ? c.getValorContato() : "");
        });
        colValor1.setPrefWidth(150);



        // TIPO do CONTATO 2
        TableColumn<Cliente, String> colTipo2 = new TableColumn<>("Tipo (2)");
        colTipo2.setCellValueFactory(cellData -> {
            Contato c = getContatoPorIndice(cellData.getValue(), 1);
            return new SimpleStringProperty(c != null ? c.getTipoContato() : "");
        });
        colTipo2.setPrefWidth(75);

        // VALOR do CONTATO 2
        TableColumn<Cliente, String> colValor2 = new TableColumn<>("Contato (2)");
        colValor2.setCellValueFactory(cellData -> {
            Contato c = getContatoPorIndice(cellData.getValue(), 1);
            return new SimpleStringProperty(c != null ? c.getValorContato() : "");
        });
        colValor2.setPrefWidth(150);

        // Adiciona as colunas a tabela
        this.tableGerenciador.getColumns().addAll(colNome, colEndereco, colTipo1, colValor1, colTipo2, colValor2);
    }

    /**
     * Pega o contato pelo índice no array de contatos
     * Usado para poder pegar o primeiro e o segundo contato do usuário com segurança
     *
     * @param indice representa o índice do array, 0 = primeiro contato, 1 = segundo...
     * @return retorna o contato encontrado no indice do array de contatos
     *
     * */
    private Contato getContatoPorIndice(Cliente cliente, int indice) {
        // Se não tiver nenhum contato
        if (cliente.getContatos() == null || cliente.getContatos().isEmpty()) {
            return null;
        }

        List<Contato> lista = new ArrayList<>(cliente.getContatos());
        // Se o índice fornecido não for maior que o tamanho da lista (valor incorreto)
        if (indice < lista.size()) {
            return lista.get(indice);
        }
        return null;
    }

    /**
     * Atualizar a tabela mostrando todos os clientes de um determinado usuário
     * */
    private void atualizarTabela() {
        clientes = this.clienteService.listarTodosWithContatos();
        ObservableList<Cliente> observableList = FXCollections.observableArrayList(clientes);
        this.tableGerenciador.setItems(observableList);
    }

    /**
     * @return Retorna o cliente que o usuário clicou na tabela
     * */
    private Cliente getClienteSelecionado() {
        return this.tableGerenciador.getSelectionModel().getSelectedItem();
    }
}