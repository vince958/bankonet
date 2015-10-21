package com.bankonet.command;

import java.util.List;
import java.util.Scanner;

import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;
import com.bankonet.utils.exception.CompteException;

public class VirementInterneCommand extends IhmCommand {

	private static final int id = 4;
	private static final String libelle = "Effectuer un virement";
	private ClientService clientService;
	private String login;
	private Scanner input;
	
	public VirementInterneCommand(ClientService pclientService, String plogin, Scanner pinput) {
		clientService = pclientService;
		login = plogin;
		input = pinput;
	}
	
	@Override
	public void execute() {
		Client client = clientService.getClient(login);
		effectuerVirementInterne(client, input);
		System.out.println(client.consulterComptes());
	}
	
	public void effectuerVirementInterne(Client client, Scanner input){
		List<Compte> comptesList = client.getComptesList();
		for(int i = 0; i < comptesList.size(); i++)
			System.out.println((i+1)+". "+comptesList.get(i).getLibelle());
		
		System.out.println("Selectionnez un compte a debiter: ");
		int numDebiteur = input.nextInt();
		input.nextLine();
		
		System.out.println("Selectionnez un compte a crediter: ");
		int numCrediteur = input.nextInt();
		input.nextLine();
		String typeDebiteur = comptesList.get(numDebiteur-1).getType().getValue();
		
		boolean testRetrait = false;
		double montant = 0;
		double debitMax = 0;
		if(typeDebiteur.equals("Courant")) debitMax = ((CompteCourant)comptesList.get(numDebiteur-1)).calculerDebitMaximum();
		else if(typeDebiteur.equals("Epargne")) debitMax = ((CompteEpargne)comptesList.get(numDebiteur-1)).calculerDebitMaximum();
		do{
			System.out.println("Montant (0 pour abandon): ");
			montant = input.nextDouble();
			input.nextLine();
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
