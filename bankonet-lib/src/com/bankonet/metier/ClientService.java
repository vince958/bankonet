package com.bankonet.metier;

import java.util.ArrayList;
import java.util.List;

import com.bankonet.dao.ClientDao;
import com.bankonet.dao.ClientDaoFile;
import com.bankonet.dao.CompteDao;
import com.bankonet.dao.CompteDaoFile;
import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;

public class ClientService {
	private CompteDao daoComptes;
	private ClientDao daoClients;
	
	public ClientService(String pclientsPropertiesPath, String pcomptesPropertiesPath){
		daoComptes = new CompteDaoFile(pcomptesPropertiesPath);
		daoClients = new ClientDaoFile(pclientsPropertiesPath);
	}
	
	public void ajouterModifier(Client client) {
		daoClients.ajouterModifier(client);
		ArrayList<Compte> comptesList = client.getComptesList();
		daoComptes.ajouterModifier(comptesList);
	}
	
	public List<String[]> getLibelleList() {
		List<String[]> libelleId = new ArrayList<String[]>();
		List<IdLibelleComptesDTO> dtoList = daoClients.retournerIdClients();
		for(IdLibelleComptesDTO dto:dtoList){
			int nbCompteEpargne = 0;
			int nbCompteCourant = 0;
			List<Compte> comptes = daoComptes.chargerComptes(dto.getComptesList());
			for(Compte compte:comptes)
				if(compte.getType().equals("Courant"))
					nbCompteCourant++;
				else if(compte.getType().equals("Epargne"))
					nbCompteEpargne++;
			libelleId.add(new String[] {dto.getLibelle()+" / Courant:"+nbCompteCourant+" / Epargne:"+nbCompteEpargne, dto.getId()});
		}
		return libelleId;
	}
	
	public Client getClient(String login){
		ClientComptesDTO dto = daoClients.chargerClient(login);
		Client client = dto.getClient();
		List<Compte> comptes = daoComptes.chargerComptes(dto.getComptesList());
		for(Compte compte:comptes)
			client.creerCompte(compte);
		return client;
	}
	
	public boolean connexionClient(String login, String mdp){
		return daoClients.connexionClient(login, mdp);
	}
}
