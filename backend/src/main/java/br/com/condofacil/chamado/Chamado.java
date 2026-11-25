package br.com.condofacil.chamado;

import java.time.Instant;
import java.time.LocalDate;

import br.com.condofacil.condominio.Condominio;
import br.com.condofacil.usuario.Usuario;
import jakarta.persistence.*;

//chamado aberto pelo morador
@Entity
@Table(name = "chamado")
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Condominio
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "condominio_id", nullable = false)
    private Condominio condominio;

    // Morador
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "morador_id", nullable = false)
    private Usuario morador;

    // Prestador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestador_id")
    private Usuario prestador;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "local_do_problema", length = 150)
    private String localDoProblema;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusDoChamado status = StatusDoChamado.ABERTO;

    // opcional. A ser desenvolvido mais a frente.
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PrioridadeDoChamado prioridade;

    
    private LocalDate prazo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    //construtor vazio do jpa (obrigatorio)
    protected Chamado() {
    }

    public Chamado(Condominio condominio, Usuario morador, String titulo, String descricao) {
        this.condominio = condominio;
        this.morador = morador;
        this.titulo = titulo;
        this.descricao = descricao;
    }


    @PrePersist
    void definirDatasDeCriacao() {
        Instant agora = Instant.now();
        this.criadoEm = agora;
        this.atualizadoEm = agora;
    }


    @PreUpdate
    void renovarDataDeAtualizacao() {
        this.atualizadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Condominio getCondominio() {
        return condominio;
    }

    public Usuario getMorador() {
        return morador;
    }

    public Usuario getPrestador() {
        return prestador;
    }

    public void setPrestador(Usuario prestador) {
        this.prestador = prestador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalDoProblema() {
        return localDoProblema;
    }

    public void setLocalDoProblema(String localDoProblema) {
        this.localDoProblema = localDoProblema;
    }

    public StatusDoChamado getStatus() {
        return status;
    }

    public void setStatus(StatusDoChamado status) {
        this.status = status;
    }

    public PrioridadeDoChamado getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(PrioridadeDoChamado prioridade) {
        this.prioridade = prioridade;
    }

    public LocalDate getPrazo() {
        return prazo;
    }

    public void setPrazo(LocalDate prazo) {
        this.prazo = prazo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }
}
