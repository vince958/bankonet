package com.bankonet;

public enum TypeCompte {
	COURANT("Courant", "CC"), EPARGNE("Epargne", "CE");
	
	private String value = "";
	private String diminutif = "";
	
	private TypeCompte(String pvalue, String pdiminutif){
		value = pvalue;
		diminutif = pdiminutif;
	}
	
	public String getValue(){ return value; }
	public String getDiminutif(){ return diminutif; }
	
	public static final TypeCompte getType(String type){
		if(type.equals("COURANT") || type.equals("Courant") || type.equals("CC")) return TypeCompte.COURANT;
		if(type.equals("EPARGNE") || type.equals("Epargne") || type.equals("CE")) return TypeCompte.EPARGNE;
		else return null;
	}
}
