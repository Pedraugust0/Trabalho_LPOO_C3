package com.devs.trabalho.controller.gerenciador.eventos;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.exceptions.usuario.UsuarioNotLoggedException;
import com.devs.trabalho.model.cliente.Cliente;
import com.devs.trabalho.model.eventos.Evento;
import com.devs.trabalho.model.usuario.Usuario;
import com.devs.trabalho.service.ClienteService;
import com.devs.trabalho.service.EventoService;
import com.devs.trabalho.utils.GerenciadorSessao;
import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class AdicionarEventoController extends FormularioBaseController<Evento> implements Initializable {

    private EventoService eventoService;
    private ClienteService clienteService;

    @FXML private TextField nomeField;
    @FXML private TextField tipoField;
    @FXML private TextField localField;
    @FXML private TextField numConvidadosField;
    @FXML private TextArea descricaoArea;
    @FXML private DatePicker dataInicioPicker;
    @FXML private DatePicker dataFinalPicker;
    @FXML private ComboBox<Cliente> comboCliente;

    public AdicionarEventoController() {
        this.eventoService = new EventoService();
        this.clienteService = new ClienteService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarClientes();
    }

    private void carregarClientes() {
        // Carrega lista de clientes para vincular ao evento
        try {
            comboCliente.setItems(FXCollections.observableArrayList(clienteService.listarTodos()));
            // Define como o objeto Cliente aparece no ComboBox (apenas o nome)
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
        } catch (SistemaException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Não foi possível carregar os clientes. (Erro no banco de dados)");
        }
    }

    @Override
    @FXML
    protected void salvar(ActionEvent event) {
        Usuario usuarioLogado;
        try {
            usuarioLogado = GerenciadorSessao.getUsuarioLogado();
        } catch(UsuarioNotLoggedException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro Login", "Você precisa estar logado!");
            fecharJanela(event);
            GerenciadorTelas.irParaEntrada(true);
            return;
        }

        if (this.nomeField.getText().isBlank()) {
            GerenciadorTelas.mostrarAlertaErro("Campos Obrigatórios", "Nome e Data Início são obrigatórios.");
            return;
        }

        int numConvidados = 0;
        try {
            if (!numConvidadosField.getText().isBlank()) {
                numConvidados = Integer.parseInt(numConvidadosField.getText());
            }
        } catch (NumberFormatException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro de Formato", "Número de convidados deve ser um valor numérico.");
            return;
        }

        Evento novoEvento = new Evento();
        novoEvento.setNome(nomeField.getText());
        novoEvento.setTipo(tipoField.getText());
        novoEvento.setLocal(localField.getText());
        novoEvento.setDescricao(descricaoArea.getText());
        novoEvento.setNumConvidados(numConvidados);
        novoEvento.setDataInicio(dataInicioPicker.getValue());
        novoEvento.setDataFinal(dataFinalPicker.getValue());
        novoEvento.setCliente(comboCliente.getValue());
        novoEvento.setUsuarioDono(usuarioLogado);

        try {
            this.eventoService.cadastrar(novoEvento);
            this.entidadeSalva = novoEvento;
            this.salvarConfirmado = true;
            fecharJanela(event);
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Erro ao cadastrar evento no banco de dados.");
        }
    }
}