package com.bankonet.dao.dto;

import java.util.List;

import com.bankonet.utils.Client;

public class ClientComptesDTO {
	private Client client;
	private List<String> comptesList;
	
	public ClientComptesDTO(Client pclient, List<String> pcomptesList) {
		client = pclient;
		comptesList = pcomptesList;
	}
	
	public Client getClient(){ return client; }
	public List<String> getComptesList(){ return comptesList; }
}
