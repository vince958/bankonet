package com.bankonet.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.bankonet.metier.Client;
import com.bankonet.metier.Compte;
import com.bankonet.metier.utils.Civilite;

public class ClientDaoFile implements ClientDao {

	private CompteDaoFile bddComptes;
	private String clientsPropertiesPath;
	
	public ClientDaoFile(String pclientsPropertiesPath, String pcomptesPropertiesPath){
		bddComptes = new CompteDaoFile(pcomptesPropertiesPath);
		clientsPropertiesPath = pclientsPropertiesPath;
	}
	
	@Override
	public void ajouterModifier(Client client) {
		boolean state = false;
		int nbLignes = 0;
		File fichierSav = new File(clientsPropertiesPath+".save");
		File fichierBase = new File(clientsPropertiesPath);
		try {
			fichierSav.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try(	FileReader clientsR = new FileReader(fichierBase);
				BufferedReader input_clients = new BufferedReader(clientsR);
				FileWriter clientsW = new FileWriter(fichierSav, true);
				BufferedWriter output_clients = new BufferedWriter(clientsW);){
		
			String ligne;
			while ((ligne=input_clients.readLine())!=null){
				nbLignes++;
				String identifiant = ligne.split("=")[0];
				if(identifiant.equals(client.getId())){
					state = true;
					output_clients.write(client.genererSauvegarde());
					output_clients.flush();
				}else{
					output_clients.write(ligne+"\n");
					output_clients.flush();
				}
			}
			if(!state || nbLignes==0){
				output_clients.write(client.genererSauvegarde());
				output_clients.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fichierBase.delete();
		fichierSav.renameTo(fichierBase);
		
		ArrayList<Compte> comptesList = client.getComptesList();
		bddComptes.ajouterModifier(comptesList);
	}

	@Override
	public ArrayList<String[]> retournerIdClients() {
		ArrayList<String[]> clientsString = new ArrayList<String[]>();
		try(	FileReader clients = new FileReader(clientsPropertiesPath);
				BufferedReader input_clients = new BufferedReader(clients);){
		
			String ligne;
			while ((ligne=input_clients.readLine())!=null){
				int nbCompteEpargne = 0;
				int nbCompteCourant = 0;
				String identifiant = ligne.split("=")[0];
				String nom = ligne.split("nom:")[1].split("&")[0];
				String prenom = ligne.split("prenom:")[1].split("&")[0];
				
				String[] comptesString = ligne.split("comptes:")[1].split(",");
				ArrayList<Compte> comptes = bddComptes.chargerComptes(comptesString);
				for(Compte compte:comptes)
					if(compte.getType().equals("Courant"))
						nbCompteCourant++;
					else if(compte.getType().equals("Epargne"))
						nbCompteEpargne++;
				String[] client = new String[2];
				client[0] = "\nIdentifiant: "+identifiant+" / Nom: "+nom+" / Prenom: "+prenom+" / Courant:"+nbCompteCourant+" / Epargne:"+nbCompteEpargne;
				client[1] = identifiant;
				clientsString.add(client);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clientsString;
	}

	@Override
	public Client chargerClient(String login) {
		Client client = null;
		try(	FileReader clients = new FileReader(clientsPropertiesPath);
				BufferedReader input_clients = new BufferedReader(clients);){
		
			String ligne;
			boolean state = true;
			while ((ligne=input_clients.readLine())!=null && state){
				String identifiant = ligne.split("=")[0];
				if(identifiant.equals(login)){
					state = false;
					String nom = ligne.split("nom:")[1].split("&")[0];
					String prenom = ligne.split("prenom:")[1].split("&")[0];
					String mdp = ligne.split("mdp:")[1].split("&")[0];
					Civilite civilite = Civilite.getCivilite(ligne.split("civilite:")[1].split("&")[0]);
					client = new Client(identifiant, mdp, civilite, nom, prenom);
					
					String[] comptesString = ligne.split("comptes:")[1].split(",");
					ArrayList<Compte> comptes = bddComptes.chargerComptes(comptesString);
					for(Compte compte:comptes)
						client.creerCompte(compte);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return client;
	}

	@Override
	public boolean connexionClient(String login, String pmdp) {
		boolean state = false;
		try(	FileReader clients = new FileReader(clientsPropertiesPath);
				BufferedReader input_clients = new BufferedReader(clients);){
		
			String ligne;
			while ((ligne=input_clients.readLine())!=null && !state){
				String identifiant = ligne.split("=")[0];
				if(identifiant.equals(login)){
					String mdp = ligne.split("mdp:")[1].split("&")[0];
					if(mdp.equals(pmdp))
						state = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public void supprimer(String login) {
		// TODO Auto-generated method stub
		
	}

}
