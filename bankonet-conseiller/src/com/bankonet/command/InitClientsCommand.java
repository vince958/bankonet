package com.bankonet.command;

import com.bankonet.metier.InitService;

public class InitClientsCommand implements IhmCommand{

	private static final int id = 6;
	private static final String libelle = "Ouvrir un compte courant";
	private InitService initService;
	
	public InitClientsCommand(InitService pinitService) {
		initService = pinitService;
	}
	
	@Override
	public void execute() {
		System.out.println("test");
		initService.init();
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
