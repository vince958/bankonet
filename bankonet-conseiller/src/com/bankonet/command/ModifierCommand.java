package com.bankonet.command;

import java.util.Scanner;

import com.bankonet.metier.InitService;

public class ModifierCommand extends IhmCommand{

	private static final int id = 8;
	private static final String libelle = "Modifier un client";
	private InitService initService;
	private Scanner input;
	
	public ModifierCommand(InitService pinitService, Scanner pinput) {
		initService = pinitService;
		input = pinput;
	}
	
	@Override
	public void execute() {
		System.out.println("Entrez le login du client: ");
		String login = input.nextLine();
		System.out.println("Entrez le nouveau nom du client: ");
		String nom = input.nextLine();
		System.out.println("Entrez le nouveau prenom du client: ");
		String prenom = input.nextLine();
		initService.modifierClient(login, nom, prenom);
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
