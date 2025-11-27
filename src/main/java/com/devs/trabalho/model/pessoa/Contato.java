package com.devs.trabalho.model.pessoa;

import javax.persistence.*;

@Entity
@Table(name = "Contatos")
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_contato", nullable = false)
    private String tipoContato;

    @Column(name = "valor_contato", nullable = false)
    private String valorContato;

    public Contato() {}

    public Contato(String tipoContato, String valorContato) {
        this.tipoContato = tipoContato;
        this.valorContato = valorContato;
    }

    public Long getId() {
        return id;
    }
    public String getTipoContato() {
        return tipoContato;
    }
    public void setTipoContato(String tipoContato) {
        this.tipoContato = tipoContato;
    }
    public String getValorContato() {
        return valorContato;
    }
    public void setValorContato(String valorContato) {
        this.valorContato = valorContato;
    }
}