package com.bankonet.dao;

import com.bankonet.dao.client.ClientDao;
import com.bankonet.dao.client.ClientDaoSQL;
import com.bankonet.dao.compte.CompteDao;
import com.bankonet.dao.compte.CompteDaoSQL;

public class DaoFactorySQL implements DaoFactory {

	private String url;
	private String user;
	private String password;
	
	public DaoFactorySQL(String purl, String puser, String ppassword) {
		url = purl;
		user = puser;
		password = ppassword;
	}
	
	@Override
	public ClientDao getClientDao() {
		return new ClientDaoSQL(url, user, password);
	}

	@Override
	public CompteDao getCompteDao() {
		return new CompteDaoSQL(url, user, password);
	}

}
