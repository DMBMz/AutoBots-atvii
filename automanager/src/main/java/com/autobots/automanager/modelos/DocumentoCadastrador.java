package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;

@Component
public class DocumentoCadastrador {
	private StringVerificadorNulo verificadorNulo = new StringVerificadorNulo();

	public void cadastro(Cliente cliente, Documento documento) {
		if (documento != null) {
			if (!verificadorNulo.verificar(documento.getTipo()) && !verificadorNulo.verificar(documento.getNumero())) {
				Documento documentoNovo = new Documento();
				documentoNovo.setTipo(documento.getTipo());
				documentoNovo.setNumero(documento.getNumero());
				cliente.getDocumentos().add(documentoNovo);
			}
		}
	}

	public void cadastro(Cliente cliente, List<Documento> documentos) {
		for (Documento documento : documentos) {
			cadastro(cliente, documento);
		}
	}
}
