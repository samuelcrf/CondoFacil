package br.com.condofacil.condominio;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.com.condofacil.usuario.EmailJaCadastradoExcecao;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.context.annotation.Import;
import br.com.condofacil.seguranca.ConfiguracaoDeSeguranca;
@WebMvcTest(CondominioController.class)
@Import(ConfiguracaoDeSeguranca.class)
class CondominioControllerTest {

    private static final String JSON_VALIDO = """
            {
              "nomeDoCondominio": "Condomínio A",
              "enderecoDoCondominio": "Rua ABC, 100",
              "nomeDoSindico": "Síndico A",
              "emailDoSindico": "sindico@condominio-a.com",
              "senhaDoSindico": "senha-segura-123",
              "telefoneDoSindico": "(00) 00000-0000"
            }
            """;

    // nome em branco, email fora do formato, senha curta...
    private static final String JSON_INVALIDO = """
            {
              "nomeDoCondominio": "",
              "enderecoDoCondominio": "Rua ABC, 100",
              "nomeDoSindico": "Síndico A",
              "emailDoSindico": "isto-nao-e-email",
              "senhaDoSindico": "curta"
            }
            """;

    @Autowired
    private MockMvc simuladorDeRequisicoes;

    @MockitoBean
    private CadastroDeCondominioServico cadastroDeCondominioServico;

    @Test
    void deveResponder201ENaoDevolverASenhaQuandoOsDadosSaoValidos() throws Exception {
        when(cadastroDeCondominioServico.cadastrar(any())).thenReturn(
                new CondominioCadastradoResposta(1L, "Condomínio A", 2L, "sindico@condominio-a.com"));

        simuladorDeRequisicoes.perform(post("/api/condominios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDoCondominio").value(1))
                .andExpect(jsonPath("$.emailDoSindico").value("sindico@condominio-a.com"))
                .andExpect(jsonPath("$.senhaDoSindico").doesNotExist());
    }

    @Test
    void deveResponder400ComOsErrosDeCadaCampoQuandoOsDadosSaoInvalidos() throws Exception {
        simuladorDeRequisicoes.perform(post("/api/condominios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_INVALIDO))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.nomeDoCondominio").exists())
                .andExpect(jsonPath("$.erros.emailDoSindico").exists())
                .andExpect(jsonPath("$.erros.senhaDoSindico").exists());


        verify(cadastroDeCondominioServico, never()).cadastrar(any());
    }

    @Test
    void deveResponder400QuandoOJsonEstaMalformado() throws Exception {
        simuladorDeRequisicoes.perform(post("/api/condominios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ isto não é json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveResponder409QuandoOEmailJaEstaCadastrado() throws Exception {
        when(cadastroDeCondominioServico.cadastrar(any()))
                .thenThrow(new EmailJaCadastradoExcecao("sindico@condominio-a.com"));

        simuladorDeRequisicoes.perform(post("/api/condominios").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.erros.emailDoSindico").exists());
    }
}