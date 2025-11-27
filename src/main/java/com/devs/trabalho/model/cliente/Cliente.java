package com.devs.trabalho.model.cliente;

import com.devs.trabalho.model.pessoa.Pessoa;
import com.devs.trabalho.model.usuario.Usuario;

import javax.persistence.*;

@Entity
@Table(name = "Clientes")
public class Cliente extends Pessoa {

    @ManyToOne
    @JoinColumn(name = "usuario_dono_id", nullable = false)
    private Usuario usuarioDono;

    public Cliente() { super(); }

    public Cliente(String nome) {
        super(nome);
    }

    public Cliente(String nome, String endereco) {
        super(nome, endereco);
    }

    public Usuario getUsuarioDono() {
        return usuarioDono;
    }

    public void setUsuarioDono(Usuario usuarioDono) {
        this.usuarioDono = usuarioDono;
    }
}