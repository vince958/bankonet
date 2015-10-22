package com.bankonet.utils;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.bankonet.utils.exception.CompteException;
import com.bankonet.utils.exception.CreditException;
import com.bankonet.utils.others.TypeCompte;

@Entity
@DiscriminatorValue("EPARGNE")
public class CompteEpargne extends Compte{
	
	private static final long serialVersionUID = 1L;
	private static final double PLAFOND = 12000;
	@Column(name="taux", length = 200, nullable = false)
	private double tauxInteret;
	private static int nbComptesEpargnes = 0;
	
	public CompteEpargne(){}
	
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
	
	public void setTauxInteret(double ptauxInteret){tauxInteret = ptauxInteret;}
}
