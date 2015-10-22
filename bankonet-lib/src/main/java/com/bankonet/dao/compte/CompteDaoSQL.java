package com.bankonet.dao.compte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;
import com.bankonet.utils.others.TypeCompte;

public class CompteDaoSQL implements CompteDao {

	private String url;
	private String user;
	private String password;
	
	public CompteDaoSQL(String purl, String puser, String ppassword) {
		url = purl;
		user = puser;
		password = ppassword;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		int nbTotal = 0;
		int nbCourant = 0;
		int nbEpargne = 0;
		
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			ResultSet resultat = statement.executeQuery("SELECT * FROM comptes;");
			resultat.last();
			nbTotal = resultat.getRow();
			
			resultat = statement.executeQuery("SELECT * FROM comptes WHERE type='COURANT';");
			resultat.last();
			nbCourant = resultat.getRow();
			
			resultat = statement.executeQuery("SELECT * FROM comptes WHERE intitule='EPARGNE';");
			resultat.last();
			nbEpargne = resultat.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Compte.setNbTotal(nbTotal);
		CompteCourant.setNbComptesCourants(nbCourant);
		CompteEpargne.setNbComptesEpargnes(nbEpargne);
	}
	
	@Override
	public List<Compte> chargerComptes(List<String> comptesString) {
		List<Compte> comptes = new ArrayList<Compte>();
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			for(String intitule:comptesString){
				Statement statement = bdd.createStatement();
				ResultSet resultat = statement.executeQuery("SELECT * FROM comptes WHERE intitule='"+intitule+"';");
				resultat.next();
				
				String numero = resultat.getString("numero");
				TypeCompte type = TypeCompte.getType(resultat.getString("type"));
				String libelle = resultat.getString("libelle");
				double solde = resultat.getDouble("solde");
				
				if(type.getValue().equals("Courant")) comptes.add(new CompteCourant(numero, type, intitule, libelle, solde, resultat.getFloat("decouvert")));
				else comptes.add(new CompteEpargne(numero, type, intitule, libelle, solde, resultat.getFloat("taux")));
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return comptes;
	}

	@Override
	public void ajouterModifier(List<Compte> comptesList) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			
			float taux;
			double decouvert;
			for(Compte compte:comptesList){
				Statement statement = bdd.createStatement();
				if(compte.getType().getValue().equals("Courant")){
					decouvert = ((CompteCourant)compte).getMontantDecouvertAutorise();
					taux = 0f;
				}else{
					decouvert = 0d;
					taux = (float)((CompteEpargne)compte).getTauxInteret();
				}
				statement.execute(
						"INSERT INTO comptes (intitule, numero, type, libelle, solde, decouvert, taux) VALUES ('"+compte.getIntitule()+"', '"+compte.getNumero()+"', '"+compte.getType()+"', '"+compte.getLibelle()+"', '"+compte.getSolde()+"', "+decouvert+", "+taux+")"+
						"  ON DUPLICATE KEY UPDATE intitule='"+compte.getIntitule()+"', numero='"+compte.getNumero()+"', type='"+compte.getType()+"', libelle='"+compte.getLibelle()+"', solde='"+compte.getSolde()+"', decouvert="+decouvert+", taux="+taux+" ;"
						);
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void supprimer(List<String> intitules) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			for(String intitule:intitules){
				Statement statement = bdd.createStatement();
				statement.execute("DELETE FROM clients where intitule='"+intitule+"';");
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toutSupprimer() {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			Statement statement = bdd.createStatement();
			statement.execute("DELETE FROM comptes;");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

}
