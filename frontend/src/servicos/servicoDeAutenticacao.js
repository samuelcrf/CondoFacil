import { chamarApi } from "./clienteDeApi.js";

export function entrar(email, senha) {
  return chamarApi("/api/autenticacao/login", {
    metodo: "POST",
    corpoFormulario: { email, senha },
    mensagemPadraoDeErro: "Não foi possível entrar. Tente novamente.",
  });
}

export function sair() {
  return chamarApi("/api/autenticacao/logout", { metodo: "POST" });
}

export async function consultarUsuarioLogado() {
  try {
    return await chamarApi("/api/autenticacao/usuario-logado");
  } catch {
    return null;
  }
}
