package com.bankonet.command;

import com.bankonet.metier.InitService;

public class SupprimerTousCommand extends IhmCommand{

	private static final int id = 10;
	private static final String libelle = "Supprimer TOUS les clients";
	private InitService initService;
	
	public SupprimerTousCommand(InitService pinitService) {
		initService = pinitService;
	}
	
	@Override
	public void execute() {
		initService.supprimerTous();
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
