package com.bankonet.metier;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.bankonet.metier.utils.Civilite;
import com.bankonet.metier.utils.ToString;

public class Client {
	private String mdp = "secret";
	@ToString private String prenom;
	@ToString(upperCase = true) private String nom;
	@ToString private Civilite civilite;
	@ToString private String identifiant;
	private ArrayList<Compte> comptesList;
	
	public Client(String pidentifiant, String pmdp, Civilite pcivilite , String pnom, String pprenom){
		civilite = pcivilite;
		mdp = pmdp;
		prenom = pprenom;
		nom = pnom;
		identifiant = pidentifiant;
		
		comptesList = new ArrayList<Compte>();
	}
	
	public String toString(){
		String comptes = consulterComptes();
		
		for(Field champ:Client.class.getDeclaredFields()){
			ToString ann = champ.getAnnotation(ToString.class);
			if(ann != null)
				try {
					if(ann.upperCase()) comptes = champ.getName()+": "+(champ.get(this)+"\n").toUpperCase()+comptes;
					else comptes = champ.getName()+": "+champ.get(this)+"\n"+comptes;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
		}
		return comptes;
	}
	
	public String consulterComptes(){
		String comptes = "Comptes Associes: \n{\n";
		for(Compte i:comptesList)
			comptes += i.getLibelle()+" - "+i.getSolde()+"\n";
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
		return identifiant+"=mdp:"+mdp+"&nom:"+nom+"&prenom:"+prenom+"&civilite:"+civilite+"&comptes:"+comptes.substring(0, comptes.length()-1)+"\n";
	}
	
	public ArrayList<Compte> getComptesList(){ return comptesList; }
	public String getId(){return identifiant;}
	
	public String getNom(){return nom;}
	public String getPrenom(){return prenom;}
}
