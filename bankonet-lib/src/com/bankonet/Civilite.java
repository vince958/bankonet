package com.bankonet;

public enum Civilite {
	MONSIEUR("Monsieur"), MADAME("Madame"), MADEMOISELLE("Mademoiselle");
	
	private String value = "";
	
	private Civilite(String pvalue){
		value = pvalue;
	}
	
	public String getValue(){ return value; }
	public static final Civilite getCivilite(String civilite){
		if(civilite.equals("Monsieur") || civilite.equals("MONSIEUR")) return Civilite.MONSIEUR;
		if(civilite.equals("Madame") || civilite.equals("MADAME")) return Civilite.MADAME;
		if(civilite.equals("Mademoiselle") || civilite.equals("MADEMOISELLE")) return Civilite.MADEMOISELLE;
		else return null;
	}
}
