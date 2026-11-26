package br.com.condofacil.seguranca;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import br.com.condofacil.condominio.Condominio;
import br.com.condofacil.condominio.CondominioRepositorio;
import br.com.condofacil.usuario.PerfilDeUsuario;
import br.com.condofacil.usuario.Usuario;
import br.com.condofacil.usuario.UsuarioRepositorio;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("teste")
@Transactional
class AcessoPorPerfilIntegracaoTest {

    @Autowired
    private MockMvc simuladorDeRequisicoes;

    @Autowired
    private CondominioRepositorio condominioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    private Usuario sindicoA;
    private Usuario moradorA;
    private Usuario prestadorA;
    private Usuario sindicoB;
    private Usuario moradorB;

    @BeforeEach
    void criarOsDoisCondominiosComSeusUsuarios() {
        Condominio condominioA = condominioRepositorio.save(new Condominio("Condomínio A", "Rua ABC, 100"));
        Condominio condominioB = condominioRepositorio.save(new Condominio("Condomínio B", "Rua XYZ, 200"));

        sindicoA = criarUsuario(condominioA, "Síndico A", "sindico@condominio-a.com", PerfilDeUsuario.SINDICO);
        moradorA = criarUsuario(condominioA, "Morador A", "morador@condominio-a.com", PerfilDeUsuario.MORADOR);
        prestadorA = criarUsuario(condominioA, "Prestador A", "prestador@condominio-a.com", PerfilDeUsuario.PRESTADOR);
        sindicoB = criarUsuario(condominioB, "Síndico B", "sindico@condominio-b.com", PerfilDeUsuario.SINDICO);
        moradorB = criarUsuario(condominioB, "Morador B", "morador@condominio-b.com", PerfilDeUsuario.MORADOR);
    }

    private Usuario criarUsuario(Condominio condominio, String nome, String email, PerfilDeUsuario perfil) {
        return usuarioRepositorio.save(new Usuario(condominio, nome, email, "hash-da-senha", perfil));
    }

    //simula que o usuario esta logado no pedido
    private RequestPostProcessor logadoComo(Usuario usuario) {
        return user(UsuarioAutenticado.deUsuario(usuario));
    }

    //acesso por perfil

    @Test
    void deveNegarAListaDeUsuariosParaMoradorComErro403EmJson() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/usuarios").with(logadoComo(moradorA)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensagem").value("Você não tem permissão para realizar esta ação."));
    }

    @Test
    void deveNegarAListaDeUsuariosParaPrestador() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/usuarios").with(logadoComo(prestadorA)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveExigirLoginParaListarUsuarios() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void devePermitirAoSindicoListarOsUsuariosDoProprioCondominioSemExporASenha() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/usuarios").with(logadoComo(sindicoA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].senha").doesNotExist())
                .andExpect(jsonPath("$[*].senhaCriptografada").doesNotExist());
    }

    // ---------------------------------------------------------------- isolamento por condomínio

    @Test
    void deveListarSomenteOsUsuariosDoCondominioDoSindicoLogado() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/usuarios").with(logadoComo(sindicoA)))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder(
                        "sindico@condominio-a.com", "morador@condominio-a.com", "prestador@condominio-a.com")));

        simuladorDeRequisicoes.perform(get("/api/usuarios").with(logadoComo(sindicoB)))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder(
                        "sindico@condominio-b.com", "morador@condominio-b.com")));
    }

    @Test
    void deveBuscarUmUsuarioDoProprioCondominio() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/usuarios/" + moradorA.getId()).with(logadoComo(sindicoA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("morador@condominio-a.com"))
                .andExpect(jsonPath("$.perfil").value("MORADOR"));
    }

    @Test
    void deveResponder404AoBuscarUsuarioDeOutroCondominio() throws Exception {
        // O síndico A tenta ver o morador B trocando o número no endereço e eh negado
        simuladorDeRequisicoes.perform(get("/api/usuarios/" + moradorB.getId()).with(logadoComo(sindicoA)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Usuário não encontrado."));
    }

    @Test
    void deveResponder404AoBuscarUsuarioInexistente() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/usuarios/999999999").with(logadoComo(sindicoA)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDevolverACadaUsuarioApenasOSeuProprioCondominio() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/condominios/meu").with(logadoComo(moradorA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Condomínio A"))
                .andExpect(jsonPath("$.endereco").value("Rua ABC, 100"));

        simuladorDeRequisicoes.perform(get("/api/condominios/meu").with(logadoComo(sindicoB)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Condomínio B"));
    }

    @Test
    void deveExigirLoginParaConsultarOCondominio() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/condominios/meu"))
                .andExpect(status().isUnauthorized());
    }
}