package com.devs.trabalho.controller.gerenciador.clientes;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.controller.gerenciador.contatos.AdicionarContatoController;
import com.devs.trabalho.controller.gerenciador.contatos.ControladorContatos;
import com.devs.trabalho.controller.gerenciador.contatos.ItemContatoController;
import com.devs.trabalho.model.cliente.Cliente;
import com.devs.trabalho.model.pessoa.Contato;
import com.devs.trabalho.service.ClienteService;
import com.devs.trabalho.utils.GerenciadorTelas;
import com.devs.trabalho.utils.JanelaPopUp;
import com.devs.trabalho.utils.Tela;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class EditarClienteController extends FormularioBaseController<Cliente> implements ControladorContatos {

    @FXML
    private TextField idField;
    @FXML
    private TextField nomeField;
    @FXML
    private TextField enderecoField;
    @FXML
    private VBox containerContatos;

    private ClienteService clienteService;
    private Cliente clienteOriginal;
    private Set<Contato> contatosEdicao;

    public EditarClienteController() {
        this.clienteService = new ClienteService();
        this.contatosEdicao = new HashSet<>();
    }

    /**
     * Seta os dados do cliente nos campos da tela
     * */
    public void setClienteEdicao(Cliente cliente) {
        //Guarda para poder reutilizar pelo controller
        this.clienteOriginal = cliente;

        //Seta nos campos os dados do cliente
        this.idField.setText(String.valueOf(cliente.getId()));
        this.nomeField.setText(cliente.getNome());
        this.enderecoField.setText(cliente.getEndereco());

        // Se tiver contatos ele os joga para um Set
        if (cliente.getContatos() != null) {
            this.contatosEdicao = new HashSet<>(cliente.getContatos());
        // Se não tiver só instancia um Set vazio
        } else {
            this.contatosEdicao = new HashSet<>();
        }

        // Limpa o container de contatos para não ocorrer de duplicar caso não esteja vazio
        this.containerContatos.getChildren().clear();

        // Adiciona contato por contato no container da interface
        for (Contato contato : this.contatosEdicao) {
            adicionarContatoNaInterface(contato);
        }
    }

    /**
     * Chama a JanelaPopUp para adicionar novos contatos ao cliente
     * */
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

        // Se o contato foi salvo
        if (novoContato != null) {
            this.contatosEdicao.add(novoContato);
            adicionarContatoNaInterface(novoContato);
        }
    }

    /**
     * Adiciona um contato ao container de contatos (lista de contatos)
     * O container de contatos por ter lógica própria precisa de um controller próprio para
     * organização de código e separação do front-end do back-end
     *
     * @param contato Contato a ser adicionado ao container
     * */
    private void adicionarContatoNaInterface(Contato contato) {
        // Carrega a janela de contatos
        Tela<ItemContatoController> janela = new Tela<>("gerenciador/contatos/itemContato");

        // Pega o container pai da janela de contatos
        Parent linhaVisual = janela.getScene().getRoot();

        // Pega o controller da janela de contatos
        ItemContatoController controller = janela.getController();

        // Método que joga o contato a linha do container de contatos
        controller.setDados(this, contato, linhaVisual);

        // Joga a linha para a tela
        this.containerContatos.getChildren().add(linhaVisual);
    }

    /**
     * Método da interface ControladorContatos
     * remove um contato específico da lista de contatos
     * */
    @Override
    public void removerContatoLista(Contato contato, Parent linhaVisual) {
        // Remove da memória (Set)
        this.contatosEdicao.remove(contato);

        // Remove da tela (VBox)
        this.containerContatos.getChildren().remove(linhaVisual);
    }

    @Override
    @FXML
    protected void salvar(ActionEvent event) {
        if (this.nomeField.getText().isBlank()) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Nome é obrigatório");
            return;
        }

        this.clienteOriginal.setNome(nomeField.getText());
        this.clienteOriginal.setEndereco(enderecoField.getText());
        this.clienteOriginal.setContatos(this.contatosEdicao);

        this.clienteService.atualizar(this.clienteOriginal);

        this.salvarConfirmado = true;
        this.entidadeSalva = this.clienteOriginal;

        this.fecharJanela(event);
    }
}