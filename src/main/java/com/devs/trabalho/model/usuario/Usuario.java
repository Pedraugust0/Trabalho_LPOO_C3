package com.devs.trabalho.model.usuario;

import com.devs.trabalho.model.pessoa.Pessoa;
import javax.persistence.*;

@Entity
@Table(name = "Usuarios")
public class Usuario extends Pessoa {

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "senha", nullable = false)
    private String senha;

    public Usuario() { super(); }

    public Usuario(String login, String senha) {
        super();
        this.login = login;
        this.senha = senha;
    }

    public Usuario(String nome, String login, String senha) {
        super(nome);
        this.login = login;
        this.senha = senha;
    }

    public Usuario(String nome, String endereco, String login, String senha) {
        super(nome, endereco);
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}