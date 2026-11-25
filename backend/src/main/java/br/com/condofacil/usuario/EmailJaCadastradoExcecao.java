
package br.com.condofacil.usuario;

public class EmailJaCadastradoExcecao extends RuntimeException {

    public EmailJaCadastradoExcecao(String email) {
        super("Já existe um usuário com o e-mail " + email);
    }
}