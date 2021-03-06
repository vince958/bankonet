package com.bankonet.command;

import java.util.List;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;
import com.bankonet.utils.exception.CompteException;
import com.bankonet.utils.others.InputSingleton;

public class RetraitCommand extends IhmCommand{

	private static final int id = 3;
	private static final String libelle = "Effectuer un retrait";
	private ClientService clientService;
	private String login;
	private InputSingleton input = InputSingleton.getInstance();
	
	public RetraitCommand(ClientService pclientService, String plogin) {
		clientService = pclientService;
		login = plogin;
	}
	
	@Override
	public void execute() {
		Client client = clientService.getClient(login);
		effectuerDepotRetrait(client, false);
		System.out.println(client.consulterComptes());
	}
	
	public void effectuerDepotRetrait(Client client, boolean isDepot){
		List<Compte> comptesList = client.getComptesList();
		for(int i = 0; i < comptesList.size(); i++)
			System.out.println((i+1)+". "+comptesList.get(i).getLibelle());
		
		int num = input.readInt("Selectionnez un compte: ", 0, comptesList.size()-1);

		String type = comptesList.get(num-1).getType().getValue();
		boolean testRetrait = false;
		double montant = 0;
		double debitMax = 0;
		if(type.equals("Courant")) debitMax = ((CompteCourant)comptesList.get(num-1)).calculerDebitMaximum();
		else if(type.equals("Epargne")) debitMax = ((CompteEpargne)comptesList.get(num-1)).calculerDebitMaximum();
		do{
			montant = input.readDouble("Montant (0 pour abandon): ", 0, 0);
			testRetrait = montant > debitMax;
			if(testRetrait && !isDepot && montant != 0)
				System.out.println("Debit impossible, retrait maximum atteind!");
		}while(testRetrait && !isDepot && montant!=0);
		
		if(montant != 0){
			if(type.equals("Courant"))
				try {
					if(isDepot)((CompteCourant)comptesList.get(num-1)).crediter(montant);
					else ((CompteCourant)comptesList.get(num-1)).debiter(montant);
				} catch (CompteException e) {
					e.printStackTrace();
				}
			else if(type.equals("Epargne"))
				try {
					if(isDepot)((CompteEpargne)comptesList.get(num-1)).crediter(montant);
					else ((CompteEpargne)comptesList.get(num-1)).debiter(montant);
				} catch (CompteException e) {
					e.printStackTrace();
				}
			clientService.ajouterModifier(client);
		}
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
