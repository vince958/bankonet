package com.bankonet.metier;

import java.util.ArrayList;
import java.util.List;

import com.bankonet.dao.client.ClientDao;
import com.bankonet.dao.compte.CompteDao;
import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;

public class ClientService {
	private CompteDao daoComptes;
	private ClientDao daoClients;
	
	public ClientService(ClientDao pclientDao, CompteDao pcompteDao){
		daoComptes = pcompteDao;
		daoClients = pclientDao;
	}
	
	public void ajouterModifier(Client client) {
		daoClients.ajouterModifier(client);
		List<Compte> comptesList = client.getComptesList();
		daoComptes.ajouterModifier(comptesList);
	}
	
	public List<String[]> getLibelleList() {
		List<String[]> libelleId = new ArrayList<String[]>();
		List<IdLibelleComptesDTO> dtoList = daoClients.retournerIdClients();
		for(IdLibelleComptesDTO dto:dtoList){
			int nbCompteEpargne = 0;
			int nbCompteCourant = 0;
			for(Compte compte:daoComptes.chargerComptes(dto.getComptesList())){
				if(compte.getType().getValue().equals("Courant"))
					nbCompteCourant++;
				else if(compte.getType().getValue().equals("Epargne"))
					nbCompteEpargne++;
			}
			libelleId.add(new String[] {dto.getLibelle()+" / Courant:"+nbCompteCourant+" / Epargne:"+nbCompteEpargne, dto.getId()});
		}
		return libelleId;
	}
	
	public Client getClient(String login){
		ClientComptesDTO dto = daoClients.chargerClient(login);
		Client client = dto.getClient();
		client.setComptesList(daoComptes.chargerComptes(dto.getComptesList()));
		return client;
	}
	
	public boolean connexionClient(String login, String mdp){
		return daoClients.connexionClient(login, mdp);
	}
}
