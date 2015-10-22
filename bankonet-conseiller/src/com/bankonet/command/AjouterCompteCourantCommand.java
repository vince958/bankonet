package com.bankonet.command;

import java.util.List;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;
import com.bankonet.utils.others.InputSingleton;

public class AjouterCompteCourantCommand extends IhmCommand{

	private static final int id = 3;
	private static final String libelle = "Ajouter un compte courant";
	private ClientService clientService;
	private InputSingleton input = InputSingleton.getInstance();
	
	public AjouterCompteCourantCommand(ClientService pclientService) {
		clientService = pclientService;
	}
	
	@Override
	public void execute() {
		AjouterCompteCourantEpargne(true);
	}
	
	public void AjouterCompteCourantEpargne(boolean isCourant){
		List<String[]> clientsString = clientService.getLibelleList();
		for(int i = 0; i < clientsString.size(); i++)
			System.out.println((i+1)+". "+clientsString.get(i)[0]);
		int num = input.readInt("Selectionnez un client: ", 0, clientsString.size()-1);
		
		Client client = clientService.getClient(clientsString.get(num-1)[1]);
		if(isCourant){
			CompteCourant compte = new CompteCourant(client.getNom(), client.getPrenom(), 50.0d, 500.0d);
			client.creerCompte(compte);
		}else{
			CompteEpargne compte = new CompteEpargne(client.getNom(), client.getPrenom(), 50.0d, 2.5d);
			client.creerCompte(compte);
		}
		clientService.ajouterModifier(client);
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
