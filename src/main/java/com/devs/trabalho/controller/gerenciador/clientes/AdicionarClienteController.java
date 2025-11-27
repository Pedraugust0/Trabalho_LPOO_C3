package com.devs.trabalho.controller.gerenciador.clientes;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.controller.gerenciador.contatos.AdicionarContatoController;
import com.devs.trabalho.controller.gerenciador.contatos.ControladorContatos;
import com.devs.trabalho.controller.gerenciador.contatos.ItemContatoController;
import com.devs.trabalho.exceptions.usuario.UsuarioNotLoggedException;
import com.devs.trabalho.model.cliente.Cliente;
import com.devs.trabalho.model.pessoa.Contato;
import com.devs.trabalho.model.usuario.Usuario;
import com.devs.trabalho.service.ClienteService;
import com.devs.trabalho.utils.GerenciadorSessao;
import com.devs.trabalho.utils.GerenciadorTelas;
import com.devs.trabalho.utils.JanelaPopUp;
import com.devs.trabalho.utils.Tela;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

/**
 * Controlador responsável pelo cadastro de novos clientes.
 * Implementa ControladorContatos para gerenciar a lista visual de contatos.
 */
public class AdicionarClienteController extends FormularioBaseController<Cliente> implements ControladorContatos {

    private Set<Contato> contatosCliente;
    private ClienteService clienteService;

    @FXML private TextField enderecoField;
    @FXML private TextField nomeField;
    @FXML private VBox containerContatos;

    public AdicionarClienteController() {
        this.clienteService = new ClienteService();
        this.contatosCliente = new HashSet<>();
    }

    @FXML
    public void adicionarContato() {
        JanelaPopUp<AdicionarContatoController> popup = new JanelaPopUp<>(
                "gerenciador/contatos/adicionar",
                "Adicionar Contato",
                this.nomeField.getScene().getWindow()
        );

        AdicionarContatoController controller = popup.getController();
        Stage janela = popup.getPopupStage();
        janela.centerOnScreen();
        janela.showAndWait();

        Contato novoContato = controller.getResult();

        if (novoContato != null) {
            this.contatosCliente.add(novoContato);
            adicionarContatoInterface(novoContato);
        }
    }

    /**
     * Adiciona o contato usando o componente padronizado (FXML)
     */
    private void adicionarContatoInterface(Contato contato) {
        // Carrega o componente visual
        Tela<ItemContatoController> itemTela = new Tela<>("gerenciador/contatos/itemContato");

        ItemContatoController controller = itemTela.getController();
        Parent linhaVisual = itemTela.getScene().getRoot();

        controller.setDados(this, contato, linhaVisual);

        this.containerContatos.getChildren().add(linhaVisual);
    }

    /**
     * Implementação da Interface: Remove o contato da lista
     */
    @Override
    public void removerContatoLista(Contato contato, Parent linhaVisual) {
        this.contatosCliente.remove(contato);
        this.containerContatos.getChildren().remove(linhaVisual);
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
            GerenciadorTelas.mostrarAlertaErro("Erro de Cadastro", "Campo Nome está em branco!");
            return;
        }

        this.entidadeSalva = new Cliente(nomeField.getText(), enderecoField.getText());
        this.entidadeSalva.setContatos(this.contatosCliente);
        this.entidadeSalva.setUsuarioDono(usuarioLogado);

        try {
            this.clienteService.cadastrar(this.entidadeSalva);
            this.salvarConfirmado = true;
            this.fecharJanela(event);
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro de conexão", "Erro ao acessar o banco de dados...");
            this.fecharJanela(event);
        }
    }
}