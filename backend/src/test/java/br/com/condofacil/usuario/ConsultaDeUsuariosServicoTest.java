package br.com.condofacil.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.condofacil.condominio.Condominio;
import br.com.condofacil.erro.RecursoNaoEncontradoExcecao;


@ExtendWith(MockitoExtension.class)
class ConsultaDeUsuariosServicoTest {

    private static final Long ID_DO_CONDOMINIO = 1L;

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @InjectMocks
    private ConsultaDeUsuariosServico servico;

    private Usuario criarUsuario(String nome, PerfilDeUsuario perfil) {
        Condominio condominio = new Condominio("Condomínio A", "Rua ABC, 100");
        return new Usuario(condominio, nome, nome.toLowerCase().replace(" ", "") + "@condominio-a.com",
                "hash-da-senha", perfil);
    }

    @Test
    void deveListarOsUsuariosDoCondominio() {
        when(usuarioRepositorio.findAllByCondominioIdOrderByNomeAsc(ID_DO_CONDOMINIO))
                .thenReturn(List.of(
                        criarUsuario("Morador A", PerfilDeUsuario.MORADOR),
                        criarUsuario("Síndico A", PerfilDeUsuario.SINDICO)));

        List<UsuarioResumoResposta> usuarios = servico.listarDoCondominio(ID_DO_CONDOMINIO);

        assertEquals(2, usuarios.size());
        assertEquals("Morador A", usuarios.get(0).nome());
        assertEquals(PerfilDeUsuario.SINDICO, usuarios.get(1).perfil());
    }

    @Test
    void deveBuscarUmUsuarioDoCondominio() {
        when(usuarioRepositorio.findByIdAndCondominioId(10L, ID_DO_CONDOMINIO))
                .thenReturn(Optional.of(criarUsuario("Morador A", PerfilDeUsuario.MORADOR)));

        UsuarioResumoResposta usuario = servico.buscarDoCondominio(10L, ID_DO_CONDOMINIO);

        assertEquals("Morador A", usuario.nome());
    }

    @Test
    void deveLancarNaoEncontradoQuandoOUsuarioForDeOutroCondominioOuNaoExistir() {
        when(usuarioRepositorio.findByIdAndCondominioId(99L, ID_DO_CONDOMINIO))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoExcecao.class,
                () -> servico.buscarDoCondominio(99L, ID_DO_CONDOMINIO));
    }
}
