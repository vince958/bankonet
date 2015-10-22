package com.bankonet.dao.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;
import com.bankonet.utils.Compte;
import com.bankonet.utils.others.Civilite;

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
			e.printStackTrace();
		}
	}
	
	@Override
	public void ajouterModifier(Client client) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			statement.execute(
					"INSERT INTO clients (login, password, nom, prenom, civilite) VALUES ('"+client.getLogin()+"', '"+client.getMdp()+"', '"+client.getNom()+"', '"+client.getPrenom()+"', '"+client.getCivilite()+"')"+
					"  ON DUPLICATE KEY UPDATE login='"+client.getLogin()+"', password='"+client.getMdp()+"', nom='"+client.getNom()+"', prenom='"+client.getPrenom()+"', civilite='"+client.getCivilite()+"' ;"
					);
			statement.close();
			for(Compte compte:client.getComptesList()) {
				Statement st = bdd.createStatement();
				ResultSet resultat = st.executeQuery("SELECT * FROM clients_comptes WHERE login='"+client.getLogin()+"' && intitule='"+compte.getIntitule()+"';");
				if(!resultat.next()){
					st.execute(
						"INSERT INTO clients_comptes (login, intitule) VALUES ('"+client.getLogin()+"', '"+compte.getIntitule()+"');"
						);
				}
				
				st.close();
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public List<IdLibelleComptesDTO> retournerIdClients() {
		List<IdLibelleComptesDTO> clientsDto = new ArrayList<IdLibelleComptesDTO>();
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			ResultSet resultat = statement.executeQuery("SELECT login, nom, prenom FROM clients;");
			while(resultat.next()){
			
				String login = resultat.getString("login");
				String nom = resultat.getString("nom");
				String prenom = resultat.getString("prenom");
				String lib = "\nLogin: "+login+" / Nom: "+nom+" / Prenom: "+prenom;
				
				Statement statement_comptes = bdd.createStatement();
				ResultSet resultat_comptes = statement_comptes.executeQuery("SELECT intitule FROM clients_comptes WHERE login='"+login+"';");
				ArrayList<String> comptesList = new ArrayList<String>();
				while(resultat_comptes.next())
					comptesList.add(resultat_comptes.getString("intitule"));
				
				clientsDto.add(new IdLibelleComptesDTO(login, lib, comptesList));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return clientsDto;
	}

	@Override
	public ClientComptesDTO chargerClient(String login) {
		ClientComptesDTO clientDto = null;
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			ResultSet resultat = statement.executeQuery("SELECT * FROM clients WHERE login='"+login+"';");
			resultat.next();
			
			String mdp = resultat.getString("password");
			Civilite civilite = Civilite.getCivilite(resultat.getString("civilite"));
			String nom = resultat.getString("nom");
			String prenom = resultat.getString("prenom");
			
			resultat = statement.executeQuery("SELECT intitule FROM clients_comptes WHERE login='"+login+"';");
			ArrayList<String> comptesList = new ArrayList<String>();
			while(resultat.next())
				comptesList.add(resultat.getString("intitule"));
			
			clientDto = new ClientComptesDTO(new Client(login, mdp, civilite, nom, prenom), comptesList);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return clientDto;
	}

	@Override
	public boolean connexionClient(String login, String pmdp) {
		boolean connexion = false;
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			ResultSet resultat = statement.executeQuery("SELECT password FROM clients WHERE login='"+login+"' AND password='"+pmdp+"';");
			if(resultat.next())
				connexion = true;
			
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return connexion;
	}

	@Override
	public void supprimer(String login) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			statement.execute("DELETE FROM clients where login='"+login+"';");
			statement.execute("DELETE FROM clients_comptes where login='"+login+"';");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public List<ClientComptesDTO> rechercher(String nom, String prenom) {
		List<ClientComptesDTO> ccdList = new ArrayList<ClientComptesDTO>();
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			ResultSet resultat = statement.executeQuery("SELECT * FROM clients where nom='"+nom+"' OR prenom='"+prenom+"';");
			while(resultat.next()){
				Client client = new Client(resultat.getString("login"), resultat.getString("password"), Civilite.getCivilite(resultat.getString("civilite")), resultat.getString("nom"), resultat.getString("prenom"));
				Statement statement_comptes = bdd.createStatement();
				ResultSet resultat_comptes = statement_comptes.executeQuery("SELECT intitule FROM clients_comptes WHERE login='"+resultat.getString("login")+"';");
				ArrayList<String> comptesList = new ArrayList<String>();
				while(resultat_comptes.next())
					comptesList.add(resultat_comptes.getString("intitule"));
				ccdList.add(new ClientComptesDTO(client, comptesList));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return ccdList;
	}

	@Override
	public void toutSupprimer() {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			statement.execute("DELETE FROM clients;");
			statement.execute("DELETE FROM clients_comptes;");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public List<ClientComptesDTO> getAllClients() {
		List<ClientComptesDTO> ccdList = new ArrayList<ClientComptesDTO>();
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			ResultSet resultat = statement.executeQuery("SELECT * FROM clients;");
			while(resultat.next()){
				Client client = new Client(resultat.getString("login"), resultat.getString("password"), Civilite.getCivilite(resultat.getString("civilite")), resultat.getString("nom"), resultat.getString("prenom"));
				Statement statement_comptes = bdd.createStatement();
				ResultSet resultat_comptes = statement_comptes.executeQuery("SELECT intitule FROM clients_comptes WHERE login='"+resultat.getString("login")+"';");
				ArrayList<String> comptesList = new ArrayList<String>();
				while(resultat_comptes.next())
					comptesList.add(resultat_comptes.getString("intitule"));
				ccdList.add(new ClientComptesDTO(client, comptesList));
			}
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return ccdList;
	}

}
