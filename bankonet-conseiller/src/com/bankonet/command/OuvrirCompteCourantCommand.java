package com.bankonet.command;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.exception.TypeException;
import com.bankonet.utils.others.Civilite;
import com.bankonet.utils.others.InputSingleton;

public class OuvrirCompteCourantCommand extends IhmCommand{

	private static final int id = 1;
	private static final String libelle = "Ouvrir un compte courant";
	private ClientService clientService;
	private InputSingleton input = InputSingleton.getInstance();
	
	public OuvrirCompteCourantCommand(ClientService pclientService) {
		clientService = pclientService;
	}
	
	@Override
	public void execute() {
		try {
			ouvrirCompte();
		} catch (TypeException e) {
			e.printStackTrace();
		}
	}
	
	public void ouvrirCompte() throws TypeException{
		
		int temp = input.readInt("Entrez la civilite(1.Mr 2.Mme 3.Mlle): ", 1, 3);
		Civilite civilite;
		if(temp == 1) civilite = Civilite.MONSIEUR;
		else if (temp == 2) civilite = Civilite.MADAME;
		else if (temp == 3) civilite = Civilite.MADEMOISELLE;
		else throw new TypeException("Type inconnu!");
		
		String nom = input.readString("Entrez le nom du client: ");
		String prenom = input.readString("Entrez le prenom du client: ");
		String login = input.readString("Entrez le login du client: ");
		String mdp = input.readString("Entrez le mdp du client: ");
		
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
