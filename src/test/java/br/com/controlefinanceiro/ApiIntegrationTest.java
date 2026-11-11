package br.com.controlefinanceiro;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "app.seed-demo-data=false"
})
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    MockMvc mvc;

    private int criarCategoria(String nome, String tipo) throws Exception {
        String body = mvc.perform(post("/api/categories").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + nome + "\",\"type\":\"" + tipo + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    private String lancamento(String descricao, String valor, String tipo, int categoriaId) {
        return "{\"description\":\"" + descricao + "\",\"amount\":" + valor
                + ",\"date\":\"2026-11-05\",\"type\":\"" + tipo + "\",\"categoryId\":" + categoriaId + "}";
    }

    @Test
    void relatorioMensalSomaReceitasEDespesas() throws Exception {
        int salario = criarCategoria("Salário", "INCOME");
        int mercado = criarCategoria("Mercado", "EXPENSE");

        mvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON)
                        .content(lancamento("Salário", "3000.00", "INCOME", salario)))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON)
                        .content(lancamento("Compras", "450.50", "EXPENSE", mercado)))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/reports/monthly").param("month", "2026-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.income").value(3000.00))
                .andExpect(jsonPath("$.expense").value(450.50))
                .andExpect(jsonPath("$.balance").value(2549.50))
                .andExpect(jsonPath("$.expensesByCategory", hasSize(1)));
    }

    @Test
    void rejeitaLancamentoComCategoriaDeOutroTipo() throws Exception {
        int aluguel = criarCategoria("Aluguel", "EXPENSE");

        mvc.perform(post("/api/transactions").contentType(MediaType.APPLICATION_JSON)
                        .content(lancamento("Errado", "10", "INCOME", aluguel)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void validaCamposObrigatoriosDaMeta() throws Exception {
        mvc.perform(post("/api/goals").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
