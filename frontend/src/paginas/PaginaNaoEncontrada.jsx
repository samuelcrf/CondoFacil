import { Link } from 'react-router-dom'

/**
 * Tela mostrada quando o usuário digita um endereço que não existe (erro 404).
 */
export default function PaginaNaoEncontrada() {
  return (
    <section>
      <h1 className="h3">Página não encontrada</h1>
      <p>O endereço acessado não existe.</p>
      <Link to="/" className="btn btn-primary">
        Voltar para o início
      </Link>
    </section>
  )
}
