package com.devs.trabalho.controller.gerenciador.funcionarios;

import com.devs.trabalho.model.funcionario.Funcionario;
import com.devs.trabalho.model.pessoa.Contato;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InspecionarFuncionarioController {

    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextField funcaoField;
    @FXML private TextField salarioField;
    @FXML private TextField enderecoField;
    @FXML private VBox containerContatos;

    private Funcionario funcionarioSelecionado;

    public void setFuncionarioSelecionado(Funcionario funcionario) {
        this.funcionarioSelecionado = funcionario;
        carregarDados();
    }

    private void carregarDados() {
        if (funcionarioSelecionado == null) return;

        this.idField.setText(String.valueOf(funcionarioSelecionado.getId()));
        this.nomeField.setText(funcionarioSelecionado.getNome());
        this.funcaoField.setText(funcionarioSelecionado.getFuncao());

        String endereco = funcionarioSelecionado.getEndereco();
        this.enderecoField.setText(endereco != null ? endereco : "Não informado");

        if(funcionarioSelecionado.getSalario() != null) {
            this.salarioField.setText(String.format("R$ %.2f", funcionarioSelecionado.getSalario()));
        } else {
            this.salarioField.setText("R$ 0.00");
        }

        this.containerContatos.getChildren().clear();

        if (funcionarioSelecionado.getContatos() != null && !funcionarioSelecionado.getContatos().isEmpty()) {
            for (Contato contato : funcionarioSelecionado.getContatos()) {
                adicionarContatoNaInterface(contato);
            }
        } else {
            Label lblVazio = new Label("Nenhum contato cadastrado.");
            lblVazio.setStyle("-fx-text-fill: #999; -fx-font-style: italic;");
            this.containerContatos.getChildren().add(lblVazio);
        }
    }

    private void adicionarContatoNaInterface(Contato contato) {
        HBox linha = new HBox(10);
        linha.setAlignment(Pos.CENTER_LEFT);
        linha.setStyle("-fx-background-color: #e8e8e8; -fx-background-radius: 5; -fx-padding: 8;");

        String texto = contato.getTipoContato() + ": " + contato.getValorContato();
        Label lblContato = new Label(texto);
        lblContato.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        linha.getChildren().addAll(lblContato, spacer);
        this.containerContatos.getChildren().add(linha);
    }

    @FXML
    public void fechar(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}