import { Link } from "react-router-dom";

//Tela mostrada quando o usuario está logado, mas o perfil dele não tem permissão
//para ver a pagina que tentou abrir

export default function PaginaDeAcessoNegado() {
  return (
    <section>
      <h1 className="h3">Acesso negado</h1>
      <p>Você não tem permissão para acessar esta página.</p>
      <Link to="/" className="btn btn-primary">
        Voltar para o início
      </Link>
    </section>
  );
}
