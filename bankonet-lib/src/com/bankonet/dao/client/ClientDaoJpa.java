package com.bankonet.dao.client;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;

public class ClientDaoJpa implements ClientDao{
	
	EntityManagerFactory emf;
	
	public ClientDaoJpa(EntityManagerFactory pemf) {
		emf = pemf;
	}
	
	public Client findClientByLogin(String login){
		EntityManager em = emf.createEntityManager();
		Client client;
		try{
			client = em	.createNamedQuery("clients.findClientByLogin", Client.class)
				 		.setParameter("login", login)
				 		.getSingleResult();
		}catch(NoResultException e){
			client = null;
		}
		return client;
	}
	
	public List<Client> findAllClients(){
		EntityManager em = emf.createEntityManager();
		return em.createNamedQuery("clients.findAllClients", Client.class)
				 .getResultList();
	}
	
	public Client existClientLoginPass(String login, String mdp){
		EntityManager em = emf.createEntityManager();
		Client client;
		try{
			client = em .createNamedQuery("clients.existClientLoginPass", Client.class)
				 		.setParameter("login", login)
				 		.setParameter("mdp", mdp)
				 		.getSingleResult();
		}catch(NoResultException e){
			client = null;
		}
		return client;
	}
	
	public List<Client> findClientByFirstLastName(String nom, String prenom){
		EntityManager em = emf.createEntityManager();
		return em.createNamedQuery("clients.findClientByFirstLastName", Client.class)
				 .setParameter("nom", nom)
				 .setParameter("prenom", prenom)
				 .getResultList();
	}

	@Override
	public void ajouterModifier(Client client) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		Client clientTemp = findClientByLogin(client.getLogin());
		if(clientTemp != null){
			clientTemp.setCivilite(client.getCivilite());
			clientTemp.setLogin(client.getLogin());
			clientTemp.setMdp(client.getMdp());
			clientTemp.setNom(client.getNom());
			clientTemp.setPrenom(client.getPrenom());
		}else{
			em.persist(client);
		}
		
		et.commit();
		em.close();
	}

	@Override
	public List<IdLibelleComptesDTO> retournerIdClients() {
		List<IdLibelleComptesDTO> listClients = new ArrayList<IdLibelleComptesDTO>();
		List<Client> clients = findAllClients();
		for(Client client:clients){
			String lib = "\nIdentifiant: "+client.getLogin()+" / Nom: "+client.getNom()+" / Prenom: "+client.getPrenom();
			List<String> comptesList = new ArrayList<String>();
			listClients.add(new IdLibelleComptesDTO(client.getLogin(), lib, comptesList));
		}
		return listClients;
	}

	@Override
	public ClientComptesDTO chargerClient(String login) {
		EntityManager em = emf.createEntityManager();
		Client client = findClientByLogin(login);
		List<String> comptesList = new ArrayList<String>();
		em.close();
		return new ClientComptesDTO(client, comptesList);
	}

	@Override
	public boolean connexionClient(String login, String mdp) {
		boolean connexion = false;
		Client clientTemp = existClientLoginPass(login, mdp);
		if(clientTemp != null)
			connexion = true;
		return connexion;
	}

	@Override
	public void supprimer(String login) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		Client client = findClientByLogin(login);
		if(client != null)
			em.remove(client);
		
		et.commit();
		em.close();
	}

	@Override
	public List<Client> rechercher(String nom, String prenom) {
		List<Client> clients = findClientByFirstLastName(nom, prenom);
		return clients;
	}

	@Override
	public void toutSupprimer() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		List<Client> clients = findAllClients();
		for(Client client:clients)
			em.remove(client);
		
		et.commit();
		em.close();
	}

}
