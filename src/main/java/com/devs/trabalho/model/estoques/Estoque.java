package com.devs.trabalho.model.estoques;

import com.devs.trabalho.model.produtos.Produto;
import com.devs.trabalho.model.usuario.Usuario;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Estoques")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "usuario_dono_id", nullable = false)
    private Usuario usuarioDono;

    @OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produto> produtos;

    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    public Estoque(String nome) {
        this.nome = nome;
        this.produtos = new ArrayList<>();
    }

    /**
     * Método auxiliar (não persistido no banco) que calcula o valor total
     * dos ativos neste estoque.
     * @return Soma de (Quantidade * Valor Unitário) de todos os produtos.
     */
    public Double getValorTotalEstoque() {
        if (this.produtos == null || this.produtos.isEmpty()) {
            return 0.0;
        }
        return this.produtos.stream()
                .mapToDouble(p -> {
                    double preco = p.getValorUnitario() != null ? p.getValorUnitario() : 0.0;
                    int qtd = p.getQuantidade() != null ? p.getQuantidade() : 0;
                    return preco * qtd;
                })
                .sum();
    }

    // Getters e Setters normais...
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
    public Usuario getUsuarioDono() { return usuarioDono; }
    public void setUsuarioDono(Usuario usuarioDono) { this.usuarioDono = usuarioDono; }
}