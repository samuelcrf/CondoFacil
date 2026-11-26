package br.com.condofacil.seguranca;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autenticacao")
public class AutenticacaoController {

	@GetMapping("/usuario-logado")
	public UsuarioLogadoResposta consultarUsuarioLogado(
			@AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {
		return UsuarioLogadoResposta.deUsuarioAutenticado(usuarioAutenticado);
	}
}
