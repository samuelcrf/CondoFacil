package br.com.condofacil.condominio;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.condofacil.seguranca.UsuarioAutenticado;

@RestController
@RequestMapping("/api/condominios/meu")
public class CondominioDoUsuarioController {

	private final ConsultaDeCondominioServico consultaDeCondominioServico;

	public CondominioDoUsuarioController(ConsultaDeCondominioServico consultaDeCondominioServico) {
		this.consultaDeCondominioServico = consultaDeCondominioServico;
	}

	@GetMapping
	public CondominioConsultadoResposta consultarMeuCondominio(
			@AuthenticationPrincipal UsuarioAutenticado usuarioLogado) {
		return consultaDeCondominioServico.buscarPorId(usuarioLogado.getIdDoCondominio());
	}
}
