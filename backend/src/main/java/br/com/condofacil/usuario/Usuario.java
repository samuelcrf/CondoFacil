package br.com.condofacil.usuario;

import java.time.*;

import br.com.condofacil.condominio.Condominio;
import jakarta.persistence.*;


@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "condominio_id", nullable = false)
    private Condominio condominio;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "senha_criptografada", nullable = false, length = 100)
    private String senhaCriptografada;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PerfilDeUsuario perfil;

    @Column(length = 20)
    private String telefone;


    @Column(length = 50)
    private String unidade;


    @Column(length = 100)
    private String especialidade;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;


    protected Usuario() {
    }

    public Usuario(Condominio condominio, String nome, String email,
                   String senhaCriptografada, PerfilDeUsuario perfil) {
        this.condominio = condominio;
        this.nome = nome;
        this.email = email;
        this.senhaCriptografada = senhaCriptografada;
        this.perfil = perfil;
    }

    @PrePersist
    void definirDataDeCriacao() {
        this.criadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaCriptografada() {
        return senhaCriptografada;
    }

    public void setSenhaCriptografada(String senhaCriptografada) {
        this.senhaCriptografada = senhaCriptografada;
    }

    public PerfilDeUsuario getPerfil() {
        return perfil;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}
