package com.devs.trabalho.controller.gerenciador.eventos;

import com.devs.trabalho.model.eventos.Evento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class InspecionarEventoController {

    @FXML private TextField idField;
    @FXML private TextField nomeField;
    @FXML private TextField tipoField;
    @FXML private TextField localField;
    @FXML private TextField dataInicioField;
    @FXML private TextField dataFinalField;
    @FXML private TextField clienteField;
    @FXML private TextField convidadosField;
    @FXML private TextArea descricaoArea;

    public void setEventoSelecionado(Evento evento) {
        if (evento == null) return;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        idField.setText(String.valueOf(evento.getId()));
        nomeField.setText(evento.getNome());
        tipoField.setText(evento.getTipo());
        localField.setText(evento.getLocal());
        convidadosField.setText(String.valueOf(evento.getNumConvidados()));
        descricaoArea.setText(evento.getDescricao());

        if (evento.getDataInicio() != null) dataInicioField.setText(evento.getDataInicio().format(fmt));
        if (evento.getDataFinal() != null) dataFinalField.setText(evento.getDataFinal().format(fmt));

        if (evento.getCliente() != null) {
            clienteField.setText(evento.getCliente().getNome());
        } else {
            clienteField.setText("Sem cliente vinculado");
        }
    }

    @FXML
    public void fechar(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}