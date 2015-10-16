package com.bankonet.compte;

import com.bankonet.TypeCompte;
import com.bankonet.exception.CompteException;
import com.bankonet.exception.CreditException;

public class CompteEpargne extends Compte{
	
	private static final double PLAFOND = 12000;
	
	private double tauxInteret;
	private static int nbComptesEpargnes = 0;
	
	public CompteEpargne(String nom, String prenom, double psolde, double ptauxInteret){
		super(TypeCompte.EPARGNE, nom, prenom, psolde);
		nbComptesEpargnes++;
		tauxInteret = ptauxInteret;
	}
	
	public CompteEpargne(String pnumero, TypeCompte ptype, String pintitule, String plibelle, double psolde, double ptauxInteret){
		super(pnumero, ptype, pintitule, plibelle, psolde);
		tauxInteret = ptauxInteret;
	}
	
	public String toString(){
		return 
				"Compte Epargne: \n"+
				super.toString()+
				"Taux Interet: "+tauxInteret+"\n"+
				"Plafond: "+PLAFOND+"\n";
	}
	
	public double calculerDebitMaximum(){
		return getSolde();
	}
	
	@Override
	public void crediter(double montant) throws CompteException {
		if(getSolde() + montant > PLAFOND) {
			throw new CreditException("Plafond atteind "+ PLAFOND);
		}
		super.crediter(montant);
	}
	
	public String genererSauvegarde(){
		return super.genererSauvegarde()+"&taux:"+tauxInteret+"\n";
	}
	
	public double getTauxInteret(){ return tauxInteret; }
	public int getCount(){return nbComptesEpargnes; }
	public static void setNbComptesEpargnes(int nb){ nbComptesEpargnes = nb; }
}
