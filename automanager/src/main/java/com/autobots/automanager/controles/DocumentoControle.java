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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.DocumentoCadastrador;
import com.autobots.automanager.modelos.DocumentoRemovedor;
import com.autobots.automanager.modelos.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
public class DocumentoControle {

	@Autowired
	private DocumentoRepositorio repositorio;

	@Autowired
	private ClienteRepositorio clienteRepositorio;

	@Autowired
	private DocumentoSelecionador selecionador;

	@Autowired
	private ClienteSelecionador clienteSelecionador;

	@Autowired
	private DocumentoCadastrador cadastrador;

	@Autowired
	private DocumentoRemovedor removedor;

	@Autowired
	private DocumentoAtualizador atualizador;

	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;

	@GetMapping("/documento/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		List<Documento> documentos = repositorio.findAll();
		Documento documento = selecionador.selecionar(documentos, id);
		if (documento == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(documento);
			return new ResponseEntity<>(documento, HttpStatus.FOUND);
		}
	}

	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		if (documentos.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(documentos);
			return new ResponseEntity<>(documentos, HttpStatus.FOUND);
		}
	}

	@GetMapping("/documentos/{clienteId}")
	public ResponseEntity<List<Documento>> obterDocumentosPorCliente(@PathVariable long clienteId) {
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio.findAll(), clienteId);
		if (cliente == null || cliente.getDocumentos().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(cliente.getDocumentos());
			return new ResponseEntity<>(cliente.getDocumentos(), HttpStatus.FOUND);
		}
	}

	@PostMapping("/documento/cadastro/{clienteId}")
	public ResponseEntity<?> cadastrarDocumento(@PathVariable long clienteId, @RequestBody List<Documento> documentos) {
		Cliente cliente = clienteSelecionador.selecionar(clienteRepositorio.findAll(), clienteId);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		cadastrador.cadastro(cliente, documentos);
		clienteRepositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("/documento/atualizar")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (atualizacao.getId() != null) {
			Documento documento = repositorio.findById(atualizacao.getId()).orElse(null);
			if (documento != null) {
				atualizador.atualizar(documento, atualizacao);
				repositorio.save(documento);
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/documento/excluir")
	public ResponseEntity<?> excluirDocumento(@RequestBody Documento exclusao) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if (exclusao.getId() != null) {
			Documento documento = repositorio.findById(exclusao.getId()).orElse(null);
			if (documento != null) {
				List<Cliente> clientes = clienteRepositorio.findAll();
				for (Cliente cliente : clientes) {
					removedor.remover(cliente, documento);
					clienteRepositorio.save(cliente);
				}
				repositorio.delete(documento);
				status = HttpStatus.OK;
			}
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/documento/excluir/{id}")
	public ResponseEntity<?> excluirDocumentoPorId(@PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Documento documento = repositorio.findById(id).orElse(null);
		if (documento != null) {
			List<Cliente> clientes = clienteRepositorio.findAll();
			for (Cliente cliente : clientes) {
				removedor.remover(cliente, documento);
				clienteRepositorio.save(cliente);
			}
			repositorio.delete(documento);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
