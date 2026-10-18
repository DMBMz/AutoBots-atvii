package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.modelos.EnderecoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
public class EnderecoControle {

	@Autowired
	private EnderecoRepositorio repositorio;

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Autowired
	private EnderecoSelecionador selecionador;

	@Autowired
	private ClienteSelecionador clienteSelecionador;

	@Autowired
	private EnderecoAtualizador atualizador;

	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;

	@GetMapping("/endereco/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
		List<Endereco> enderecos = repositorio.findAll();
		Endereco endereco = selecionador.selecionar(enderecos, id);
		if (endereco == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(endereco);
			return new ResponseEntity<>(endereco, HttpStatus.FOUND);
		}
	}

	@GetMapping("/enderecos")
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		if (enderecos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(enderecos);
			return new ResponseEntity<>(enderecos, HttpStatus.FOUND);
		}
	}

	@GetMapping("/endereco/cliente/{clienteId}")
	public ResponseEntity<Endereco> obterEnderecoPorCliente(@PathVariable long clienteId) {
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio.findAll(), clienteId);
		if (cliente == null || cliente.getEndereco() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(cliente.getEndereco());
			return new ResponseEntity<>(cliente.getEndereco(), HttpStatus.FOUND);
		}
	}

	@PutMapping("/endereco/atualizar")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (atualizacao.getId() != null) {
			Endereco endereco = repositorio.findById(atualizacao.getId()).orElse(null);
			if (endereco != null) {
				atualizador.atualizar(endereco, atualizacao);
				repositorio.save(endereco);
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/endereco/atualizar/{clienteId}")
	public ResponseEntity<?> atualizarEnderecoCliente(@PathVariable long clienteId, @RequestBody Endereco atualizacao) {
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio.findAll(), clienteId);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		if (cliente.getEndereco() == null) {
			cliente.setEndereco(atualizacao);
		} else {
			atualizador.atualizar(cliente.getEndereco(), atualizacao);
		}
		clienteRepositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/endereco/excluir")
	public ResponseEntity<?> excluirEndereco(@RequestBody Endereco exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (exclusao.getId() != null) {
			Endereco endereco = repositorio.findById(exclusao.getId()).orElse(null);
			if (endereco != null) {
				List<Cliente> clientes = clienteRepositorio.findAll();
				for (Cliente cliente : clientes) {
					if (cliente.getEndereco() != null && cliente.getEndereco().getId().equals(endereco.getId())) {
						cliente.setEndereco(null);
						clienteRepositorio.save(cliente);
					}
				}
				repositorio.delete(endereco);
				status = HttpStatus.OK;
			}
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/endereco/excluir/{id}")
	public ResponseEntity<?> excluirEnderecoPorId(@PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Endereco endereco = repositorio.findById(id).orElse(null);
		if (endereco != null) {
			List<Cliente> clientes = clienteRepositorio.findAll();
			for (Cliente cliente : clientes) {
				if (cliente.getEndereco() != null && cliente.getEndereco().getId().equals(endereco.getId())) {
					cliente.setEndereco(null);
					clienteRepositorio.save(cliente);
				}
			}
			repositorio.delete(endereco);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
