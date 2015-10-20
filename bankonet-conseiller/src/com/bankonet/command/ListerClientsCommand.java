package com.bankonet.command;

import java.util.List;

import com.bankonet.metier.ClientService;

public class ListerClientsCommand implements IhmCommand{

	private static final int id = 2;
	private static final String libelle = "Lister tous les clients";
	private ClientService clientService;
	
	public ListerClientsCommand(ClientService pclientService) {
		clientService = pclientService;
	}
	
	@Override
	public void execute() {
		List<String[]> clients = clientService.getLibelleList();
		for(String[] client:clients)
			System.out.print(client[0]);
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
