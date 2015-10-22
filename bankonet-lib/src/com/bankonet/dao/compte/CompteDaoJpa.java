package com.bankonet.dao.compte;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;
import com.bankonet.utils.others.TypeCompte;

public class CompteDaoJpa implements CompteDao{

	EntityManagerFactory emf;
	
	public CompteDaoJpa(EntityManagerFactory pemf) {
		emf = pemf;
		
		int nbTotal = 0;
		int nbCourant = 0;
		int nbEpargne = 0;

		EntityManager em = emf.createEntityManager();
		List<Compte> comptes = findAllComptes(em);
		nbTotal = comptes.size();
		
		Compte.setNbTotal(nbTotal);
		CompteCourant.setNbComptesCourants(nbCourant);
		CompteEpargne.setNbComptesEpargnes(nbEpargne);
		em.close();
	}
	
	public Compte findCompteByIntitule(EntityManager em, String intitule){
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

	public List<Compte> findAllComptes(EntityManager em){
		return em.createNamedQuery("comptes.findAllComptes", Compte.class)
				 .getResultList();
	}
	
	@Override
	public List<Compte> chargerComptes(List<String> comptesString) {
		List<Compte> comptes = new ArrayList<Compte>();
		EntityManager em = emf.createEntityManager();
		for(String intitule:comptesString){
			Compte compte = findCompteByIntitule(em, intitule);
			compte.setType(TypeCompte.getType(compte.getAType()));
			if(compte!=null)
				comptes.add(compte);
		}
		em.close();
		return comptes;
	}

	@Override
	public void ajouterModifier(List<Compte> comptesList) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		for(Compte compte:comptesList){
			Compte compteTemp = findCompteByIntitule(em, compte.getIntitule());
			if(compteTemp != null){
				compteTemp.setIntitule(compte.getIntitule());
				compteTemp.setLibelle(compte.getLibelle());
				compteTemp.setNumero(compte.getNumero());
				compteTemp.setSolde(compte.getSolde());
				compteTemp.setType(compte.getType());
				if(compteTemp.getAType().equals("COURANT")){
					((CompteCourant)compteTemp).setMontantDecouvertAutorise(((CompteCourant)compte).getMontantDecouvertAutorise());
				}else{
					((CompteEpargne)compteTemp).setTauxInteret(((CompteEpargne)compte).getTauxInteret());
				}
			}else{
				em.persist(compte);
			}
		}
		
		et.commit();
		em.close();
	}

	@Override
	public void supprimer(List<String> intitules) {
		EntityManager em = emf.createEntityManager();
		for(String intitule:intitules){
			EntityTransaction et = em.getTransaction();
			et.begin();
			
			Compte compte = findCompteByIntitule(em, intitule);
			if(compte != null)
				em.remove(em.merge(compte));
			
			et.commit();
		}
		em.close();
	}

	@Override
	public void toutSupprimer() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		
		List<Compte> comptes = findAllComptes(em);
		for(Compte compte:comptes)
			em.remove(em.merge(compte));
		
		et.commit();
		em.close();
	}

}
