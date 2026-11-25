import { useState } from "react";
import { Link } from "react-router-dom";

import CampoDeFormulario from "../componentes/CampoDeFormulario.jsx";
import { cadastrarCondominio } from "../servicos/servicoDeCondominios.js";

// Valores iniciais do formulário: todos os campos começam vazios.
const formularioVazio = {
  nomeDoCondominio: "",
  enderecoDoCondominio: "",
  nomeDoSindico: "",
  emailDoSindico: "",
  telefoneDoSindico: "",
  senhaDoSindico: "",
  confirmacaoDaSenha: "",
};

export default function PaginaDeCadastroDeCondominio() {
  const [formulario, setFormulario] = useState(formularioVazio);

  const [errosPorCampo, setErrosPorCampo] = useState({});

  const [mensagemDeErro, setMensagemDeErro] = useState("");

  const [enviando, setEnviando] = useState(false);

  const [condominioCadastrado, setCondominioCadastrado] = useState(null);

  function aoAlterarCampo(evento) {
    const { name: nomeDoCampo, value: novoValor } = evento.target;
    setFormulario({ ...formulario, [nomeDoCampo]: novoValor });
  }

  async function aoEnviarFormulario(evento) {
    evento.preventDefault();
    setMensagemDeErro("");
    setErrosPorCampo({});

    if (formulario.senhaDoSindico !== formulario.confirmacaoDaSenha) {
      setErrosPorCampo({ confirmacaoDaSenha: "As senhas não são iguais." });
      return;
    }

    setEnviando(true);
    try {
      const dadosParaEnviar = {
        nomeDoCondominio: formulario.nomeDoCondominio,
        enderecoDoCondominio: formulario.enderecoDoCondominio,
        nomeDoSindico: formulario.nomeDoSindico,
        emailDoSindico: formulario.emailDoSindico,
        senhaDoSindico: formulario.senhaDoSindico,
        telefoneDoSindico: formulario.telefoneDoSindico,
      };
      const resposta = await cadastrarCondominio(dadosParaEnviar);
      setCondominioCadastrado(resposta);
      setFormulario(formularioVazio);
    } catch (erro) {
      setMensagemDeErro(erro.message);
      setErrosPorCampo(erro.errosPorCampo ?? {});
    } finally {
      setEnviando(false);
    }
  }

  if (condominioCadastrado) {
    return (
      <section>
        <div className="alert alert-success" role="alert">
          <h1 className="h5">Cadastro concluído!</h1>
          <p className="mb-0">
            O condomínio{" "}
            <strong>{condominioCadastrado.nomeDoCondominio}</strong> foi
            cadastrado e o síndico ({condominioCadastrado.emailDoSindico}) já
            está registrado. A entrada no sistema (login) será liberada nas
            próximas etapas.
          </p>
        </div>
        <Link to="/" className="btn btn-primary">
          Voltar para o início
        </Link>
      </section>
    );
  }

  return (
    <section>
      <h1 className="h3">Cadastrar condomínio</h1>
      <p className="text-muted">
        Preencha os dados do condomínio e do síndico responsável. Campos com *
        são obrigatórios.
      </p>

      {mensagemDeErro && (
        <div className="alert alert-danger" role="alert">
          {mensagemDeErro}
        </div>
      )}

      <form onSubmit={aoEnviarFormulario} style={{ maxWidth: "40rem" }}>
        <h2 className="h5 mt-4">Dados do condomínio</h2>
        <CampoDeFormulario
          rotulo="Nome do condomínio"
          nome="nomeDoCondominio"
          valor={formulario.nomeDoCondominio}
          aoAlterar={aoAlterarCampo}
          erro={errosPorCampo.nomeDoCondominio}
          obrigatorio
          tamanhoMaximo={150}
        />
        <CampoDeFormulario
          rotulo="Endereço"
          nome="enderecoDoCondominio"
          valor={formulario.enderecoDoCondominio}
          aoAlterar={aoAlterarCampo}
          erro={errosPorCampo.enderecoDoCondominio}
          obrigatorio
          tamanhoMaximo={255}
        />

        <h2 className="h5 mt-4">Dados do síndico</h2>
        <CampoDeFormulario
          rotulo="Nome completo"
          nome="nomeDoSindico"
          valor={formulario.nomeDoSindico}
          aoAlterar={aoAlterarCampo}
          erro={errosPorCampo.nomeDoSindico}
          obrigatorio
          tamanhoMaximo={150}
          autoCompletar="name"
        />
        <CampoDeFormulario
          rotulo="E-mail"
          nome="emailDoSindico"
          tipo="email"
          valor={formulario.emailDoSindico}
          aoAlterar={aoAlterarCampo}
          erro={errosPorCampo.emailDoSindico}
          obrigatorio
          tamanhoMaximo={150}
          autoCompletar="email"
        />
        <CampoDeFormulario
          rotulo="Telefone"
          nome="telefoneDoSindico"
          tipo="tel"
          valor={formulario.telefoneDoSindico}
          aoAlterar={aoAlterarCampo}
          erro={errosPorCampo.telefoneDoSindico}
          tamanhoMaximo={20}
          autoCompletar="tel"
        />
        <CampoDeFormulario
          rotulo="Senha"
          nome="senhaDoSindico"
          tipo="password"
          valor={formulario.senhaDoSindico}
          aoAlterar={aoAlterarCampo}
          erro={errosPorCampo.senhaDoSindico}
          obrigatorio
          tamanhoMinimo={8}
          tamanhoMaximo={72}
          autoCompletar="new-password"
        />
        <CampoDeFormulario
          rotulo="Confirmar senha"
          nome="confirmacaoDaSenha"
          tipo="password"
          valor={formulario.confirmacaoDaSenha}
          aoAlterar={aoAlterarCampo}
          erro={errosPorCampo.confirmacaoDaSenha}
          obrigatorio
          autoCompletar="new-password"
        />

        <button type="submit" className="btn btn-primary" disabled={enviando}>
          {enviando ? "Cadastrando..." : "Cadastrar"}
        </button>
      </form>
    </section>
  );
}
