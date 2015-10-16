package com.bankonet.dao;

import java.util.ArrayList;

import com.bankonet.metier.Compte;

public interface CompteDao {
	public ArrayList<Compte> chargerComptes(String[] comptesString);
	public void ajouterModifier(ArrayList<Compte> comptesList);
	public void supprimer(String[] ident);
}
