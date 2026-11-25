package br.com.condofacil.condominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import br.com.condofacil.usuario.PerfilDeUsuario;
import br.com.condofacil.usuario.Usuario;
import br.com.condofacil.usuario.UsuarioRepositorio;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("teste")
@Transactional
class CadastroDeCondominioIntegracaoTest {


    private static final String JSON_DO_CADASTRO = """
            {
              "nomeDoCondominio": "Condomínio A",
              "enderecoDoCondominio": "Rua ABC, 100",
              "nomeDoSindico": "Síndico A",
              "emailDoSindico": "Sindico@Condominio-A.com",
              "senhaDoSindico": "senha-segura-123"
            }
            """;

    @Autowired
    private MockMvc simuladorDeRequisicoes;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Test
    void deveGravarCondominioESindicoNoBancoSemGuardarASenhaEmTextoPuro() throws Exception {
        simuladorDeRequisicoes.perform(post("/api/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_DO_CADASTRO))
                .andExpect(status().isCreated());

        Usuario sindico = usuarioRepositorio.findByEmailIgnoreCase("sindico@condominio-a.com").orElseThrow();
        assertEquals(PerfilDeUsuario.SINDICO, sindico.getPerfil());
        assertEquals("sindico@condominio-a.com", sindico.getEmail());
        assertEquals("Condomínio A", sindico.getCondominio().getNome());
        // A senha gravada é um hash bcrypt
        assertNotEquals("senha-segura-123", sindico.getSenhaCriptografada());
        assertTrue(sindico.getSenhaCriptografada().startsWith("$2"));
    }

    @Test
    void deveRecusarUmSegundoCadastroComOMesmoEmail() throws Exception {
        simuladorDeRequisicoes.perform(post("/api/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_DO_CADASTRO))
                .andExpect(status().isCreated());

        simuladorDeRequisicoes.perform(post("/api/condominios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_DO_CADASTRO))
                .andExpect(status().isConflict());
    }
}