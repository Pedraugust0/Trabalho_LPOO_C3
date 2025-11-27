package com.devs.trabalho.controller.entrada;

import com.devs.trabalho.exceptions.SistemaException;
import com.devs.trabalho.exceptions.usuario.AutenticacaoException;
import com.devs.trabalho.exceptions.usuario.UsuarioNotFoundException;
import com.devs.trabalho.model.usuario.Usuario;
import com.devs.trabalho.service.UsuarioService;
import com.devs.trabalho.utils.GerenciadorSessao;
import com.devs.trabalho.utils.GerenciadorTelas;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;

/**
 * Classe controladora da tela de login e criar conta (entrada)
 * */
public class EntradaController {
    private final UsuarioService usuarioService;

    @FXML private TextField inputCriarLogin;
    @FXML private PasswordField inputCriarSenha;

    @FXML private TextField inputLogin;
    @FXML private TextField inputNome;
    @FXML private PasswordField inputSenha;

    @FXML private Hyperlink linkCriarConta;
    @FXML private Hyperlink linkEntrarConta;

    @FXML private StackPane mainPane;
    @FXML private AnchorPane paneLogin;
    @FXML private AnchorPane paneRegister;

    public EntradaController() {
        this.usuarioService = new UsuarioService();
    }

    /**
     * Criar o usuário
     * */
    @FXML
    public void criar(ActionEvent event) {
        String nome = inputNome.getText();
        String login = inputCriarLogin.getText();
        String senha = inputCriarSenha.getText();

        try {
            this.usuarioService.cadastrar(new Usuario(nome, login, senha));

            //Lança UsuarioNotFoundException se não encontrar o usuário que
            // acabaou de cadastrar
            this.usuarioService.findByLogin(login);
            GerenciadorTelas.mostrarAlerta("Cadastro", "Conta criada com sucesso!");

        } catch (UsuarioNotFoundException e) {
            GerenciadorTelas.mostrarAlerta("Cadastro", "Sua conta não foi criada");

            //ExceptionInInitalizer é lançado pelo HibernateUtil caso não consiga se instanciar
        } catch(SistemaException | ExceptionInInitializerError e) {
            GerenciadorTelas.mostrarAlertaErro("Erro de Conexão", e.getMessage());
        }
    }

    @FXML
    public void entrar(ActionEvent event) {
        String login = inputLogin.getText();
        String senha = inputSenha.getText();

        try {
            // Valida se os dois campos estão vazios
            if (login.isBlank() && senha.isBlank()) {
                throw new IllegalArgumentException("Campos login e senha estão vazios");
            }

            // Valida se só o login está vazio
            if (login.isBlank()) {
                throw new IllegalArgumentException("O campo login vazio");
            }

            // Valida se só a senha está vazia
            if (senha.isBlank()) {
                throw new IllegalArgumentException("Campo senha está vazia");
            }

            // Se não falhar em nada ele autentica e vai pra home
            Usuario usuarioLogado = this.usuarioService.autenticar(login, senha);
            GerenciadorSessao.setUsuarioLogado(usuarioLogado);

            GerenciadorTelas.irParaGerenciadorHome(true);

        } catch (IllegalArgumentException | UsuarioNotFoundException | AutenticacaoException e) {
            GerenciadorTelas.mostrarAlertaErro("Erro de Login", e.getMessage());

        //ExceptionInInitalizer é lançado pelo HibernateUtil caso não consiga se instanciar
        } catch(SistemaException | ExceptionInInitializerError e) {
            GerenciadorTelas.mostrarAlertaErro("Erro de Conexão", e.getMessage());
            GerenciadorTelas.irParaEntrada(true);
        }
    }

    @FXML
    void trocarTela(ActionEvent event) {
        if(event.getSource() == this.linkCriarConta) {
            this.paneLogin.setVisible(false);
            this.paneRegister.setVisible(true);

        } else if(event.getSource() == this.linkEntrarConta) {
            this.paneRegister.setVisible(false);
            this.paneLogin.setVisible(true);
        }
    }
}
