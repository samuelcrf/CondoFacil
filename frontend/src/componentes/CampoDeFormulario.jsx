export default function CampoDeFormulario({
  rotulo,
  nome,
  tipo = "text",
  valor,
  aoAlterar,
  erro,
  obrigatorio = false,
  tamanhoMinimo,
  tamanhoMaximo,
  dica,
  autoCompletar,
}) {
  const identificadorDoCampo = `campo-${nome}`;

  return (
    <div className="mb-3">
      <label htmlFor={identificadorDoCampo} className="form-label">
        {rotulo}
        {obrigatorio && <span className="text-danger"> *</span>}
      </label>

      <input
        id={identificadorDoCampo}
        name={nome}
        type={tipo}
        className={erro ? "form-control is-invalid" : "form-control"}
        value={valor}
        onChange={aoAlterar}
        required={obrigatorio}
        minLength={tamanhoMinimo}
        maxLength={tamanhoMaximo}
        autoComplete={autoCompletar}
      />

      {dica && !erro && <div className="form-text">{dica}</div>}
      {erro && <div className="invalid-feedback">{erro}</div>}
    </div>
  );
}
