package br.com.condofacil.seguranca;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.condofacil.usuario.PerfilDeUsuario;
import br.com.condofacil.usuario.Usuario;


public class UsuarioAutenticado implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String nome;
    private final String email;
    private final PerfilDeUsuario perfil;
    private final Long idDoCondominio;
    private final boolean ativo;
    private String senhaCriptografada;

    public UsuarioAutenticado(Long id, String nome, String email, String senhaCriptografada,
                              PerfilDeUsuario perfil, Long idDoCondominio, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaCriptografada = senhaCriptografada;
        this.perfil = perfil;
        this.idDoCondominio = idDoCondominio;
        this.ativo = ativo;
    }

    //cria o usuario autenticado a partir do banco
    public static UsuarioAutenticado deUsuario(Usuario usuario) {
        return new UsuarioAutenticado(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenhaCriptografada(),
                usuario.getPerfil(),
                usuario.getCondominio().getId(),
                usuario.isAtivo());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public PerfilDeUsuario getPerfil() {
        return perfil;
    }

    public Long getIdDoCondominio() {
        return idDoCondominio;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    @Override
    public String getPassword() {
        return senhaCriptografada;
    }


    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isEnabled() {
        return ativo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public void eraseCredentials() {
        this.senhaCriptografada = null;
    }
}