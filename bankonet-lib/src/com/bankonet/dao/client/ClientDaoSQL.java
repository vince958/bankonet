package com.bankonet.dao.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;

public class ClientDaoSQL implements ClientDao {

	private String url;
	private String user;
	private String password;
	
	public ClientDaoSQL(String purl, String puser, String ppassword) {
		url = purl;
		user = puser;
		password = ppassword;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void ajouterModifier(Client client) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			statement.execute(
					"INSERT INTO clients (login, password, nom, prenom, civilite) VALUES ('"+client.getId()+"', '"+client.getMdp()+"', '"+client.getNom()+"', '"+client.getPrenom()+"', '"+client.getCivilite()+"')"+
					"  ON DUPLICATE KEY UPDATE login='"+client.getId()+"', password='"+client.getMdp()+"', nom='"+client.getNom()+"', prenom='"+client.getPrenom()+"', civilite='"+client.getCivilite()+"' ;"
					);
			statement.close();
			for(Compte compte:client.getComptesList()) {
				Statement st = bdd.createStatement();
				
				st.execute(
						"INSERT INTO clients_comptes (login, intitule) VALUES ('"+client.getId()+"', '"+compte.getIntitule()+"')"+
						"  ON DUPLICATE KEY UPDATE login='"+client.getId()+"',intitule='"+compte.getIntitule()+"';"
						);
				
				st.close();
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public List<IdLibelleComptesDTO> retournerIdClients() {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}

	@Override
	public ClientComptesDTO chargerClient(String login) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}

	@Override
	public boolean connexionClient(String login, String pmdp) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return false;
	}

	@Override
	public void supprimer(String login) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

}
