package com.bankonet.compte;

import com.bankonet.TypeCompte;
import com.bankonet.exception.CompteException;
import com.bankonet.exception.DebitException;

public abstract class Compte implements CompteStat {
	private static int nbTotal = 0;
	private TypeCompte type;
	private String numero;
	private String intitule;
	private String libelle;
	private double solde;
	
	public Compte(TypeCompte ptype, String pnom, String pprenom, double psolde){
		nbTotal++;
		type = ptype;
		numero = nbTotal+"";
		intitule = ptype.getDiminutif()+numero;
		libelle = pnom+"_"+pprenom+"_"+getType()+"_"+getNumero();
		if(psolde < 0){
			System.out.println("Il n'est pas possible de creer un compte avec un solde negatif!");
			solde = 0;
		}else 
			solde = psolde;
	}
	
	public Compte(String pnumero, TypeCompte ptype, String pintitule, String plibelle, double psolde){
		type = ptype;
		numero = pnumero;
		intitule = pintitule;
		libelle = plibelle;
		solde = psolde;
	}
	
	public void crediter(double credit) throws CompteException{
		solde += credit;
	}

	public void debiter(double debit) throws CompteException{
		double debitMax = calculerDebitMaximum();
		
		if(debit <= debitMax)
			solde -= debit;
		else
			throw new DebitException("Debit maximum atteint : " + debitMax);
	}
	
	public String toString(){
		return 
				"Numero: "+numero+"\n"+
				"Intitule: "+intitule+"\n"+
				"Solde: "+solde+"\n";
	}
	
	public void effectuerVirement(Compte compte, double montant) throws CompteException {
		debiter(montant);
		
		compte.crediter(montant);
	}
	
	public String genererSauvegarde(){
		return intitule+"=numero:"+numero+"&type:"+type+"&libelle:"+libelle+"&solde:"+solde;
	}
	
	public abstract int getCount();
	public abstract double calculerDebitMaximum();

	public String getIntitule(){return intitule;}
	public String getNumero(){return numero;}
	public double getSolde(){return solde;}
	public String getType(){return type.getValue();}
	public int getNbTotal(){ return nbTotal; }
	public String getLibelle(){ return libelle; }
	public static void setNbTotal(int nb){ nbTotal = nb; }
}
