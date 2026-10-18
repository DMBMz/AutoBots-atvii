package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;

@Component
public class TelefoneRemovedor {

	public void remover(Cliente cliente, Telefone telefone) {
		if (telefone != null && telefone.getId() != null) {
			cliente.getTelefones().removeIf(t -> t.getId() != null && t.getId().equals(telefone.getId()));
		}
	}

	public void remover(Cliente cliente, List<Telefone> telefones) {
		for (Telefone tel : telefones) {
			remover(cliente, tel);
		}
	}
}
