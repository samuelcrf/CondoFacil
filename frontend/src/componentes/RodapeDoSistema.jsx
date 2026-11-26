const anoAtual = new Date().getFullYear();

export default function RodapeDoSistema() {
  return (
    <footer className="bg-light border-top py-3">
      <div className="container text-center text-muted small">
        CondoFácil © {anoAtual}
      </div>
    </footer>
  );
}
