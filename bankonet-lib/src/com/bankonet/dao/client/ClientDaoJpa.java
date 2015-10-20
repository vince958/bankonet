package com.bankonet.dao.client;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;

public class ClientDaoJpa implements ClientDao{
	
	EntityManagerFactory emf;
	
	public ClientDaoJpa(EntityManagerFactory pemf) {
		emf = pemf;
	}

	@Override
	public void ajouterModifier(Client client) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		em.persist(client);
		et.commit();
		em.close();
	}

	@Override
	public List<IdLibelleComptesDTO> retournerIdClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientComptesDTO chargerClient(String login) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean connexionClient(String login, String pmdp) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void supprimer(String login) {
		// TODO Auto-generated method stub
		
	}

}
