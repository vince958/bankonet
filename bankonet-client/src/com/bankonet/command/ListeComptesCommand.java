package com.bankonet.command;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;

public class ListeComptesCommand extends IhmCommand{

	private static final int id = 1;
	private static final String libelle = "Consulter les soldes des comptes";
	private ClientService clientService;
	private String login;
	
	public ListeComptesCommand(ClientService pclientService, String plogin) {
		clientService = pclientService;
		login = plogin;
	}
	
	@Override
	public void execute() {
		Client client = clientService.getClient(login);
		System.out.println(client.consulterComptes());
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
