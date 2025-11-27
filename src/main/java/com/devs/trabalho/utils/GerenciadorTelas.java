package com.devs.trabalho.utils;

import com.devs.trabalho.controller.entrada.EntradaController;
import com.devs.trabalho.controller.gerenciador.GerenciadorHomeController;
import com.devs.trabalho.controller.gerenciador.clientes.GerenciadorClientesController;
import com.devs.trabalho.controller.gerenciador.estoques.GerenciadorEstoquesController;
import com.devs.trabalho.controller.gerenciador.eventos.GerenciadorEventosController;
import com.devs.trabalho.controller.gerenciador.funcionarios.GerenciadorFuncionariosController;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Classe utilitária para reutilizar telas em outros controladores
 * */
public class GerenciadorTelas {

    /** Stage|Janela principal do sistema*/
    private static Stage stagePrincipal;

    public static void setStagePrincipal(Stage stage) {
        stagePrincipal = stage;
    }

    public static Stage getStagePrincipal() {
        return stagePrincipal;
    }

    /**
     * Mostrar um Alert estilo PopUp avisando um erro
     * @param titulo Titulo do popup (aparece no topo da janela)
     * @param mensagem "Descrição" que aparece no popup
     * */
    public static void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Mostrar um Alert estilo PopUp avisando algo
     * @param titulo Titulo do popup (aparece no topo da janela)
     * @param mensagem "Descrição" que aparece no popup
     * */
    public static void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


    // Métodos para levar para as outras telas //

    public static void irParaEntrada(boolean centralizar) {
        Tela<EntradaController> telaEntrada = new Tela<>("entrada/entrada");

        stagePrincipal.sizeToScene();

        telaEntrada.exibirNoStage(stagePrincipal, "Login do Sistema", centralizar);
    }

    public static void irParaGerenciadorHome(boolean centralizar) {
        Tela<GerenciadorHomeController> telaGerenciador = new Tela<>("gerenciador/home");

        stagePrincipal.sizeToScene();

        telaGerenciador.exibirNoStage(stagePrincipal, "Gerenciador", centralizar);
    }

    public static void irParaGerenciadorClientes(boolean centralizar) {
        Tela<GerenciadorClientesController> telaGerenciador = new Tela<>("gerenciador/clientes/clientes");

        stagePrincipal.sizeToScene();

        telaGerenciador.exibirNoStage(stagePrincipal, "Gerenciador - Clientes", centralizar);
    }

    public static void irParaGerenciadorEstoques(boolean centralizar) {
        Tela<GerenciadorEstoquesController> telaGerenciador = new Tela<>("gerenciador/estoques/estoques");

        stagePrincipal.sizeToScene();

        telaGerenciador.exibirNoStage(stagePrincipal, "Gerenciador - Estoques", centralizar);
    }

    public static void irParaGerenciadorEventos(boolean centralizar) {
        Tela<GerenciadorEventosController> telaGerenciador = new Tela<>("gerenciador/eventos/eventos");

        stagePrincipal.sizeToScene();

        telaGerenciador.exibirNoStage(stagePrincipal, "Gerenciador - Eventos", centralizar);
    }

    public static void irParaGerenciadorFuncionarios(boolean centralizar) {
        Tela<GerenciadorFuncionariosController> telaGerenciador = new Tela<>("gerenciador/funcionarios/funcionarios");

        stagePrincipal.sizeToScene();

        telaGerenciador.exibirNoStage(stagePrincipal, "Gerenciador - Funcionários", centralizar);
    }
}