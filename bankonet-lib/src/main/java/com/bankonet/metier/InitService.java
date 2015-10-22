package com.bankonet.metier;

import java.util.ArrayList;
import java.util.List;

import com.bankonet.dao.client.ClientDao;
import com.bankonet.dao.compte.CompteDao;
import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.others.Civilite;

public class InitService {
	
	private ClientDao clientDao;
	private CompteDao compteDao;
	
	public InitService(ClientDao pclientDao, CompteDao pcompteDao) {
		clientDao = pclientDao;
		compteDao = pcompteDao;
	}
	
	public void init(){
		Client client1 = new Client("vince958", "1989", Civilite.MONSIEUR, "LANDRA", "Vincent");
		Compte compte1 = new CompteCourant(client1.getNom(), client1.getPrenom(), 50.0d, 2000.0d);
		client1.creerCompte(compte1);
		Client client2 = new Client("jennifer", "1991", Civilite.MADEMOISELLE, "PECOUT", "Charlene");
		Compte compte2 = new CompteCourant(client2.getNom(), client2.getPrenom(), 50.0d, 2000.0d);
		client2.creerCompte(compte2);
		Client client3 = new Client("teddy", "1993", Civilite.MONSIEUR, "LANDRA", "Florent");
		Compte compte3 = new CompteCourant(client3.getNom(), client3.getPrenom(), 50.0d, 2000.0d);
		client3.creerCompte(compte3);
		Client client4 = new Client("boulouloubi", "1989", Civilite.MONSIEUR, "BROCHE", "Bastien");
		Compte compte4 = new CompteCourant(client4.getNom(), client4.getPrenom(), 50.0d, 2000.0d);
		client4.creerCompte(compte4);
		Client client5 = new Client("skwib", "1989", Civilite.MONSIEUR, "SECHERET", "Tifaine");
		Compte compte5 = new CompteCourant(client5.getNom(), client5.getPrenom(), 50.0d, 2000.0d);
		client5.creerCompte(compte5);
		
		clientDao.ajouterModifier(client1);
		clientDao.ajouterModifier(client2);
		clientDao.ajouterModifier(client3);
		clientDao.ajouterModifier(client4);
		clientDao.ajouterModifier(client5);
		
		compteDao.ajouterModifier(client1.getComptesList());
		compteDao.ajouterModifier(client2.getComptesList());
		compteDao.ajouterModifier(client3.getComptesList());
		compteDao.ajouterModifier(client4.getComptesList());
		compteDao.ajouterModifier(client5.getComptesList());
	}
	
	public List<Client> rechercherClient(String nom, String prenom){
		List<Client> clients = new ArrayList<Client>();
		for(ClientComptesDTO ccd:clientDao.rechercher(nom, prenom)){
			ccd.getClient().setComptesList(compteDao.chargerComptes(ccd.getComptesList()));
			clients.add(ccd.getClient());
		}
		return clients;
	}
	
	public void modifierClient(String login, String nom, String prenom){
		ClientComptesDTO client = clientDao.chargerClient(login);
		client.getClient().setNom(nom);
		client.getClient().setPrenom(prenom);
		clientDao.ajouterModifier(client.getClient());
	}
	
	public void supprimerClient(String login){
		Client client = clientDao.chargerClient(login).getClient();
		List<Compte> comptes = client.getComptesList();
		clientDao.supprimer(login);
		List<Client> clients = new ArrayList<Client>();
		for(ClientComptesDTO ccd:clientDao.getAllClients()){
			ccd.getClient().setComptesList(compteDao.chargerComptes(ccd.getComptesList()));
			clients.add(ccd.getClient());
		}
		for(Client clientTemp:clients){
			List<Compte> comptesTemp = clientTemp.getComptesList();
			for(Compte compte:comptes){
				for(Compte compteTemp:comptesTemp){
					if(compte.getIntitule().equals(compteTemp.getIntitule()))
						comptes.remove(compte);
				}
			}
		}
		List<String> comptesString = new ArrayList<String>();
		for(Compte compte:comptes)
			comptesString.add(compte.getIntitule());
		compteDao.supprimer(comptesString);
	}
	
	public void supprimerTous(){
		clientDao.toutSupprimer();
		compteDao.toutSupprimer();
	}
}
