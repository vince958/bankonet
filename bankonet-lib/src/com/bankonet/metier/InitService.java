package com.bankonet.metier;

import java.util.List;

import com.bankonet.dao.client.ClientDao;
import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.utils.Client;
import com.bankonet.utils.others.Civilite;

public class InitService {
	
	private ClientDao clientDao;
	
	public InitService(ClientDao pclientDao) {
		clientDao = pclientDao;
	}
	
	public void init(){
		Client client1 = new Client("vince958", "1989", Civilite.MONSIEUR, "LANDRA", "Vincent");
		Client client2 = new Client("jennifer", "1991", Civilite.MADEMOISELLE, "PECOUT", "Charlene");
		Client client3 = new Client("teddy", "1993", Civilite.MONSIEUR, "LANDRA", "Florent");
		Client client4 = new Client("boulouloubi", "1989", Civilite.MONSIEUR, "BROCHE", "Bastien");
		Client client5 = new Client("skwib", "1989", Civilite.MONSIEUR, "SECHERET", "Tifaine");
		
		clientDao.ajouterModifier(client1);
		clientDao.ajouterModifier(client2);
		clientDao.ajouterModifier(client3);
		clientDao.ajouterModifier(client4);
		clientDao.ajouterModifier(client5);
	}
	
	public List<Client> rechercherClient(String nom, String prenom){
		return clientDao.rechercher(nom, prenom);
	}
	
	public void modifierClient(String login, String nom, String prenom){
		ClientComptesDTO client = clientDao.chargerClient(login);
		client.getClient().setNom(nom);
		client.getClient().setPrenom(prenom);
		clientDao.ajouterModifier(client.getClient());
	}
	
	public void supprimerClient(String login){
		clientDao.supprimer(login);
	}
	
	public void supprimerTous(){
		clientDao.toutSupprimer();
	}
}
