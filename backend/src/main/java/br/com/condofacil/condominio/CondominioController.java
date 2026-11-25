package br.com.condofacil.condominio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.*;

@RestController
@RequestMapping("/api/condominios")
public class CondominioController {

	private final CadastroDeCondominioServico cadastroDeCondominioServico;

	public CondominioController(CadastroDeCondominioServico cadastroDeCondominioServico) {
		this.cadastroDeCondominioServico = cadastroDeCondominioServico;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CondominioCadastradoResposta cadastrar(@Valid @RequestBody CadastroDeCondominioRequisicao requisicao) {
		return cadastroDeCondominioServico.cadastrar(requisicao);
	}
}