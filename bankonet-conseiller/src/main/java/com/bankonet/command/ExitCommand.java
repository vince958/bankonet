package com.bankonet.command;

public class ExitCommand extends IhmCommand{

	private static final int id = 0;
	private static final String libelle = "Arreter le programme";
	
	@Override
	public void execute() {
		System.out.println("Arret de l'application");
		System.exit(0);
	}
	
	@Override
	public Integer getId() {return id;}
	@Override
	public String getLibelle() {return libelle;}
}
