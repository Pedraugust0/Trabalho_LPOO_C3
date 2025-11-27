package com.devs.trabalho.controller.gerenciador.eventos;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.model.cliente.Cliente;
import com.devs.trabalho.model.eventos.Evento;
import com.devs.trabalho.service.ClienteService;
import com.devs.trabalho.service.EventoService;
import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarEventoController extends FormularioBaseController<Evento> implements Initializable {

    private EventoService eventoService;
    private ClienteService clienteService;
    private Evento eventoOriginal;

    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextField tipoField;
    @FXML private TextField localField;
    @FXML private TextField numConvidadosField;
    @FXML private TextArea descricaoArea;
    @FXML private DatePicker dataInicioPicker;
    @FXML private DatePicker dataFinalPicker;
    @FXML private ComboBox<Cliente> comboCliente;

    public EditarEventoController() {
        this.eventoService = new EventoService();
        this.clienteService = new ClienteService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarClientes();
    }

    private void carregarClientes() {
        try {
            comboCliente.setItems(FXCollections.observableArrayList(clienteService.listarTodos()));
            comboCliente.setConverter(new StringConverter<Cliente>() {
                @Override
                public String toString(Cliente cliente) {
                    return cliente == null ? "" : cliente.getNome();
                }
                @Override
                public Cliente fromString(String string) {
                    return null;
                }
            });
        } catch(SistemaException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Não foi possível listar os clientes (Erro Banco de dados).");
        }

    }

    public void setEventoEdicao(Evento evento) {
        this.eventoOriginal = evento;

        idField.setText(String.valueOf(evento.getId()));
        nomeField.setText(evento.getNome());
        tipoField.setText(evento.getTipo());
        localField.setText(evento.getLocal());
        numConvidadosField.setText(String.valueOf(evento.getNumConvidados()));
        descricaoArea.setText(evento.getDescricao());
        dataInicioPicker.setValue(evento.getDataInicio());
        dataFinalPicker.setValue(evento.getDataFinal());

        // Seleciona o cliente correto no combobox
        if (evento.getCliente() != null) {
            for (Cliente c : comboCliente.getItems()) {
                if (c.getId().equals(evento.getCliente().getId())) {
                    comboCliente.setValue(c);
                    break;
                }
            }
        }
    }

    @Override
    @FXML
    protected void salvar(ActionEvent event) {
        if (nomeField.getText().isBlank()) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Nome é obrigatório");
            return;
        }

        int numConvidados = 0;
        try {
            if(!numConvidadosField.getText().isBlank()) numConvidados = Integer.parseInt(numConvidadosField.getText());
        } catch (NumberFormatException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Convidados deve ser número");
            return;
        }

        eventoOriginal.setNome(nomeField.getText());
        eventoOriginal.setTipo(tipoField.getText());
        eventoOriginal.setLocal(localField.getText());
        eventoOriginal.setNumConvidados(numConvidados);
        eventoOriginal.setDescricao(descricaoArea.getText());
        eventoOriginal.setDataInicio(dataInicioPicker.getValue());
        eventoOriginal.setDataFinal(dataFinalPicker.getValue());
        eventoOriginal.setCliente(comboCliente.getValue());

        this.eventoService.atualizar(eventoOriginal);
        this.salvarConfirmado = true;
        this.entidadeSalva = eventoOriginal;

        fecharJanela(event);
    }
}