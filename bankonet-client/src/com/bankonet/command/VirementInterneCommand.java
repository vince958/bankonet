package com.bankonet.command;

import java.util.List;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;
import com.bankonet.utils.exception.CompteException;
import com.bankonet.utils.others.InputSingleton;

public class VirementInterneCommand extends IhmCommand {

	private static final int id = 4;
	private static final String libelle = "Effectuer un virement";
	private ClientService clientService;
	private String login;
	private InputSingleton input = InputSingleton.getInstance();
	
	public VirementInterneCommand(ClientService pclientService, String plogin) {
		clientService = pclientService;
		login = plogin;
	}
	
	@Override
	public void execute() {
		Client client = clientService.getClient(login);
		effectuerVirementInterne(client);
		System.out.println(client.consulterComptes());
	}
	
	public void effectuerVirementInterne(Client client){
		List<Compte> comptesList = client.getComptesList();
		for(int i = 0; i < comptesList.size(); i++)
			System.out.println((i+1)+". "+comptesList.get(i).getLibelle());
		
		int numDebiteur = input.readInt("Selectionnez un compte a debiter: ", 0, comptesList.size()-1);
		int numCrediteur = input.readInt("Selectionnez un compte a crediter: ", 0, comptesList.size()-1);
		String typeDebiteur = comptesList.get(numDebiteur-1).getType().getValue();
		
		boolean testRetrait = false;
		double montant = 0;
		double debitMax = 0;
		if(typeDebiteur.equals("Courant")) debitMax = ((CompteCourant)comptesList.get(numDebiteur-1)).calculerDebitMaximum();
		else if(typeDebiteur.equals("Epargne")) debitMax = ((CompteEpargne)comptesList.get(numDebiteur-1)).calculerDebitMaximum();
		do{
			montant = input.readDouble("Montant (0 pour abandon): ", 0, 0);
			testRetrait = montant > debitMax;
			if(testRetrait && montant != 0)
				System.out.println("Debit impossible, retrait maximum atteind!");
		}while(testRetrait && montant!=0);
		
		if(montant != 0){
			try {
				comptesList.get(numDebiteur-1).effectuerVirement(comptesList.get(numCrediteur-1), montant);
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
