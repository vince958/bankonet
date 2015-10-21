package com.bankonet.utils;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.bankonet.utils.exception.CompteException;
import com.bankonet.utils.exception.DebitException;
import com.bankonet.utils.others.TypeCompte;

@Entity
@Table(name="comptes")
@NamedQueries({
	@NamedQuery(name="comptes.findCompteByIntitule", query="select c from Compte c where c.intitule=:intitule"),
	@NamedQuery(name="comptes.findAllComptes", query="select c from Compte c")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Compte implements CompteStat {
	@Id
	@GeneratedValue
	private int id;
	private static int nbTotal = 0;
	@Transient @Enumerated(EnumType.STRING) private TypeCompte type;
	@Column(length = 200, nullable = false, unique = true)
	private String numero;
	@Column(length = 200, nullable = false, unique = true)
	private String intitule;
	@Column(length = 200, nullable = false, unique = true)
	private String libelle;
	@Column
	private double solde;
	
	public Compte(){}
	
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

	public int getId(){return id;}
	public String getIntitule(){return intitule;}
	public String getNumero(){return numero;}
	public double getSolde(){return solde;}
	public TypeCompte getType(){return type;}
	public int getNbTotal(){ return nbTotal; }
	public String getLibelle(){ return libelle; }
	
	public static void setNbTotal(int nb){ nbTotal = nb; }
	public void setId(int pid){id = pid;}
	public void setIntitule(String pintitule){intitule = pintitule;}
	public void setNumero(String pnumero){numero = pnumero;}
	public void setSolde(double psolde){solde = psolde;}
	public void setType(TypeCompte ptype){type = ptype;}
	public void setLibelle(String plibelle){libelle = plibelle; }
}
