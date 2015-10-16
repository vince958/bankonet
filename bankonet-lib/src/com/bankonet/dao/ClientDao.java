package com.bankonet.dao;

import java.util.List;

import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;

public interface ClientDao {
	void ajouterModifier(Client client);
	List<IdLibelleComptesDTO> retournerIdClients();
	ClientComptesDTO chargerClient(String login);
	boolean connexionClient(String login, String pmdp);
	void supprimer(String login);
}
