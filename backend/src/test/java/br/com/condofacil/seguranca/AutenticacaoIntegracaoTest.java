package br.com.condofacil.seguranca;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
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
class AutenticacaoIntegracaoTest {

    private static final String EMAIL_DO_USUARIO = "usuario@condominio-a.com";
    private static final String SENHA_DO_USUARIO = "senha-segura-123";

    @Autowired
    private MockMvc simuladorDeRequisicoes;

    @Autowired
    private CondominioRepositorio condominioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder codificadorDeSenha;

    
    @BeforeEach
    void criarUsuarioParaOsTestes() {
        Condominio condominio = condominioRepositorio.save(new Condominio("Condomínio A", "Rua ABC, 100"));
        usuarioRepositorio.save(new Usuario(
                condominio, "Usuário A", EMAIL_DO_USUARIO,
                codificadorDeSenha.encode(SENHA_DO_USUARIO), PerfilDeUsuario.SINDICO));
    }

    
    private ResultActions fazerLogin(String email, String senha) throws Exception {
        return simuladorDeRequisicoes.perform(post("/api/autenticacao/login")
                .with(csrf())
                .param("email", email)
                .param("senha", senha));
    }

  
    private MockHttpSession fazerLoginEObterSessao() throws Exception {
        MvcResult resultado = fazerLogin(EMAIL_DO_USUARIO, SENHA_DO_USUARIO)
                .andExpect(status().isOk())
                .andReturn();
        return (MockHttpSession) resultado.getRequest().getSession(false);
    }

    @Test
    void deveEntrarComEmailESenhaCorretosSemDevolverASenha() throws Exception {
        fazerLogin(EMAIL_DO_USUARIO, SENHA_DO_USUARIO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL_DO_USUARIO))
                .andExpect(jsonPath("$.nome").value("Usuário A"))
                .andExpect(jsonPath("$.perfil").value("SINDICO"))
                .andExpect(jsonPath("$.idDoCondominio").exists())
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(jsonPath("$.senhaCriptografada").doesNotExist());
    }

    @Test
    void deveEntrarMesmoQuandoOEmailForDigitadoComLetrasMaiusculas() throws Exception {
        fazerLogin("USUARIO@Condominio-A.com", SENHA_DO_USUARIO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL_DO_USUARIO));
    }

    @Test
    void deveRecusarSenhaIncorreta() throws Exception {
        fazerLogin(EMAIL_DO_USUARIO, "senha-errada-999")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("E-mail ou senha incorretos."));
    }

    @Test
    void deveRecusarEmailInexistenteComAMesmaMensagemDaSenhaIncorreta() throws Exception {
        
        fazerLogin("ninguem@condominio-a.com", SENHA_DO_USUARIO)
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("E-mail ou senha incorretos."));
    }

    @Test
    void deveRecusarUsuarioInativo() throws Exception {
        Usuario usuario = usuarioRepositorio.findByEmailIgnoreCase(EMAIL_DO_USUARIO).orElseThrow();
        usuario.setAtivo(false);
        usuarioRepositorio.saveAndFlush(usuario);

        fazerLogin(EMAIL_DO_USUARIO, SENHA_DO_USUARIO)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRecusarLoginSemOTokenCsrf() throws Exception {
        
        simuladorDeRequisicoes.perform(post("/api/autenticacao/login")
                        .param("email", EMAIL_DO_USUARIO)
                        .param("senha", SENHA_DO_USUARIO))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveManterOUsuarioLogadoNaSessao() throws Exception {
        MockHttpSession sessao = fazerLoginEObterSessao();

        simuladorDeRequisicoes.perform(get("/api/autenticacao/usuario-logado").session(sessao))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL_DO_USUARIO))
                .andExpect(jsonPath("$.perfil").value("SINDICO"));
    }

    @Test
    void deveNegarAConsultaDoUsuarioLogadoParaQuemNaoEntrou() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/autenticacao/usuario-logado"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveEncerrarASessaoNoLogout() throws Exception {
        MockHttpSession sessao = fazerLoginEObterSessao();

        simuladorDeRequisicoes.perform(post("/api/autenticacao/logout").with(csrf()).session(sessao))
                .andExpect(status().isNoContent());

        
        simuladorDeRequisicoes.perform(get("/api/autenticacao/usuario-logado").session(sessao))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveProtegerPorPadraoQualquerEnderecoDaApi() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/qualquer-endereco-da-api"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveLiberarOEnderecoDeSaudeSemLogin() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/saude"))
                .andExpect(status().isOk());
    }
}