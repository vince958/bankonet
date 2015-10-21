package com.bankonet.command;

import java.util.Scanner;

import com.bankonet.metier.InitService;

public class SupprimerCommand extends IhmCommand{

	private static final int id = 9;
	private static final String libelle = "Supprimer un client";
	private InitService initService;
	private Scanner input;
	
	public SupprimerCommand(InitService pinitService, Scanner pinput) {
		initService = pinitService;
		input = pinput;
	}
	
	@Override
	public void execute() {
		System.out.println("Entrez le login du client: ");
		String login = input.nextLine();
		initService.supprimerClient(login);
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
