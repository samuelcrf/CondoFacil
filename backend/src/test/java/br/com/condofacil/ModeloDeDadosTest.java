package br.com.condofacil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import br.com.condofacil.chamado.Chamado;
import br.com.condofacil.chamado.ChamadoRepositorio;
import br.com.condofacil.chamado.StatusDoChamado;
import br.com.condofacil.condominio.Condominio;
import br.com.condofacil.condominio.CondominioRepositorio;
import br.com.condofacil.usuario.PerfilDeUsuario;
import br.com.condofacil.usuario.Usuario;
import br.com.condofacil.usuario.UsuarioRepositorio;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("teste")
class ModeloDeDadosTest {

    @Autowired
    private CondominioRepositorio condominioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ChamadoRepositorio chamadoRepositorio;


    @Autowired
    private TestEntityManager gerenciadorDeTestes;

    private Condominio criarCondominio(String nome) {
        return condominioRepositorio.save(new Condominio(nome, "Rua ABC, 100"));
    }

    private Usuario criarUsuario(Condominio condominio, String email, PerfilDeUsuario perfil) {
        return usuarioRepositorio.save(
                new Usuario(condominio, "Usuário " + email, email, "senha-criptografada-de-teste", perfil));
    }

    @Test
    void deveSalvarELerCondominioUsuariosEChamado() {
        Condominio condominio = criarCondominio("Condomínio A");
        criarUsuario(condominio, "sindico@condominio-a.com", PerfilDeUsuario.SINDICO);
        Usuario morador = criarUsuario(condominio, "morador@condominio-a.com", PerfilDeUsuario.MORADOR);
        Chamado chamado = chamadoRepositorio.save(
                new Chamado(condominio, morador, "Chamado A", "Descrição do chamado A."));

        gerenciadorDeTestes.flush();
        gerenciadorDeTestes.clear();

        Chamado chamadoLidoDoBanco = chamadoRepositorio.findById(chamado.getId()).orElseThrow();
        assertEquals("Chamado A", chamadoLidoDoBanco.getTitulo());
        assertEquals(StatusDoChamado.ABERTO, chamadoLidoDoBanco.getStatus());
        assertNotNull(chamadoLidoDoBanco.getCriadoEm());
        assertNotNull(chamadoLidoDoBanco.getAtualizadoEm());
        assertEquals(morador.getId(), chamadoLidoDoBanco.getMorador().getId());
        assertEquals(condominio.getId(), chamadoLidoDoBanco.getCondominio().getId());
        // Ainda não foi atribuído a nenhum prestador.
        assertEquals(null, chamadoLidoDoBanco.getPrestador());
    }

    @Test
    void deveBuscarUsuarioPorEmailIgnorandoMaiusculas() {
        Condominio condominio = criarCondominio("Condomínio A");
        criarUsuario(condominio, "usuario@condominio-a.com", PerfilDeUsuario.MORADOR);

        assertTrue(usuarioRepositorio.findByEmailIgnoreCase("USUARIO@Condominio-A.com").isPresent());
        assertTrue(usuarioRepositorio.existsByEmailIgnoreCase("usuario@CONDOMINIO-A.com"));
    }

    @Test
    void naoDevePermitirDoisUsuariosComOMesmoEmail() {
        Condominio condominio = criarCondominio("Condomínio A");
        criarUsuario(condominio, "repetido@condominio-a.com", PerfilDeUsuario.MORADOR);

        assertThrows(DataIntegrityViolationException.class, () ->
                usuarioRepositorio.saveAndFlush(new Usuario(
                        condominio, "Outro usuário", "REPETIDO@condominio-a.com", "senha", PerfilDeUsuario.MORADOR)));
    }

    @Test
    void deveListarApenasOsChamadosDoProprioCondominio() {
        Condominio condominioA = criarCondominio("Condomínio A");
        Condominio condominioB = criarCondominio("Condomínio B");
        Usuario moradorA = criarUsuario(condominioA, "morador@condominio-a.com", PerfilDeUsuario.MORADOR);
        Usuario moradorB = criarUsuario(condominioB, "morador@condominio-b.com", PerfilDeUsuario.MORADOR);
        chamadoRepositorio.save(new Chamado(condominioA, moradorA, "Chamado A", "Descrição do chamado A."));
        chamadoRepositorio.save(new Chamado(condominioB, moradorB, "Chamado B", "Descrição do chamado B."));

        List<Chamado> chamadosDoCondominioA = chamadoRepositorio.findAllByCondominioId(condominioA.getId());

        assertEquals(1, chamadosDoCondominioA.size());
        assertEquals("Chamado A", chamadosDoCondominioA.get(0).getTitulo());
    }

    @Test
    void deveListarMoradoresDeUmCondominioSemMisturarPerfis() {
        Condominio condominio = criarCondominio("Condomínio A");
        criarUsuario(condominio, "sindico@condominio-a.com", PerfilDeUsuario.SINDICO);
        criarUsuario(condominio, "morador1@condominio-a.com", PerfilDeUsuario.MORADOR);
        criarUsuario(condominio, "morador2@condominio-a.com", PerfilDeUsuario.MORADOR);

        List<Usuario> moradores = usuarioRepositorio
                .findAllByCondominioIdAndPerfil(condominio.getId(), PerfilDeUsuario.MORADOR);

        assertEquals(2, moradores.size());
    }
}