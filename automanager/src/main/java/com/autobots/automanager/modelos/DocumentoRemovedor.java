package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;

@Component
public class DocumentoRemovedor {

	public void remover(Cliente cliente, Documento documento) {
		if (documento != null && documento.getId() != null) {
			cliente.getDocumentos().removeIf(d -> d.getId() != null && d.getId().equals(documento.getId()));
		}
	}

	public void remover(Cliente cliente, List<Documento> documentos) {
		for (Documento doc : documentos) {
			remover(cliente, doc);
		}
	}
}
