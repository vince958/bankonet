package com.bankonet.dao.dto;

import java.util.List;

public class IdLibelleComptesDTO {
	private String id;
	private String libelle;
	private List<String> comptesList;
	
	public IdLibelleComptesDTO(String pid, String plibelle, List<String> pcomptesList) {
		id = pid;
		libelle = plibelle;
		comptesList = pcomptesList;
	}
	
	public String getId(){return id;}
	public String getLibelle(){return libelle;}
	public List<String> getComptesList(){return comptesList;}
}
