//erro quando a api responde com problemas devido à dados invalidos
export class ErroDeApi extends Error {
  constructor(mensagem, errosPorCampo = {}) {
    super(mensagem);
    this.errosPorCampo = errosPorCampo;
  }
}

export async function cadastrarCondominio(dadosDoCadastro) {
  let resposta;
  try {
    resposta = await fetch("/api/condominios", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(dadosDoCadastro),
    });
  } catch {
    // Caiu aqui se a API estiver desligada ou sem conexão.
    throw new ErroDeApi(
      "Não foi possível falar com o servidor. Tente novamente em instantes.",
    );
  }

  //null caso json n seja valido
  const corpoDaResposta = await resposta.json().catch(() => null);

  if (!resposta.ok) {
    throw new ErroDeApi(
      corpoDaResposta?.mensagem ?? "Não foi possível concluir o cadastro.",
      corpoDaResposta?.erros ?? {},
    );
  }

  return corpoDaResposta;
}
