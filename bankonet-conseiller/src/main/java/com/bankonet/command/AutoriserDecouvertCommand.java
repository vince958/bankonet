package com.bankonet.command;

import java.util.ArrayList;
import java.util.List;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.others.InputSingleton;

public class AutoriserDecouvertCommand extends IhmCommand{

	private static final int id = 5;
	private static final String libelle = "Autoriser un decouvert";
	private ClientService clientService;
	private InputSingleton input = InputSingleton.getInstance();
	
	public AutoriserDecouvertCommand(ClientService pclientService) {
		clientService = pclientService;
	}
	
	@Override
	public void execute() {
		AjouterDecouvertAutorise();
	}
	
	public void AjouterDecouvertAutorise(){
		List<String[]> clientsString = clientService.getLibelleList();
		for(int i = 0; i < clientsString.size(); i++)
			System.out.println((i+1)+". "+clientsString.get(i)[0]);
		
		int numClient = input.readInt("Selectionnez un client: ", 0, clientsString.size()-1);
		
		Client client = clientService.getClient(clientsString.get(numClient-1)[1]);
		List<Compte> comptesList = client.getComptesList();
		ArrayList<Compte> comptesListCopie = new ArrayList<Compte>();
		for(Compte compte:comptesList)
			if(compte.getType().getValue().equals("Courant"))
				comptesListCopie.add(compte);

		for(int i = 0; i < comptesListCopie.size(); i++)
			System.out.println((i+1)+". "+comptesListCopie.get(i).getLibelle());
		
		int numCompte = input.readInt("Selectionnez un compte: ", 0, comptesListCopie.size()-1);
		double montant = input.readDouble("Montant (0 pour abandon): ", 0, 0);
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
