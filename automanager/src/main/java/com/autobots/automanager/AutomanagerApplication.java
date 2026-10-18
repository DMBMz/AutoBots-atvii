package com.autobots.automanager;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@SpringBootApplication
public class AutomanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Component
	public static class Runner implements ApplicationRunner {
		@Autowired
		public ClienteRepositorio repositorio;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			
			System.out.println("EasterEgg: Oi Prof. Gerson :) ");
			
			Calendar calendario = Calendar.getInstance();
			calendario.set(2002, 05, 15);

			Cliente cliente = new Cliente();
			cliente.setNome("Rogério Mücke Ceni");
			cliente.setDataCadastro(Calendar.getInstance().getTime());
			cliente.setDataNascimento(calendario.getTime());
			cliente.setNomeSocial("Rogério Ceni");
			Telefone telefone = new Telefone();
			telefone.setDdd("11");
			telefone.setNumero("123456789");
			cliente.getTelefones().add(telefone);
			
			Endereco endereco = new Endereco();
			endereco.setEstado("Bahia");
			endereco.setCidade("Camaçari");
			endereco.setBairro("Catu de Abrantes");
			endereco.setRua("Estrada do Coco");
			endereco.setNumero("123");
			endereco.setCodigoPostal("42825901");
			endereco.setInformacoesAdicionais("Condomínio Busca Vida");
			cliente.setEndereco(endereco);
			
			Documento rg = new Documento();
			rg.setTipo("RG");
			rg.setNumero("131");
			
			Documento cpf = new Documento();
			cpf.setTipo("CPF");
			cpf.setNumero("00000000001");
			
			cliente.getDocumentos().add(rg);
			cliente.getDocumentos().add(cpf);
			
			repositorio.save(cliente);
		}
	}

}
