package com.bankonet.command;

import java.util.Scanner;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.exception.TypeException;
import com.bankonet.utils.others.Civilite;

public class OuvrirCompteCourantCommand implements IhmCommand{

	private static final int id = 1;
	private static final String libelle = "Ouvrir un compte courant";
	private ClientService clientService;
	private Scanner input;
	
	public OuvrirCompteCourantCommand(ClientService pclientService, Scanner pinput) {
		clientService = pclientService;
		input = pinput;
	}
	
	@Override
	public void execute() {
		try {
			ouvrirCompte(input);
		} catch (TypeException e) {
			e.printStackTrace();
		}
	}
	
public void ouvrirCompte(Scanner input) throws TypeException{
		
		System.out.println("Entrez la civilite(1.Mr 2.Mme 3.Mlle): ");
		int temp = input.nextInt();
		Civilite civilite;
		if(temp == 1) civilite = Civilite.MONSIEUR;
		else if (temp == 2) civilite = Civilite.MADAME;
		else if (temp == 3) civilite = Civilite.MADEMOISELLE;
		else throw new TypeException("Type inconnu!");
		
		input.nextLine();
		System.out.println("Entrez le nom du client: ");
		String nom = input.nextLine();
		
		System.out.println("Entrez le prenom du client: ");
		String prenom = input.nextLine();
		
		System.out.println("Entrez le login du client: ");
		String login = input.nextLine();

		System.out.println("Entrez le mdp du client: ");
		String mdp = input.nextLine();
		
		Client client = new Client(login, mdp, civilite, nom, prenom);
		CompteCourant compte = new CompteCourant(nom, prenom, 50.0d, 500.0d);
		
		client.creerCompte(compte);
		clientService.ajouterModifier(client);
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
