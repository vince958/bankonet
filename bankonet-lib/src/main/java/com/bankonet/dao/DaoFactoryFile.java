package com.bankonet.dao;

import com.bankonet.dao.client.ClientDao;
import com.bankonet.dao.client.ClientDaoFile;
import com.bankonet.dao.compte.CompteDao;
import com.bankonet.dao.compte.CompteDaoFile;

public class DaoFactoryFile implements DaoFactory {
	private String clientsPropertiesPath;
	private String comptesPropertiesPath;

	public DaoFactoryFile(String pclientsPropertiesPath, String pcomptesPropertiesPath) {
		clientsPropertiesPath = pclientsPropertiesPath;
		comptesPropertiesPath = pcomptesPropertiesPath;
	}
	
	@Override
	public ClientDao getClientDao() {
		return new ClientDaoFile(clientsPropertiesPath);
	}

	@Override
	public CompteDao getCompteDao() {
		return new CompteDaoFile(comptesPropertiesPath);
	}

}
