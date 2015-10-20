package com.bankonet.metier;

import com.bankonet.dao.client.ClientDao;
import com.bankonet.utils.Client;
import com.bankonet.utils.others.Civilite;

public class InitService {
	
	private ClientDao clientDao;
	
	public InitService(ClientDao pclientDao) {
		clientDao = pclientDao;
	}
	
	public void init(){
		Client client1 = new Client("test1", "test1", Civilite.MONSIEUR, "test1", "test1");
		Client client2 = new Client("test2", "test2", Civilite.MONSIEUR, "test2", "test2");
		Client client3 = new Client("test3", "test3", Civilite.MONSIEUR, "test3", "test3");
		Client client4 = new Client("test4", "test4", Civilite.MONSIEUR, "test4", "test4");
		Client client5 = new Client("test5", "test5", Civilite.MONSIEUR, "test5", "test5");
		
		clientDao.ajouterModifier(client1);
		clientDao.ajouterModifier(client2);
		clientDao.ajouterModifier(client3);
		clientDao.ajouterModifier(client4);
		clientDao.ajouterModifier(client5);
	}
}
