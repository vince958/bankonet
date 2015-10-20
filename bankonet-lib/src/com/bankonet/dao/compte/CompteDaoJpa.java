package com.bankonet.dao.compte;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.bankonet.utils.Compte;

public class CompteDaoJpa implements CompteDao{

	EntityManagerFactory emf;
	
	public CompteDaoJpa(EntityManagerFactory pemf) {
		emf = pemf;
	}
	
	@Override
	public List<Compte> chargerComptes(List<String> comptesString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ajouterModifier(List<Compte> comptesList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void supprimer(List<String> ident) {
		// TODO Auto-generated method stub
		
	}

}
