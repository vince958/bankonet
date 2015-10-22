package com.bankonet.command;

import com.bankonet.metier.InitService;

public class InitClientsCommand extends IhmCommand{

	private static final int id = 6;
	private static final String libelle = "Init Clients";
	private InitService initService;
	
	public InitClientsCommand(InitService pinitService) {
		initService = pinitService;
	}
	
	@Override
	public void execute() {
		initService.init();
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
