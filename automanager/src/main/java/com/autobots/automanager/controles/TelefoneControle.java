package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.modelos.TelefoneCadastrar;
import com.autobots.automanager.modelos.TelefoneRemovedor;
import com.autobots.automanager.modelos.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
public class TelefoneControle {

	@Autowired
	private TelefoneRepositorio repositorio;

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Autowired
	private TelefoneSelecionador selecionador;

	@Autowired
	private ClienteSelecionador clienteSelecionador;

	@Autowired
	private TelefoneCadastrar cadastrador;

	@Autowired
	private TelefoneRemovedor removedor;

	@Autowired
	private TelefoneAtualizador atualizador;

	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;

	@GetMapping("/telefone/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		List<Telefone> telefones = repositorio.findAll();
		Telefone telefone = selecionador.selecionar(telefones, id);
		if (telefone == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(telefone);
			return new ResponseEntity<>(telefone, HttpStatus.FOUND);
		}
	}

	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = repositorio.findAll();
		if (telefones.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(telefones);
			return new ResponseEntity<>(telefones, HttpStatus.FOUND);
		}
	}

	@GetMapping("/telefones/{clienteId}")
	public ResponseEntity<List<Telefone>> obterTelefonesPorCliente(@PathVariable long clienteId) {
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio.findAll(), clienteId);
		if (cliente == null || cliente.getTelefones().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(cliente.getTelefones());
			return new ResponseEntity<>(cliente.getTelefones(), HttpStatus.FOUND);
		}
	}

	@PostMapping("/telefone/cadastro/{clienteId}")
	public ResponseEntity<?> cadastrarTelefone(@PathVariable long clienteId, @RequestBody List<Telefone> telefones) {
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio.findAll(), clienteId);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		cadastrador.cadastro(cliente, telefones);
		clienteRepositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/telefone/atualizar")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (atualizacao.getId() != null) {
			Telefone telefone = repositorio.findById(atualizacao.getId()).orElse(null);
			if (telefone != null) {
				atualizador.atualizar(telefone, atualizacao);
				repositorio.save(telefone);
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/telefone/excluir")
	public ResponseEntity<?> excluirTelefone(@RequestBody Telefone exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (exclusao.getId() != null) {
			Telefone telefone = repositorio.findById(exclusao.getId()).orElse(null);
			if (telefone != null) {
				List<Cliente> clientes = clienteRepositorio.findAll();
				for (Cliente cliente : clientes) {
					removedor.remover(cliente, telefone);
					clienteRepositorio.save(cliente);
				}
				repositorio.delete(telefone);
				status = HttpStatus.OK;
			}
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/telefone/excluir/{id}")
	public ResponseEntity<?> excluirTelefonePorId(@PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Telefone telefone = repositorio.findById(id).orElse(null);
		if (telefone != null) {
			List<Cliente> clientes = clienteRepositorio.findAll();
			for (Cliente cliente : clientes) {
				removedor.remover(cliente, telefone);
				clienteRepositorio.save(cliente);
			}
			repositorio.delete(telefone);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
