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

    // Campos invalidos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RespostaDeErro tratarDadosInvalidos(MethodArgumentNotValidException excecao) {
        Map<String, String> erroDeCadaCampo = new LinkedHashMap<>();
        for (FieldError erroDeCampo : excecao.getBindingResult().getFieldErrors()) {
            // guarda a primeira falha se tiver mais de uma
            erroDeCadaCampo.putIfAbsent(erroDeCampo.getField(), erroDeCampo.getDefaultMessage());
        }
        return new RespostaDeErro("Existem campos inválidos.", erroDeCadaCampo);
    }

    //Erro no json
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RespostaDeErro tratarCorpoIlegivel(HttpMessageNotReadableException excecao) {
        return new RespostaDeErro("O formato dos dados enviados é inválido.", Map.of());
    }

    //email ja usado
    @ExceptionHandler(EmailJaCadastradoExcecao.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RespostaDeErro tratarEmailJaCadastrado(EmailJaCadastradoExcecao excecao) {
        return new RespostaDeErro(
                "Já existe um usuário cadastrado com este e-mail.",
                Map.of("emailDoSindico", "Este e-mail já está em uso."));
    }
}