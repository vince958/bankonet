package com.bankonet.ihm;

import java.util.ArrayList;
import java.util.Scanner;

import com.bankonet.dao.DaoFactory;
import com.bankonet.dao.DaoFactorySQL;
import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;
import com.bankonet.utils.exception.CompteException;

public class InterfaceClient {
	
	private ClientService clientService;	
	
	public InterfaceClient(DaoFactory factory){
		clientService = new ClientService(factory.getClientDao(), factory.getCompteDao());		
	}
	
	public void menu(String login, Scanner input){
		while(true){
			System.out.print( 
					"\n***** APPLICATION CLIENT *****\n\n"+
					"0. Arreter le programme\n"+
					"1. Consulter les soldes des comptes\n"+
					"2. Effectuer un depot\n"+
					"3. Effectuer un retrait\n"+
					"4. Effectuer un virement\n\n"+
					"Action: "
					);
			
			Client client = null;
			switch(input.nextInt()){
				case 0:
					System.out.println("Arret de l'application");
					System.exit(0);
					break;
					
				case 1:
					client = clientService.getClient(login);
					System.out.println(client.consulterComptes());
					break;
					
				case 2:
					client = clientService.getClient(login);
					effectuerDepotRetrait(client, true, input);
					System.out.println(client.consulterComptes());
					break;
					
				case 3:
					client = clientService.getClient(login);
					effectuerDepotRetrait(client, false, input);
					System.out.println(client.consulterComptes());
					break;
					
				case 4:
					client = clientService.getClient(login);
					effectuerVirementInterne(client, input);
					System.out.println(client.consulterComptes());
					break;
					
				default:
					System.out.println("Veuillez entrer une action valide!");
					break;
			}
		}
	}
	
	public void ouvrirSession(){
		Scanner input = new Scanner(System.in);
		boolean state = true;
		while(state){
			System.out.println("Login: ");
			String login = input.nextLine();
			
			System.out.println("Mot de Passe: ");
			String mdp = input.nextLine();
			
			if(clientService.connexionClient(login, mdp)){
				state = false;
				menu(login, input);
			}else
				System.out.println("Login ou mdp incorrect!");
		}
	}
	
	public void effectuerDepotRetrait(Client client, boolean isDepot, Scanner input){
		ArrayList<Compte> comptesList = client.getComptesList();
		for(int i = 0; i < comptesList.size(); i++)
			System.out.println((i+1)+". "+comptesList.get(i).getLibelle());
		
		System.out.println("Selectionnez un compte: ");
		int num = input.nextInt();
		input.nextLine();

		String type = comptesList.get(num-1).getType();
		boolean testRetrait = false;
		double montant = 0;
		double debitMax = 0;
		if(type.equals("Courant")) debitMax = ((CompteCourant)comptesList.get(num-1)).calculerDebitMaximum();
		else if(type.equals("Epargne")) debitMax = ((CompteEpargne)comptesList.get(num-1)).calculerDebitMaximum();
		do{
			System.out.println("Montant (0 pour abandon): ");
			montant = input.nextDouble();
			input.nextLine();
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
	
	public void effectuerVirementInterne(Client client, Scanner input){
		ArrayList<Compte> comptesList = client.getComptesList();
		for(int i = 0; i < comptesList.size(); i++)
			System.out.println((i+1)+". "+comptesList.get(i).getLibelle());
		
		System.out.println("Selectionnez un compte a debiter: ");
		int numDebiteur = input.nextInt();
		input.nextLine();
		
		System.out.println("Selectionnez un compte a crediter: ");
		int numCrediteur = input.nextInt();
		input.nextLine();
		String typeDebiteur = comptesList.get(numDebiteur-1).getType();
		
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
	
	public static void main(String[] args) {
		//DaoFactory factory = new DaoFactoryFile("../bankonet-lib/clients.properties", "../bankonet-lib/comptes.properties");
		DaoFactory factory = new DaoFactorySQL("jdbc:mysql://localhost/bankonet", "root", "poupette");
		InterfaceClient fenetre = new InterfaceClient(factory);
		fenetre.ouvrirSession();
	}

}
