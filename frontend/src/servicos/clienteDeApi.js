export class ErroDeApi extends Error {
  constructor(mensagem, errosPorCampo = {}, codigoHttp = 0) {
    super(mensagem);
    this.errosPorCampo = errosPorCampo;
    this.codigoHttp = codigoHttp;
  }
}

const NOME_DO_COOKIE_CSRF = "XSRF-TOKEN";
const NOME_DO_CABECALHO_CSRF = "X-XSRF-TOKEN";

function lerTokenCsrf() {
  const cookieEncontrado = document.cookie
    .split("; ")
    .find((cookie) => cookie.startsWith(`${NOME_DO_COOKIE_CSRF}=`));
  return cookieEncontrado
    ? decodeURIComponent(cookieEncontrado.split("=")[1])
    : null;
}

async function garantirTokenCsrf() {
  if (!lerTokenCsrf()) {
    await fetch("/api/autenticacao/usuario-logado").catch(() => null);
  }
  return lerTokenCsrf();
}

export async function chamarApi(
  caminho,
  { metodo = "GET", corpoJson, corpoFormulario, mensagemPadraoDeErro } = {},
) {
  const cabecalhos = {};
  let corpo;

  if (corpoJson !== undefined) {
    cabecalhos["Content-Type"] = "application/json";
    corpo = JSON.stringify(corpoJson);
  } else if (corpoFormulario !== undefined) {
    cabecalhos["Content-Type"] = "application/x-www-form-urlencoded";
    corpo = new URLSearchParams(corpoFormulario).toString();
  }

  if (metodo !== "GET") {
    const tokenCsrf = await garantirTokenCsrf();
    if (tokenCsrf) {
      cabecalhos[NOME_DO_CABECALHO_CSRF] = tokenCsrf;
    }
  }

  let resposta;
  try {
    resposta = await fetch(caminho, {
      method: metodo,
      headers: cabecalhos,
      body: corpo,
    });
  } catch {
    throw new ErroDeApi(
      "Não foi possível falar com o servidor. Tente novamente em instantes.",
    );
  }

  const corpoDaResposta = await resposta.json().catch(() => null);

  if (!resposta.ok) {
    throw new ErroDeApi(
      corpoDaResposta?.mensagem ??
        mensagemPadraoDeErro ??
        "Não foi possível concluir a operação.",
      corpoDaResposta?.erros ?? {},
      resposta.status,
    );
  }

  return corpoDaResposta;
}
