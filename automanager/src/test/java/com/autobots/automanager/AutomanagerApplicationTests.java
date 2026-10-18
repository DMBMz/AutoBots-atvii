package com.autobots.automanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AutomanagerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Contexto deve carregar com sucesso")
	void contextLoads() {
	}

	@Test
	@DisplayName("Deve obter lista de clientes com HATEOAS (Level 3)")
	void testObterClientes() throws Exception {
		mockMvc.perform(get("/clientes"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].links[?(@.rel == 'self')]").exists());
	}

	@Test
	@DisplayName("Deve obter um cliente por ID com links HATEOAS")
	void testObterClientePorId() throws Exception {
		mockMvc.perform(get("/cliente/1"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$._links.clientes.href").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	@DisplayName("Deve obter documentos com HATEOAS")
	void testObterDocumentos() throws Exception {
		mockMvc.perform(get("/documentos"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].links[?(@.rel == 'self')]").exists());
	}

	@Test
	@DisplayName("Deve obter documento por ID com HATEOAS")
	void testObterDocumentoPorId() throws Exception {
		mockMvc.perform(get("/documento/1"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$._links.documentos.href").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	@DisplayName("Deve obter documentos por ID do cliente")
	void testObterDocumentosPorCliente() throws Exception {
		mockMvc.perform(get("/documentos/1"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].links[?(@.rel == 'self')]").exists());
	}

	@Test
	@DisplayName("Deve obter endereços com HATEOAS")
	void testObterEnderecos() throws Exception {
		mockMvc.perform(get("/enderecos"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].links[?(@.rel == 'self')]").exists());
	}

	@Test
	@DisplayName("Deve obter endereço por ID com HATEOAS")
	void testObterEnderecoPorId() throws Exception {
		mockMvc.perform(get("/endereco/1"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$._links.enderecos.href").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	@DisplayName("Deve obter endereço por ID do cliente")
	void testObterEnderecoPorCliente() throws Exception {
		mockMvc.perform(get("/endereco/cliente/1"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$._links.enderecos.href").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	@DisplayName("Deve obter telefones com HATEOAS")
	void testObterTelefones() throws Exception {
		mockMvc.perform(get("/telefones"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].links[?(@.rel == 'self')]").exists());
	}

	@Test
	@DisplayName("Deve obter telefone por ID com HATEOAS")
	void testObterTelefonePorId() throws Exception {
		mockMvc.perform(get("/telefone/1"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$._links.telefones.href").exists())
				.andExpect(jsonPath("$._links.self.href").exists());
	}

	@Test
	@DisplayName("Deve obter telefones por ID do cliente")
	void testObterTelefonesPorCliente() throws Exception {
		mockMvc.perform(get("/telefones/1"))
				.andExpect(status().isFound())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[0].links[?(@.rel == 'self')]").exists());
	}

	@Test
	@DisplayName("Deve cadastrar cliente com status 201 CREATED")
	void testCadastrarCliente() throws Exception {
		String novoClienteJson = "{" +
				"\"nome\": \"Cliente Teste\"," +
				"\"nomeSocial\": \"Teste\"," +
				"\"telefones\": [{\"ddd\": \"11\", \"numero\": \"999999999\"}]" +
				"}";

		mockMvc.perform(post("/cliente/cadastro")
				.contentType(MediaType.APPLICATION_JSON)
				.content(novoClienteJson))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Deve retornar NOT_FOUND para cliente inexistente")
	void testClienteNaoEncontrado() throws Exception {
		mockMvc.perform(get("/cliente/9999"))
				.andExpect(status().isNotFound());
	}
}
