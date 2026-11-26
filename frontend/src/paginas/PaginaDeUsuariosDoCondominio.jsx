import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { listarUsuariosDoCondominio } from "../servicos/servicoDeUsuarios.js";
import { rotuloDoPerfil } from "../utilitarios/rotulosDePerfil.js";

export default function PaginaDeUsuariosDoCondominio() {
  // Lista recebida da API, ou null enquanto carrega
  const [usuarios, setUsuarios] = useState(null);
  const [mensagemDeErro, setMensagemDeErro] = useState("");

  useEffect(() => {
    let componenteAindaExiste = true;
    listarUsuariosDoCondominio()
      .then((lista) => {
        if (componenteAindaExiste) {
          setUsuarios(lista);
        }
      })
      .catch((erro) => {
        if (componenteAindaExiste) {
          // 401 sessao expirada
          setMensagemDeErro(
            erro.codigoHttp === 401
              ? "Sua sessão expirou. Entre novamente."
              : erro.message,
          );
        }
      });
    return () => {
      componenteAindaExiste = false;
    };
  }, []);

  return (
    <section>
      <h1 className="h3">Usuários do condomínio</h1>
      <p className="text-muted">Pessoas cadastradas no seu condomínio.</p>

      {mensagemDeErro && (
        <div className="alert alert-danger" role="alert">
          {mensagemDeErro}{" "}
          {mensagemDeErro.includes("sessão") && (
            <Link to="/login">Ir para o login</Link>
          )}
        </div>
      )}

      {!mensagemDeErro && usuarios === null && (
        <p className="text-muted">Carregando...</p>
      )}

      {usuarios && usuarios.length === 0 && <p>Nenhum usuário cadastrado.</p>}

      {usuarios && usuarios.length > 0 && (
        <div className="table-responsive">
          <table className="table table-striped align-middle">
            <thead>
              <tr>
                <th>Nome</th>
                <th>E-mail</th>
                <th>Perfil</th>
                <th>Unidade / Especialidade</th>
                <th>Telefone</th>
                <th>Situação</th>
              </tr>
            </thead>
            <tbody>
              {usuarios.map((usuario) => (
                <tr key={usuario.id}>
                  <td>{usuario.nome}</td>
                  <td>{usuario.email}</td>
                  <td>{rotuloDoPerfil(usuario.perfil)}</td>

                  <td>{usuario.unidade ?? usuario.especialidade ?? "-"}</td>
                  <td>{usuario.telefone ?? "-"}</td>
                  <td>
                    {usuario.ativo ? (
                      <span className="badge text-bg-success">Ativo</span>
                    ) : (
                      <span className="badge text-bg-secondary">Inativo</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  );
}
