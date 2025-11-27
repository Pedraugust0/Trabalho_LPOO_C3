package com.devs.trabalho.controller.gerenciador.funcionarios;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.controller.gerenciador.contatos.AdicionarContatoController;
import com.devs.trabalho.controller.gerenciador.contatos.ControladorContatos;
import com.devs.trabalho.controller.gerenciador.contatos.ItemContatoController;
import com.devs.trabalho.exceptions.usuario.UsuarioNotLoggedException;
import com.devs.trabalho.model.funcionario.Funcionario;
import com.devs.trabalho.model.pessoa.Contato;
import com.devs.trabalho.model.usuario.Usuario;
import com.devs.trabalho.service.FuncionarioService;
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
 * Controlador responsável pelo cadastro de novos funcionários.
 * Implementa ControladorContatos para gerenciar a lista visual de contatos.
 */
public class AdicionarFuncionarioController extends FormularioBaseController<Funcionario> implements ControladorContatos {

    private Set<Contato> contatosFuncionario;
    private FuncionarioService funcionarioService;

    @FXML private TextField nomeField;
    @FXML private TextField funcaoField;
    @FXML private TextField salarioField;
    @FXML private TextField enderecoField;
    @FXML private VBox containerContatos;

    public AdicionarFuncionarioController() {
        this.funcionarioService = new FuncionarioService();
        this.contatosFuncionario = new HashSet<>();
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
            this.contatosFuncionario.add(novoContato);
            adicionarContatoInterface(novoContato);
        }
    }

    /**
     * Adiciona o contato à tela usando o componente reutilizável ItemContato
     */
    private void adicionarContatoInterface(Contato contato) {
        // Carrega o componente visual
        Tela<ItemContatoController> itemTela = new Tela<>("gerenciador/contatos/itemContato");

        ItemContatoController itemController = itemTela.getController();
        Parent linhaVisual = itemTela.getScene().getRoot();


        itemController.setDados(this, contato, linhaVisual);

        this.containerContatos.getChildren().add(linhaVisual);
    }

    /**
     * Implementação da interface ControladorContatos.
     * Remove o contato da lista lógica e visual.
     */
    @Override
    public void removerContatoLista(Contato contato, Parent linhaVisual) {
        this.contatosFuncionario.remove(contato);
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

        Double salario = 0.0;
        try {
            if(!this.salarioField.getText().isBlank()) {
                salario = Double.parseDouble(this.salarioField.getText().replace(",", "."));
            }
        } catch (NumberFormatException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro de Cadastro", "Valor do salário inválido!");
            return;
        }

        this.entidadeSalva = new Funcionario(nomeField.getText(), enderecoField.getText(), funcaoField.getText());
        this.entidadeSalva.setSalario(salario);
        this.entidadeSalva.setContatos(this.contatosFuncionario);
        this.entidadeSalva.setUsuarioDono(usuarioLogado);

        try {
            this.funcionarioService.cadastrar(this.entidadeSalva);
            this.salvarConfirmado = true;
            this.fecharJanela(event);
        } catch (Exception e) {
            GerenciadorTelas.mostrarAlertaErro("Erro de conexão", "Erro ao acessar o banco de dados...");
            this.fecharJanela(event);
        }
    }
}