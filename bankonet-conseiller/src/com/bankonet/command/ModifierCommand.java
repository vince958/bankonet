package com.bankonet.command;

import com.bankonet.metier.InitService;
import com.bankonet.utils.others.InputSingleton;

public class ModifierCommand extends IhmCommand{

	private static final int id = 8;
	private static final String libelle = "Modifier un client";
	private InitService initService;
	private InputSingleton input = InputSingleton.getInstance();
	
	public ModifierCommand(InitService pinitService) {
		initService = pinitService;
	}
	
	@Override
	public void execute() {
		String login = input.readString("Entrez le login du client: ");
		String nom = input.readString("Entrez le nouveau nom du client: ");
		String prenom = input.readString("Entrez le nouveau prenom du client: ");
		initService.modifierClient(login, nom, prenom);
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
