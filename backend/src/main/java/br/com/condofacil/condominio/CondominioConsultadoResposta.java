package br.com.condofacil.condominio;

public record CondominioConsultadoResposta(Long id, String nome, String endereco) {

	public static CondominioConsultadoResposta deCondominio(Condominio condominio) {
		return new CondominioConsultadoResposta(condominio.getId(), condominio.getNome(), condominio.getEndereco());
	}
}
