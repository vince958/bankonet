package com.bankonet.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;

public class AutoriserDecouvertCommand extends IhmCommand{

	private static final int id = 5;
	private static final String libelle = "Autoriser un decouvert";
	private ClientService clientService;
	private Scanner input;
	
	public AutoriserDecouvertCommand(ClientService pclientService, Scanner pinput) {
		clientService = pclientService;
		input = pinput;
	}
	
	@Override
	public void execute() {
		AjouterDecouvertAutorise(input);
	}
	
	public void AjouterDecouvertAutorise(Scanner input){
		List<String[]> clientsString = clientService.getLibelleList();
		for(int i = 0; i < clientsString.size(); i++)
			System.out.println((i+1)+". "+clientsString.get(i)[0]);
		
		System.out.println("Selectionnez un client: ");
		int numClient = input.nextInt();
		input.nextLine();
		
		Client client = clientService.getClient(clientsString.get(numClient-1)[1]);
		List<Compte> comptesList = client.getComptesList();
		ArrayList<Compte> comptesListCopie = new ArrayList<Compte>();
		for(Compte compte:comptesList)
			if(compte.getType().getValue().equals("Courant"))
				comptesListCopie.add(compte);

		for(int i = 0; i < comptesListCopie.size(); i++)
			System.out.println((i+1)+". "+comptesListCopie.get(i).getLibelle());
		
		System.out.println("Selectionnez un compte: ");
		int numCompte = input.nextInt();
		input.nextLine();
		
		System.out.println("Montant (0 pour abandon): ");
		double montant = input.nextDouble();
		input.nextLine();
		if(montant > 0){
			((CompteCourant)comptesListCopie.get(numCompte-1)).setMontantDecouvertAutorise(montant);
			clientService.ajouterModifier(client);
		}
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
