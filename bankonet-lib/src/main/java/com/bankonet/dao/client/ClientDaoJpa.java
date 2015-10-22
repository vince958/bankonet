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
import com.bankonet.utils.Compte;
import com.bankonet.utils.others.TypeCompte;

public class ClientDaoJpa implements ClientDao{
	
	EntityManagerFactory emf;
	
	public ClientDaoJpa(EntityManagerFactory pemf) {
		emf = pemf;
	}
	
	public Client findClientByLogin(EntityManager em, String login){
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
	
	public List<Client> findAllClients(EntityManager em){
		return em.createNamedQuery("clients.findAllClients", Client.class)
				 .getResultList();
	}
	
	public Client existClientLoginPass(EntityManager em, String login, String mdp){
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
	
	public List<Client> findClientByFirstLastName(EntityManager em, String nom, String prenom){
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
		
		Client clientTemp = findClientByLogin(em, client.getLogin());
		if(clientTemp != null){
			clientTemp.setCivilite(client.getCivilite());
			clientTemp.setLogin(client.getLogin());
			clientTemp.setMdp(client.getMdp());
			clientTemp.setNom(client.getNom());
			clientTemp.setPrenom(client.getPrenom());
			clientTemp.setComptesList(client.getComptesList());
		}else{
			em.persist(client);
		}

		et.commit();
		em.close();
		/*
		em = emf.createEntityManager();
		clientTemp = findClientByLogin(em, client.getLogin());
		if(clientTemp != null){
			System.out.println(clientTemp.getNom()+"    "+clientTemp.getPrenom());
		}
		em.close();
		*/
	}

	@Override
	public List<IdLibelleComptesDTO> retournerIdClients() {
		EntityManager em = emf.createEntityManager();
		List<IdLibelleComptesDTO> listClients = new ArrayList<IdLibelleComptesDTO>();
		List<Client> clients = findAllClients(em);
		for(Client client:clients){
			String lib = "\nIdentifiant: "+client.getLogin()+" / Nom: "+client.getNom()+" / Prenom: "+client.getPrenom();
			
			List<String> comptesList = new ArrayList<String>();
			for(Compte compte:client.getComptesList())
				comptesList.add(compte.getIntitule());
			listClients.add(new IdLibelleComptesDTO(client.getLogin(), lib, comptesList));
		}
		em.close();
		return listClients;
	}

	@Override
	public ClientComptesDTO chargerClient(String login) {
		EntityManager em = emf.createEntityManager();
		Client client = findClientByLogin(em, login);
		List<String> comptesList = new ArrayList<String>();
		for(Compte compte:client.getComptesList()){
			compte.setType(TypeCompte.getType(compte.getAType()));
			comptesList.add(compte.getIntitule());
		}
		em.close();
		return new ClientComptesDTO(client, comptesList);
	}

	@Override
	public boolean connexionClient(String login, String mdp) {
		EntityManager em = emf.createEntityManager();
		boolean connexion = false;
		Client clientTemp = existClientLoginPass(em, login, mdp);
		if(clientTemp != null)
			connexion = true;
		em.close();
		return connexion;
	}

	@Override
	public void supprimer(String login) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		Client client = findClientByLogin(em, login);
		if(client != null)
			em.remove(em.merge(client));
		
		et.commit();
		em.close();
	}

	@Override
	public List<ClientComptesDTO> rechercher(String nom, String prenom) {
		EntityManager em = emf.createEntityManager();
		List<ClientComptesDTO> ccdList = new ArrayList<ClientComptesDTO>();
		List<Client> clients = findClientByFirstLastName(em, nom, prenom);
		for(Client client:clients){
			List<String> comptesList = new ArrayList<String>();
			for(Compte compte:client.getComptesList()){
				compte.setType(TypeCompte.getType(compte.getAType()));
				comptesList.add(compte.getIntitule());
			}
			ccdList.add(new ClientComptesDTO(client, comptesList));
		}
		em.close();
		return ccdList;
	}

	@Override
	public void toutSupprimer() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		List<Client> clients = findAllClients(em);
		for(Client client:clients)
			em.remove(em.merge(client));
		
		et.commit();
		em.close();
	}

	@Override
	public List<ClientComptesDTO> getAllClients() {
		EntityManager em = emf.createEntityManager();
		List<ClientComptesDTO> ccdList = new ArrayList<ClientComptesDTO>();
		List<Client> clients = findAllClients(em);
		for(Client client:clients){
			List<String> comptesList = new ArrayList<String>();
			for(Compte compte:client.getComptesList()){
				compte.setType(TypeCompte.getType(compte.getAType()));
				comptesList.add(compte.getIntitule());
			}
			ccdList.add(new ClientComptesDTO(client, comptesList));
		}
		em.close();
		return ccdList;
	}

}
