package com.bankonet.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.bankonet.dao.client.ClientDao;
import com.bankonet.dao.client.ClientDaoJpa;
import com.bankonet.dao.compte.CompteDao;
import com.bankonet.dao.compte.CompteDaoJpa;

public class DaoFactoryJpa implements DaoFactory{
	
	EntityManagerFactory emf;
	
	public DaoFactoryJpa(String nomBDD) {
		emf = Persistence.createEntityManagerFactory(nomBDD);
	}

	@Override
	public ClientDao getClientDao() {
		return new ClientDaoJpa(emf);
	}

	@Override
	public CompteDao getCompteDao() {
		return new CompteDaoJpa(emf);
	}

}
