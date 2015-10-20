package com.bankonet.utils;

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bankonet.utils.others.Civilite;

@Entity
@Table(name="clients")
public class Client {
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "prenom", length = 200, nullable = false, unique = true)
	private String prenom;
	@Column(name="password", length = 200, nullable = false)
	private String mdp = "secret";
	@Column(length = 200, nullable = false)
	private String nom;
	@Column(length = 200, nullable = false)
	private String login;
	@Transient
	private Civilite civilite;
	@Transient
	private ArrayList<Compte> comptesList;
	
	public Client(){}
	
	public Client(String plogin, String pmdp, Civilite pcivilite , String pnom, String pprenom){
		civilite = pcivilite;
		mdp = pmdp;
		prenom = pprenom;
		nom = pnom;
		login = plogin;
		
		comptesList = new ArrayList<Compte>();
	}
	
	public String toString(){
		String comptes = consulterComptes();
		comptes = 	"Civilite: "+civilite+"\n"+
					"Nom: "+nom+"\n"+
					"Prenom: "+prenom+"\n"+
					"Identifiant: "+login+"\n"+comptes;
		return comptes;
	}
	
	public String consulterComptes(){
		String comptes = "Comptes Associes: \n{\n";
		for(Compte i:comptesList){
			if(i.getType().equals("Courant"))
				comptes += i.getLibelle()+" - Solde: "+i.getSolde()+" - Decouvert Autorise: "+((CompteCourant)i).getMontantDecouvertAutorise()+"\n";
			else
				comptes += i.getLibelle()+" - Solde: "+i.getSolde()+" - Taux d'interet: "+((CompteEpargne)i).getTauxInteret()+"\n";
		}
		comptes += "}\n";
		return comptes;
	}
	
	public double calculerAvoirGlobal(){
		double avoir = 0;
		for(Compte i:comptesList)
			avoir+=i.getSolde();
		return avoir;
	}
	
	public void creerCompte(Compte compte){
		comptesList.add(compte);
	}
	
	public void supprimerCompte(Compte compte){
		comptesList.remove(compte.getNumero());
	}
	
	public String genererSauvegarde(){
		String comptes = "";
		for(Compte i:comptesList)
			comptes+=i.getIntitule()+",";
		return login+"=mdp:"+mdp+"&nom:"+nom+"&prenom:"+prenom+"&civilite:"+civilite+"&comptes:"+comptes.substring(0, comptes.length()-1)+"\n";
	}
	
	public ArrayList<Compte> getComptesList(){ return comptesList; }
	
	public int getId(){return id;}
	public String getLogin(){return login;}
	public String getNom(){return nom;}
	public String getPrenom(){return prenom;}
	public Civilite getCivilite(){return civilite;}
	public String getMdp(){return mdp;}
	
	public void setId(int pid){id = pid;}
	public void setLogin(String plogin){login = plogin;}
	public void setNom(String pnom){nom = pnom;}
	public void setPrenom(String pprenom){prenom = pprenom;}
	public void setCivilite(Civilite pcivilite){civilite = pcivilite;}
	public void setMdp(String pmdp){mdp = pmdp;}
}
