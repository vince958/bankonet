package com.bankonet.command;

import java.util.List;
import java.util.Scanner;

import com.bankonet.metier.InitService;
import com.bankonet.utils.Client;

public class RechercherCommand extends IhmCommand{

	private static final int id = 7;
	private static final String libelle = "Rechercher un client";
	private InitService initService;
	private Scanner input;
	
	public RechercherCommand(InitService pinitService, Scanner pinput) {
		initService = pinitService;
		input = pinput;
	}
	
	@Override
	public void execute() {
		System.out.println("Entrez le nom d'un client: ");
		String nom = input.nextLine();
		System.out.println("Entrez le prenom d'un client: ");
		String prenom = input.nextLine();
		
		List<Client> clients = initService.rechercherClient(nom, prenom);
		for(Client client:clients)
			System.out.println("Login: "+client.getLogin()+" - Civilite:"+client.getCivilite()+" - Nom: "+client.getNom()+" - Prenom: "+client.getPrenom());
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
