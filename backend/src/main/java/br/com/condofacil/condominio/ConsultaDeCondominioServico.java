package br.com.condofacil.condominio;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.condofacil.erro.RecursoNaoEncontradoExcecao;

@Service
public class ConsultaDeCondominioServico {

	private final CondominioRepositorio condominioRepositorio;

	public ConsultaDeCondominioServico(CondominioRepositorio condominioRepositorio) {
		this.condominioRepositorio = condominioRepositorio;
	}

	@Transactional(readOnly = true)
	public CondominioConsultadoResposta buscarPorId(Long idDoCondominio) {
		return condominioRepositorio.findById(idDoCondominio).map(CondominioConsultadoResposta::deCondominio)
				.orElseThrow(() -> new RecursoNaoEncontradoExcecao("Condomínio não encontrado."));
	}
}