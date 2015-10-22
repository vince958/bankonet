package com.bankonet.command;

import java.util.List;

import com.bankonet.metier.InitService;
import com.bankonet.utils.Client;
import com.bankonet.utils.others.InputSingleton;

public class RechercherCommand extends IhmCommand{

	private static final int id = 7;
	private static final String libelle = "Rechercher un client";
	private InitService initService;
	private InputSingleton input = InputSingleton.getInstance();
	
	public RechercherCommand(InitService pinitService) {
		initService = pinitService;
	}
	
	@Override
	public void execute() {
		String nom = input.readString("Entrez le nom d'un client: ");
		String prenom = input.readString("Entrez le prenom d'un client: ");
		
		List<Client> clients = initService.rechercherClient(nom, prenom);
		for(Client client:clients)
			System.out.println("Login: "+client.getLogin()+" - Civilite:"+client.getCivilite()+" - Nom: "+client.getNom()+" - Prenom: "+client.getPrenom());
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
