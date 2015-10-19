package com.bankonet.dao.compte;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.bankonet.utils.Compte;
import com.bankonet.utils.CompteCourant;
import com.bankonet.utils.CompteEpargne;

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
	}
	
	@Override
	public List<Compte> chargerComptes(List<String> comptesString) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}

	@Override
	public void ajouterModifier(List<Compte> comptesList) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			
			float taux;
			double decouvert;
			for(Compte compte:comptesList){
				Statement statement = bdd.createStatement();
				if(compte.getType().equals("Courant")){
					decouvert = ((CompteCourant)compte).getMontantDecouvertAutorise();
					taux = 0f;
				}else{
					decouvert = 0d;
					taux = (float)((CompteEpargne)compte).getTauxInteret();
				}
				statement.execute(
						"INSERT INTO comptes (intitule, type, libelle, solde, decouvert, taux) VALUES ('"+compte.getIntitule()+"', '"+compte.getType()+"', '"+compte.getLibelle()+"', '"+compte.getSolde()+"', "+decouvert+", "+taux+")"+
						"  ON DUPLICATE KEY UPDATE intitule='"+compte.getIntitule()+"', type='"+compte.getType()+"', libelle='"+compte.getLibelle()+"', solde='"+compte.getSolde()+"', decouvert="+decouvert+", taux="+taux+" ;"
						);
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void supprimer(List<String> ident) {
		try(Connection bdd = DriverManager.getConnection(url, user, password);) {
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
