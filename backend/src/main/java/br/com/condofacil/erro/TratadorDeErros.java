package br.com.condofacil.erro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.condofacil.usuario.EmailJaCadastradoExcecao;

@RestControllerAdvice
public class TratadorDeErros {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RespostaDeErro tratarDadosInvalidos(MethodArgumentNotValidException excecao) {
		Map<String, String> erroDeCadaCampo = new LinkedHashMap<>();
		for (FieldError erroDeCampo : excecao.getBindingResult().getFieldErrors()) {

			erroDeCadaCampo.putIfAbsent(erroDeCampo.getField(), erroDeCampo.getDefaultMessage());
		}
		return new RespostaDeErro("Existem campos inválidos.", erroDeCadaCampo);
	}

	// JSON mal formado
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RespostaDeErro tratarCorpoIlegivel(HttpMessageNotReadableException excecao) {
		return new RespostaDeErro("O formato dos dados enviados é inválido.", Map.of());
	}

	// E-mail ja usado por outro usuário: erro 409
	@ExceptionHandler(EmailJaCadastradoExcecao.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public RespostaDeErro tratarEmailJaCadastrado(EmailJaCadastradoExcecao excecao) {
		return new RespostaDeErro("Já existe um usuário cadastrado com este e-mail.",
				Map.of("emailDoSindico", "Este e-mail já está em uso."));
	}

	@ExceptionHandler(RecursoNaoEncontradoExcecao.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RespostaDeErro tratarRecursoNaoEncontrado(RecursoNaoEncontradoExcecao excecao) {
		return new RespostaDeErro(excecao.getMessage(), Map.of());
	}
}