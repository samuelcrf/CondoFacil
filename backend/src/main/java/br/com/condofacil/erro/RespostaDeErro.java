package br.com.condofacil.erro;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RespostaDeErro(String mensagem, @JsonProperty("erros") Map<String, String> errosPorCampo) {
}