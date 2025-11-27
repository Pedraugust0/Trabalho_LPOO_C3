package com.devs.trabalho.controller.gerenciador.eventos;

import com.devs.trabalho.controller.gerenciador.GerenciadorBaseController;
import com.devs.trabalho.controller.gerenciador.GerenciadorHomeController;
import com.devs.trabalho.controller.popup.ConfirmacaoController;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.model.eventos.Evento;
import com.devs.trabalho.service.EventoService;
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

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GerenciadorEventosController extends GerenciadorHomeController implements Initializable, GerenciadorBaseController {

    private EventoService eventoService;
    private static List<Evento> eventos;

    @FXML protected TableView<Evento> tableGerenciador;
    @FXML private TextField campoPesquisa;
    @FXML private ComboBox<String> comboTipoPesquisa;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.eventoService = new EventoService();
        eventos = new ArrayList<>();

        configurarPesquisa();
        configurarColunas();
        atualizarTabela();
    }

    @Override
    public void adicionar(ActionEvent event) {
        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<AdicionarEventoController> janelaPopUp = new JanelaPopUp<>("gerenciador/eventos/adicionar", "Novo Evento", janelaAtual);
        janelaPopUp.getPopupStage().showAndWait();
        if (janelaPopUp.getController().getResult() != null) atualizarTabela();
    }

    @Override
    public void editar(ActionEvent event) {
        Evento evento = getEventoSelecionado();
        if (evento == null) return;

        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<EditarEventoController> popup = new JanelaPopUp<>("gerenciador/eventos/editar", "Editar Evento", janelaAtual);
        popup.getController().setEventoEdicao(evento);
        popup.getPopupStage().showAndWait();
        if (popup.getController().getResult() != null) atualizarTabela();
    }

    @Override
    public void remover(ActionEvent event) {
        if (getEventoSelecionado() == null) return;

        JanelaPopUp<ConfirmacaoController> popup = new JanelaPopUp<>("popup/confirmacao", "Confirmar Exclusão", GerenciadorTelas.getStagePrincipal());
        popup.getController().setDescricao("Deseja excluir o evento selecionado?");
        popup.getPopupStage().showAndWait();

        if (popup.getController().getResult()) {
            this.eventoService.remover(getEventoSelecionado());
            this.atualizarTabela();
        }
    }

    @FXML
    public void inspecionar(ActionEvent event) {
        Evento evento = getEventoSelecionado();
        if (evento == null) return;

        Window janelaAtual = this.campoPesquisa.getScene().getWindow();
        JanelaPopUp<InspecionarEventoController> popup = new JanelaPopUp<>("gerenciador/eventos/inspecionar", "Detalhes do Evento", janelaAtual);
        popup.getController().setEventoSelecionado(evento);
        popup.getPopupStage().showAndWait();
    }

    @Override
    public void consultar(ActionEvent event) {
        String termo = this.campoPesquisa.getText().toLowerCase().trim();
        String tipoFiltro = this.comboTipoPesquisa.getValue();

        if (termo.isBlank()) {
            atualizarTabela();
            return;
        }

        List<Evento> listaFiltrada;
        if (eventos != null) {
            listaFiltrada = eventos.stream().filter(ev -> {
                switch (tipoFiltro) {
                    case "Nome": return ev.getNome().toLowerCase().contains(termo);
                    case "Tipo": return ev.getTipo() != null && ev.getTipo().toLowerCase().contains(termo);
                    case "Local": return ev.getLocal() != null && ev.getLocal().toLowerCase().contains(termo);
                    case "Cliente":
                        return ev.getCliente() != null && ev.getCliente().getNome().toLowerCase().contains(termo);
                    default: return false;
                }
            }).collect(Collectors.toList());
            this.tableGerenciador.setItems(FXCollections.observableArrayList(listaFiltrada));
        }
    }

    private void configurarPesquisa() {
        ObservableList<String> opcoes = FXCollections.observableArrayList("Nome", "Tipo", "Local", "Cliente");
        this.comboTipoPesquisa.setItems(opcoes);
        this.comboTipoPesquisa.getSelectionModel().selectFirst();
    }

    private void configurarColunas() {
        this.tableGerenciador.getColumns().clear();

        TableColumn<Evento, String> colNome = new TableColumn<>("Evento");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(150);

        TableColumn<Evento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colTipo.setPrefWidth(100);

        TableColumn<Evento, String> colData = new TableColumn<>("Data Início");
        colData.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDataInicio() != null) {
                return new SimpleStringProperty(cellData.getValue().getDataInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            return new SimpleStringProperty("");
        });
        colData.setPrefWidth(100);

        TableColumn<Evento, String> colLocal = new TableColumn<>("Local");
        colLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colLocal.setPrefWidth(120);

        TableColumn<Evento, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCliente() != null) {
                return new SimpleStringProperty(cellData.getValue().getCliente().getNome());
            }
            return new SimpleStringProperty("Sem Cliente");
        });
        colCliente.setPrefWidth(150);

        this.tableGerenciador.getColumns().addAll(colNome, colTipo, colData, colLocal, colCliente);
    }

    private void atualizarTabela() {
        try {
            eventos = this.eventoService.listarTodos();
            this.tableGerenciador.setItems(FXCollections.observableArrayList(eventos));
        } catch(SistemaException e ) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Não foi possível listar os clientes (Erro Banco de dados).");
        }
    }

    private Evento getEventoSelecionado() {
        return this.tableGerenciador.getSelectionModel().getSelectedItem();
    }
}