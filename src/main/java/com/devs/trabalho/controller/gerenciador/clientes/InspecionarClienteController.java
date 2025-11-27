package com.devs.trabalho.controller.gerenciador.clientes;

import com.devs.trabalho.model.cliente.Cliente;
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

public class InspecionarClienteController {

    @FXML
    private TextField idField;

    @FXML
    private TextField nomeField;

    @FXML
    private TextField enderecoField;

    @FXML
    private VBox containerContatos;

    private Cliente clienteSelecionado;

    public void setClienteSelecionado(Cliente cliente) {
        this.clienteSelecionado = cliente;
        carregarDados();
    }

    private void carregarDados() {
        if (clienteSelecionado == null) return;

        // Preenche campos simples
        this.idField.setText(String.valueOf(clienteSelecionado.getId()));
        this.nomeField.setText(clienteSelecionado.getNome());

        // Tratamento para endereço nulo (estético)
        String endereco = clienteSelecionado.getEndereco();
        this.enderecoField.setText(endereco != null ? endereco : "Não informado");

        // Preenche contatos
        this.containerContatos.getChildren().clear();

        if (clienteSelecionado.getContatos() != null && !clienteSelecionado.getContatos().isEmpty()) {
            for (Contato contato : clienteSelecionado.getContatos()) {
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

        // Exibe Tipo e Valor (Ex: "Telefone: 9999-8888")
        String texto = contato.getTipoContato() + ": " + contato.getValorContato();
        Label lblContato = new Label(texto);
        lblContato.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        // Spacer para preencher a linha (estético)
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        linha.getChildren().addAll(lblContato, spacer);
        this.containerContatos.getChildren().add(linha);
    }

    @FXML
    public void fechar(ActionEvent event) {
        // Fecha a janela atual
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}