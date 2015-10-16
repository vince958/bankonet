package com.bankonet.dao;

import java.util.ArrayList;

import com.bankonet.metier.Client;

public interface ClientDao {
	public void ajouterModifier(Client client);
	public ArrayList<String[]> retournerIdClients();
	public Client chargerClient(String login);
	public boolean connexionClient(String login, String pmdp);
	public void supprimer(String login);
}
