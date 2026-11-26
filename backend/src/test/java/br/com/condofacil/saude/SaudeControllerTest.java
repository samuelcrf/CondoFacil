package br.com.condofacil.saude;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import br.com.condofacil.seguranca.ConfiguracaoDeSeguranca;


@WebMvcTest(SaudeController.class)
@Import(ConfiguracaoDeSeguranca.class)
class SaudeControllerTest {

    @Autowired
    private MockMvc simuladorDeRequisicoes;

    @Test
    void deveResponderOnlineQuandoConsultarSaude() throws Exception {
        simuladorDeRequisicoes.perform(get("/api/saude"))
                
                .andExpect(status().isOk())
               
                .andExpect(jsonPath("$.situacao").value("ONLINE"))
                .andExpect(jsonPath("$.aplicacao").value("CondoFacil"));
    }
}