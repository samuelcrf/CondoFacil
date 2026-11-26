package br.com.condofacil.seguranca;

import br.com.condofacil.usuario.PerfilDeUsuario;

public record UsuarioLogadoResposta(Long id, String nome, String email, PerfilDeUsuario perfil, Long idDoCondominio) {

	public static UsuarioLogadoResposta deUsuarioAutenticado(UsuarioAutenticado usuario) {
		return new UsuarioLogadoResposta(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil(),
				usuario.getIdDoCondominio());
	}
}
