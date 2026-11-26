package br.com.condofacil.seguranca;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.condofacil.usuario.UsuarioRepositorio;

@Service
public class UsuarioDetalhesServico implements UserDetailsService {

	private final UsuarioRepositorio usuarioRepositorio;

	public UsuarioDetalhesServico(UsuarioRepositorio usuarioRepositorio) {
		this.usuarioRepositorio = usuarioRepositorio;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioRepositorio.findByEmailIgnoreCase(email.trim()).map(UsuarioAutenticado::deUsuario)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
	}
}
