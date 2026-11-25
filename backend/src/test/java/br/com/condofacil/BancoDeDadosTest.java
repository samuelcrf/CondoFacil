package br.com.condofacil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("teste")
class BancoDeDadosTest {

    @Autowired
    private JdbcTemplate executorDeComandosSql;

    @Test
    void deveConectarNoPostgreSql() {
        Integer resultadoDaConsulta = executorDeComandosSql.queryForObject("SELECT 1", Integer.class);

        assertEquals(1, resultadoDaConsulta);
    }

    @Test
    void deveTerExecutadoOFlyway() {
        
        Boolean tabelaDeHistoricoExiste = executorDeComandosSql.queryForObject(
                "SELECT EXISTS (SELECT FROM information_schema.tables "
                        + "WHERE table_name = 'flyway_schema_history')",
                Boolean.class);

        assertTrue(tabelaDeHistoricoExiste);
    }
}
