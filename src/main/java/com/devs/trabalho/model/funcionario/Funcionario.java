package com.devs.trabalho.model.funcionario;

import com.devs.trabalho.model.pessoa.Pessoa;
import com.devs.trabalho.model.usuario.Usuario;

import javax.persistence.*;

@Entity
@Table(name = "Funcionarios")
@PrimaryKeyJoinColumn(name = "id")
public class Funcionario extends Pessoa {

    @Column(name = "funcao")
    private String funcao;

    @Column(name = "salario")
    private Double salario;

    @ManyToOne
    @JoinColumn(name = "usuario_dono_id", nullable = false)
    private Usuario usuarioDono;

    public Funcionario() { super(); }

    public Funcionario(String nome, String funcao) {
        super(nome);
        this.funcao = funcao;
    }

    public Funcionario(String nome, String endereco, String funcao) {
        super(nome, endereco);
        this.funcao = funcao;
    }

    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }

    public Double getSalario() { return salario; }
    public void setSalario(Double salario) { this.salario = salario; }

    public Usuario getUsuarioDono() { return usuarioDono; }
    public void setUsuarioDono(Usuario usuarioDono) { this.usuarioDono = usuarioDono; }
}