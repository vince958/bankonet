package com.bankonet.dao;

import java.util.List;

import com.bankonet.utils.Compte;

public interface CompteDao {
	List<Compte> chargerComptes(List<String> comptesString);
	void ajouterModifier(List<Compte> comptesList);
	void supprimer(List<String> ident);
}
