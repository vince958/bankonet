package com.bankonet.command;

import com.bankonet.metier.InitService;
import com.bankonet.utils.others.InputSingleton;

public class SupprimerCommand extends IhmCommand{

	private static final int id = 9;
	private static final String libelle = "Supprimer un client";
	private InitService initService;
	private InputSingleton input = InputSingleton.getInstance();
	
	public SupprimerCommand(InitService pinitService) {
		initService = pinitService;
	}
	
	@Override
	public void execute() {
		String login = input.readString("Entrez le login du client: ");
		initService.supprimerClient(login);
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
