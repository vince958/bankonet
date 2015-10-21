package com.bankonet.dao.compte;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import com.bankonet.utils.Compte;

public class CompteDaoJpa implements CompteDao{

	EntityManagerFactory emf;
	
	public CompteDaoJpa(EntityManagerFactory pemf) {
		emf = pemf;
	}
	
	public Compte findCompteByIntitule(String intitule){
		EntityManager em = emf.createEntityManager();
		Compte compte;
		try{
			compte = em	.createNamedQuery("comptes.findCompteByIntitule", Compte.class)
				 		.setParameter("intitule", intitule)
				 		.getSingleResult();
		}catch(NoResultException e){
			compte = null;
		}
		return compte;
	}

	public List<Compte> findAllComptes(){
		EntityManager em = emf.createEntityManager();
		return em.createNamedQuery("comptes.findAllComptes", Compte.class)
				 .getResultList();
	}
	
	@Override
	public List<Compte> chargerComptes(List<String> comptesString) {
		List<Compte> comptes = new ArrayList<Compte>();
		EntityManager em = emf.createEntityManager();
		for(String intitule:comptesString){
			Compte compte = findCompteByIntitule(intitule);
			if(compte!=null)
				comptes.add(compte);
		}
		em.close();
		return comptes;
	}

	@Override
	public void ajouterModifier(List<Compte> comptesList) {
		EntityManager em = emf.createEntityManager();
		for(Compte compte:comptesList){
			EntityTransaction et = em.getTransaction();
			et.begin();
			
			Compte compteTemp = findCompteByIntitule(compte.getIntitule());
			if(compteTemp != null){
				compteTemp.setIntitule(compte.getIntitule());
				compteTemp.setLibelle(compte.getLibelle());
				compteTemp.setNumero(compte.getNumero());
				compteTemp.setSolde(compte.getSolde());
				compteTemp.setType(compte.getType());
			}else{
				em.persist(compte);
			}
			
			et.commit();
		}
		em.close();
	}

	@Override
	public void supprimer(List<String> intitules) {
		EntityManager em = emf.createEntityManager();
		for(String intitule:intitules){
			EntityTransaction et = em.getTransaction();
			et.begin();
			
			Compte compte = findCompteByIntitule(intitule);
			if(compte != null)
				em.remove(compte);
			
			et.commit();
		}
		em.close();
	}

	@Override
	public void toutSupprimer() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		List<Compte> comptes = findAllComptes();
		for(Compte compte:comptes)
			em.remove(compte);
		
		et.commit();
		em.close();
	}

}
