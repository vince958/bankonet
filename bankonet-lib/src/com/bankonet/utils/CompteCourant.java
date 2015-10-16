package com.bankonet.utils;

import com.bankonet.utils.others.TypeCompte;

public class CompteCourant extends Compte {
	
	private double montantDecouvertAutorise;
	private static int nbComptesCourants = 0;
	
	public CompteCourant(String nom, String prenom, double psolde, double pmontantDecouvertAutorise){
		super(TypeCompte.COURANT, nom, prenom, psolde);
		nbComptesCourants++;
		montantDecouvertAutorise = pmontantDecouvertAutorise;
	}
	
	public CompteCourant(String pnumero, TypeCompte ptype, String pintitule, String plibelle, double psolde, double pmontantDecouvertAutorise){
		super(pnumero, ptype, pintitule, plibelle, psolde);
		montantDecouvertAutorise = pmontantDecouvertAutorise;
	}
	
	@Override
	public String toString(){
		return 
				"Compte Courant: \n"+
				super.toString()+
				"Montant Decouvert Autorise: "+montantDecouvertAutorise+"\n";
	}
	
	public double calculerDebitMaximum(){
		return getSolde() + montantDecouvertAutorise;
	}
	
	public String genererSauvegarde(){
		return super.genererSauvegarde()+"&decouvert:"+montantDecouvertAutorise+"\n";
	}
	
	public int getCount(){return nbComptesCourants;}
	public double getMontantDecouvertAutorise(){return montantDecouvertAutorise;}
	public static void setNbComptesCourants(int nb){ nbComptesCourants = nb; }
	public void setDecouvertAutorise(double d){ montantDecouvertAutorise = d; }
}
