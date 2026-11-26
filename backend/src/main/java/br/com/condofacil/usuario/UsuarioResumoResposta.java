package br.com.condofacil.usuario;

public record UsuarioResumoResposta(
        Long id,
        String nome,
        String email,
        PerfilDeUsuario perfil,
        String telefone,
        String unidade,
        String especialidade,
        boolean ativo
) {

    public static UsuarioResumoResposta deUsuario(Usuario usuario) {
        return new UsuarioResumoResposta(
                usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil(),
                usuario.getTelefone(), usuario.getUnidade(), usuario.getEspecialidade(), usuario.isAtivo());
    }
}