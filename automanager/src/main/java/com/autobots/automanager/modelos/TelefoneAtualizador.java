package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Telefone;

@Component
public class TelefoneAtualizador {
	private StringVerificadorNulo verificadorNulo = new StringVerificadorNulo();

	public void atualizar(Telefone telefone, Telefone telefoneNovo) {
		if (telefoneNovo != null) {
			if (!verificadorNulo.verificar(telefoneNovo.getDdd())) {
				telefone.setDdd(telefoneNovo.getDdd());
			}
			if (!verificadorNulo.verificar(telefoneNovo.getNumero())) {
				telefone.setNumero(telefoneNovo.getNumero());
			}
		}
	}

	public void atualizar(List<Telefone> telefones, List<Telefone> telefonesNovos) {
		for (Telefone telefone : telefones) {
			for (Telefone telefoneNovo : telefonesNovos) {
				if (telefone.getId() != null && telefoneNovo.getId() != null && telefone.getId().equals(telefoneNovo.getId())) {
					atualizar(telefone, telefoneNovo);
				}
			}
		}
	}
}
