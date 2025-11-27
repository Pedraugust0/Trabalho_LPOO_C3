package com.devs.trabalho.model.eventos;

import com.devs.trabalho.model.cliente.Cliente;
import com.devs.trabalho.model.usuario.Usuario;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_final")
    private LocalDate dataFinal;

    @Column(name = "local")
    private String local;

    @Column(name = "num_convidados")
    private int numConvidados;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "fk_cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_dono_id", nullable = false)
    private Usuario usuarioDono;

    public Evento() {}

    public Evento(String nome, String tipo, Usuario usuarioDono) {
        this.nome = nome;
        this.tipo = tipo;
        this.usuarioDono = usuarioDono;
    }

    public Evento(String nome, String tipo, String local, Usuario usuarioDono) {
        this.nome = nome;
        this.tipo = tipo;
        this.local = local;
        this.usuarioDono = usuarioDono;
    }

    public Evento(String nome, String tipo, LocalDate dataInicio, LocalDate dataFinal, String local, int numConvidados, String descricao) {
        this.nome = nome;
        this.tipo = tipo;
        this.dataInicio = dataInicio;
        this.dataFinal = dataFinal;
        this.local = local;
        this.numConvidados = numConvidados;
        this.descricao = descricao;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFinal() { return dataFinal; }
    public void setDataFinal(LocalDate dataFinal) { this.dataFinal = dataFinal; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public int getNumConvidados() { return numConvidados; }
    public void setNumConvidados(int numConvidados) { this.numConvidados = numConvidados; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuarioDono() {
        return usuarioDono;
    }
    public void setUsuarioDono(Usuario usuarioDono) {
        this.usuarioDono = usuarioDono;
    }
}