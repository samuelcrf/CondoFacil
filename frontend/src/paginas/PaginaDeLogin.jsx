import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import CampoDeFormulario from "../componentes/CampoDeFormulario.jsx";
import { useAutenticacao } from "../contexto/useAutenticacao.js";

export default function PaginaDeLogin() {
  const { entrarNoSistema } = useAutenticacao();
  const navegarPara = useNavigate();

  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [mensagemDeErro, setMensagemDeErro] = useState("");
  const [enviando, setEnviando] = useState(false);

  async function aoEnviarFormulario(evento) {
    // Impede o comportamento padrão do navegador
    evento.preventDefault();
    setMensagemDeErro("");
    setEnviando(true);
    try {
      await entrarNoSistema(email, senha);
      navegarPara("/");
    } catch (erro) {
      setMensagemDeErro(erro.message);
      setEnviando(false);
    }
  }

  return (
    <section style={{ maxWidth: "26rem" }}>
      <h1 className="h3">Entrar</h1>
      <p className="text-muted">Use o e-mail e a senha cadastrados.</p>

      {mensagemDeErro && (
        <div className="alert alert-danger" role="alert">
          {mensagemDeErro}
        </div>
      )}

      <form onSubmit={aoEnviarFormulario}>
        <CampoDeFormulario
          rotulo="E-mail"
          nome="email"
          tipo="email"
          valor={email}
          aoAlterar={(evento) => setEmail(evento.target.value)}
          obrigatorio
          autoCompletar="email"
        />
        <CampoDeFormulario
          rotulo="Senha"
          nome="senha"
          tipo="password"
          valor={senha}
          aoAlterar={(evento) => setSenha(evento.target.value)}
          obrigatorio
          autoCompletar="current-password"
        />
        <button type="submit" className="btn btn-primary" disabled={enviando}>
          {enviando ? "Entrando..." : "Entrar"}
        </button>
      </form>

      <p className="mt-4 mb-0">
        Ainda não tem cadastro? <Link to="/cadastro">Cadastrar condomínio</Link>
      </p>
    </section>
  );
}
