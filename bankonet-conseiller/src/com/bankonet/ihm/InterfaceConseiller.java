package com.bankonet.ihm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bankonet.dao.DaoFactory;
import com.bankonet.dao.DaoFactorySQL;
import com.bankonet.metier.ClientService;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;
import com.bankonet.utils.exception.TypeException;
import com.bankonet.utils.others.Civilite;

public class InterfaceConseiller {
	
	private ClientService clientService;	
	
	public InterfaceConseiller(DaoFactory factory){
		clientService = new ClientService(factory.getClientDao(), factory.getCompteDao());		
	}
	
	public void menu(){
		while(true){
			System.out.print( 
					"\n***** APPLICATION CONSEILLER BANCAIRE *****\n\n"+
					"0. Arreter le programme\n"+
					"1. Ouvrir un compte courant\n"+
					"2. Lister tous les clients\n"+
					"3. Ajouter un compte courant\n"+
					"4. Ajouter un compte epargne\n"+
					"5. Autoriser un decouvert\n\n"+
					"Action: "
					);
			
			Scanner input = new Scanner(System.in);
			
			switch(input.nextInt()){
				case 0:
					System.out.println("Arret de l'application");
					System.exit(0);
					break;
					
				case 1:
					try {
						ouvrirCompte(input);
					} catch (TypeException e) {
						e.printStackTrace();
					}
					break;
					
				case 2:
					List<String[]> clients = clientService.getLibelleList();
					for(String[] client:clients)
						System.out.print(client[0]);
					break;
					
				case 3:
					AjouterCompteCourantEpargne(true, input);
					break;
					
				case 4:
					AjouterCompteCourantEpargne(false, input);
					break;
					
				case 5:
					AjouterDecouvertAutorise(input);
					break;
					
				default:
					System.out.println("Veuillez entrer une action valide!");
					break;
			}
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
	
	public void AjouterCompteCourantEpargne(boolean isCourant, Scanner input){
		List<String[]> clientsString = clientService.getLibelleList();
		for(int i = 0; i < clientsString.size(); i++)
			System.out.println((i+1)+". "+clientsString.get(i)[0]);
		System.out.println("Selectionnez un client: ");
		int num = input.nextInt();
		input.nextLine();
		
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
	
	public void AjouterDecouvertAutorise(Scanner input){
		List<String[]> clientsString = clientService.getLibelleList();
		for(int i = 0; i < clientsString.size(); i++)
			System.out.println((i+1)+". "+clientsString.get(i)[0]);
		
		System.out.println("Selectionnez un client: ");
		int numClient = input.nextInt();
		input.nextLine();
		
		Client client = clientService.getClient(clientsString.get(numClient-1)[1]);
		ArrayList<Compte> comptesList = client.getComptesList();
		ArrayList<Compte> comptesListCopie = new ArrayList<Compte>();
		for(Compte compte:comptesList)
			if(compte.getType().equals("Courant"))
				comptesListCopie.add(compte);

		for(int i = 0; i < comptesListCopie.size(); i++)
			System.out.println((i+1)+". "+comptesListCopie.get(i).getLibelle());
		
		System.out.println("Selectionnez un compte: ");
		int numCompte = input.nextInt();
		input.nextLine();
		
		System.out.println("Montant (0 pour abandon): ");
		double montant = input.nextDouble();
		input.nextLine();
		
		((CompteCourant)comptesListCopie.get(numCompte-1)).setDecouvertAutorise(montant);
		clientService.ajouterModifier(client);
	}
	
	public static void main(String[] args) {
		//DaoFactory factory = new DaoFactoryFile("../bankonet-lib/clients.properties", "../bankonet-lib/comptes.properties");
		DaoFactory factory = new DaoFactorySQL("jdbc:mysql://localhost/bankonet", "root", "poupette");
		InterfaceConseiller fenetre = new InterfaceConseiller(factory);
		fenetre.menu();
	}

}
