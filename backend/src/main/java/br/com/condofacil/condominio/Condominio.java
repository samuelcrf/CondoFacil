package br.com.condofacil.condominio;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "condominio")
public class Condominio {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;


    protected Condominio() {
    }

    public Condominio(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }


    @PrePersist
    void definirDataDeCriacao() {
        this.criadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}
