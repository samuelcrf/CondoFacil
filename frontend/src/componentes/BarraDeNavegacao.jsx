import { Link } from "react-router-dom";

export default function BarraDeNavegacao() {
  return (
    <nav className="navbar navbar-dark bg-dark">
      <div className="container">
        <Link className="navbar-brand" to="/">
          CondoFácil
        </Link>
      </div>
    </nav>
  );
}
