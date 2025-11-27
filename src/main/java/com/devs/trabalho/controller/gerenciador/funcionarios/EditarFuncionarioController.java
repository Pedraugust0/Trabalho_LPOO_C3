package com.devs.trabalho.controller.gerenciador.funcionarios;

import com.devs.trabalho.controller.gerenciador.FormularioBaseController;
import com.devs.trabalho.controller.gerenciador.contatos.AdicionarContatoController;
import com.devs.trabalho.controller.gerenciador.contatos.ControladorContatos;
import com.devs.trabalho.controller.gerenciador.contatos.ItemContatoController;
import com.devs.trabalho.model.funcionario.Funcionario;
import com.devs.trabalho.model.pessoa.Contato;
import com.devs.trabalho.service.FuncionarioService;
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

public class EditarFuncionarioController extends FormularioBaseController<Funcionario> implements ControladorContatos {

    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextField funcaoField;
    @FXML private TextField salarioField;
    @FXML private TextField enderecoField;
    @FXML private VBox containerContatos;

    private FuncionarioService funcionarioService;
    private Funcionario funcionarioOriginal;
    private Set<Contato> contatosEdicao;

    public EditarFuncionarioController() {
        this.funcionarioService = new FuncionarioService();
        this.contatosEdicao = new HashSet<>();
    }

    /**
     * Seta os dados do funcionário nos campos da tela
     * */
    public void setFuncionarioEdicao(Funcionario funcionario) {
        this.funcionarioOriginal = funcionario;

        this.idField.setText(String.valueOf(funcionario.getId()));
        this.nomeField.setText(funcionario.getNome());
        this.funcaoField.setText(funcionario.getFuncao());
        this.enderecoField.setText(funcionario.getEndereco());

        if(funcionario.getSalario() != null) {
            this.salarioField.setText(String.valueOf(funcionario.getSalario()));
        } else {
            this.salarioField.setText("0.0");
        }

        if (funcionario.getContatos() != null) {
            this.contatosEdicao = new HashSet<>(funcionario.getContatos());
        } else {
            this.contatosEdicao = new HashSet<>();
        }

        this.containerContatos.getChildren().clear();

        for (Contato contato : this.contatosEdicao) {
            adicionarContatoNaInterface(contato);
        }
    }

    /**
     * Remove o contato da lista (Chamado pelo ItemContatoController)
     */
    public void removerContatoDaLista(Contato contato, Parent linhaVisual) {
        this.contatosEdicao.remove(contato);
        this.containerContatos.getChildren().remove(linhaVisual);
    }

    /**
     * Chama a JanelaPopUp para adicionar novos contatos
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

        if (novoContato != null) {
            this.contatosEdicao.add(novoContato);
            adicionarContatoNaInterface(novoContato);
        }
    }

    /**
     * Adiciona um contato ao container usando a classe Tela e ItemContatoController
     * */
    private void adicionarContatoNaInterface(Contato contato) {
        Tela<ItemContatoController> janela = new Tela<>("gerenciador/contatos/itemContato");
        Parent linhaVisual = janela.getScene().getRoot();
        ItemContatoController controller = janela.getController();

        controller.setDados(this, contato, linhaVisual);

        this.containerContatos.getChildren().add(linhaVisual);
    }

    /**
     * Método da interface ControladoraContatos
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

        double salario = 0.0;
        try {
            if(!this.salarioField.getText().isBlank()) {
                salario = Double.parseDouble(this.salarioField.getText().replace(",", "."));
            }
        } catch (NumberFormatException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro", "Salário inválido");
            return;
        }

        this.funcionarioOriginal.setNome(nomeField.getText());
        this.funcionarioOriginal.setFuncao(funcaoField.getText());
        this.funcionarioOriginal.setSalario(salario);
        this.funcionarioOriginal.setEndereco(enderecoField.getText());
        this.funcionarioOriginal.setContatos(this.contatosEdicao);

        this.funcionarioService.atualizar(this.funcionarioOriginal);

        this.salvarConfirmado = true;
        this.entidadeSalva = this.funcionarioOriginal;

        this.fecharJanela(event);
    }
}