import { chamarApi } from "./clienteDeApi.js";

export function cadastrarCondominio(dadosDoCadastro) {
  return chamarApi("/api/condominios", {
    metodo: "POST",
    corpoJson: dadosDoCadastro,
    mensagemPadraoDeErro: "Não foi possível concluir o cadastro.",
  });
}

export function consultarMeuCondominio() {
  return chamarApi("/api/condominios/meu", {
    mensagemPadraoDeErro: "Não foi possível carregar os dados do condomínio.",
  });
}
