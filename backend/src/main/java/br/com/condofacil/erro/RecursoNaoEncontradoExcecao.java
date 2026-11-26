package br.com.condofacil.erro;

public class RecursoNaoEncontradoExcecao extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoExcecao(String mensagem) {
        super(mensagem);
    }
}