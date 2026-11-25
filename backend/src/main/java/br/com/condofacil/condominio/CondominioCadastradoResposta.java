package br.com.condofacil.condominio;

public record CondominioCadastradoResposta(Long idDoCondominio, String nomeDoCondominio, Long idDoSindico,
		String emailDoSindico) {
}