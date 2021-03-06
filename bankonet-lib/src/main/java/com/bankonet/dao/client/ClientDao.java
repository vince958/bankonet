package com.bankonet.dao.client;

import java.util.List;

import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;

public interface ClientDao {
	void ajouterModifier(Client client);
	List<IdLibelleComptesDTO> retournerIdClients();
	ClientComptesDTO chargerClient(String login);
	boolean connexionClient(String login, String pmdp);
	List<ClientComptesDTO> rechercher(String nom, String prenom);
	List<ClientComptesDTO> getAllClients();
	void supprimer(String login);
	void toutSupprimer();
}
