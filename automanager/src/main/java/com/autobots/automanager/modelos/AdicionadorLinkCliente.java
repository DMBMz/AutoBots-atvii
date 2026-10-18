package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

	@Autowired
	@Lazy
	private AdicionadorLinkDocumento adicionadorLinkDocumento;

	@Autowired
	@Lazy
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@Autowired
	@Lazy
	private AdicionadorLinkTelefone adicionadorLinkTelefone;

	@Override
	public void adicionarLink(List<Cliente> lista) {
		for (Cliente cliente : lista) {
			long id = cliente.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ClienteControle.class)
							.obterCliente(id))
					.withSelfRel();
			cliente.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(Cliente objeto) {
		Link linkColecao = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterClientes())
				.withRel("clientes");
		objeto.add(linkColecao);

		if (objeto.getId() != null) {
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ClienteControle.class)
							.obterCliente(objeto.getId()))
					.withSelfRel();
			objeto.add(linkProprio);
		}

		if (objeto.getDocumentos() != null && !objeto.getDocumentos().isEmpty() && adicionadorLinkDocumento != null) {
			adicionadorLinkDocumento.adicionarLink(objeto.getDocumentos());
		}
		if (objeto.getEndereco() != null && adicionadorLinkEndereco != null) {
			adicionadorLinkEndereco.adicionarLink(objeto.getEndereco());
		}
		if (objeto.getTelefones() != null && !objeto.getTelefones().isEmpty() && adicionadorLinkTelefone != null) {
			adicionadorLinkTelefone.adicionarLink(objeto.getTelefones());
		}
	}
}
