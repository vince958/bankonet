package com.bankonet.dao.client;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
		
		TypedQuery<Client> query = em.createQuery("select c from Client c where c.login='"+client.getLogin()+"'", Client.class);
		Client clientTemp = query.getSingleResult();
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
		EntityManager em = emf.createEntityManager();
		TypedQuery<Client> query = em.createQuery("select c from Client c", Client.class);
		List<Client> clients = query.getResultList();
		for(Client client:clients){
			String lib = "\nIdentifiant: "+client.getLogin()+" / Nom: "+client.getNom()+" / Prenom: "+client.getPrenom();
			List<String> comptesList = new ArrayList<String>();
			listClients.add(new IdLibelleComptesDTO(client.getLogin(), lib, comptesList));
		}
		em.close();
		return listClients;
	}

	@Override
	public ClientComptesDTO chargerClient(String login) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select c from Client c where c.login='"+login+"'");
		Client client = (Client)query.getSingleResult();
		List<String> comptesList = new ArrayList<String>();
		em.close();
		return new ClientComptesDTO(client, comptesList);
	}

	@Override
	public boolean connexionClient(String login, String pmdp) {
		boolean connexion = false;
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("select c from Client c where c.login='"+login+"', c.mdp='"+pmdp+"'");
		Client clientTemp = (Client) query.getSingleResult();
		if(clientTemp != null)
			connexion = true;
		return connexion;
	}

	@Override
	public void supprimer(String login) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		TypedQuery<Client> query = em.createQuery("select c from Client c where c.login='"+login+"'", Client.class);
		Client client = query.getSingleResult();
		if(client != null)
			em.remove(client);
		
		et.commit();
		em.close();
	}

	@Override
	public List<Client> rechercher(String nom, String prenom) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<Client> query = em.createQuery("select c from Client c where c.nom='"+nom+"' or c.prenom='"+prenom+"'", Client.class);
		List<Client> clients = query.getResultList();
		em.close();
		return clients;
	}

	@Override
	public void toutSupprimer() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		TypedQuery<Client> query = em.createQuery("select c from Client c", Client.class);
		List<Client> clients = query.getResultList();
		for(Client client:clients)
			em.remove(client);
		
		et.commit();
		em.close();
	}

}
