package br.com.condofacil.condominio;

import jakarta.validation.constraints.*;


public record CadastroDeCondominioRequisicao(

        @NotBlank(message = "Informe o nome do condomínio.")
        @Size(max = 150, message = "O nome do condomínio pode ter no máximo 150 caracteres.")
        String nomeDoCondominio,

		@NotBlank(message = "Informe o endereço do condomínio.")        @Size(max = 255, message = "O endereço pode ter no máximo 255 caracteres.")
        String enderecoDoCondominio,

        @NotBlank(message = "Informe o nome do síndico.")
        @Size(max = 150, message = "O nome do síndico pode ter no máximo 150 caracteres.")
        String nomeDoSindico,

        @NotBlank(message = "Informe o e-mail do síndico.")
        @Email(message = "Informe um e-mail válido.")
        @Size(max = 150, message = "O e-mail pode ter no máximo 150 caracteres.")
        String emailDoSindico,

        @NotBlank(message = "Informe a senha.")
        @Size(min = 8, max = 72, message = "A senha deve ter de 8 a 72 caracteres.")
        String senhaDoSindico,

        @Size(max = 20, message = "O telefone pode ter no máximo 20 caracteres.")
        String telefoneDoSindico
) {
}
