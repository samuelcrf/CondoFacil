package br.com.condofacil.usuario;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.condofacil.erro.RecursoNaoEncontradoExcecao;


@Service
public class ConsultaDeUsuariosServico {

    private final UsuarioRepositorio usuarioRepositorio;

    public ConsultaDeUsuariosServico(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    
    @Transactional(readOnly = true)
    public List<UsuarioResumoResposta> listarDoCondominio(Long idDoCondominio) {
        return usuarioRepositorio.findAllByCondominioIdOrderByNomeAsc(idDoCondominio).stream()
                .map(UsuarioResumoResposta::deUsuario)
                .toList();
    }

    /**
     * Busca um usuário pelo id dentro do condomínio informado.
     *
     * @throws RecursoNaoEncontradoExcecao se o id não existir OU pertencer a outro condomínio
     */
    @Transactional(readOnly = true)
    public UsuarioResumoResposta buscarDoCondominio(Long idDoUsuario, Long idDoCondominio) {
        return usuarioRepositorio.findByIdAndCondominioId(idDoUsuario, idDoCondominio)
                .map(UsuarioResumoResposta::deUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoExcecao("Usuário não encontrado."));
    }
}
