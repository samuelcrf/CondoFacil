// Nomes de cada perfil como aparecem para o usuário
const rotulosPorPerfil = {
  MORADOR: "Morador",
  SINDICO: "Síndico",
  PRESTADOR: "Prestador",
};

// Converte o perfil enviado pela API
export function rotuloDoPerfil(perfil) {
  return rotulosPorPerfil[perfil] ?? perfil;
}
