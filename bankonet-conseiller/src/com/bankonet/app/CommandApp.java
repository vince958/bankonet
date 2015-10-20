package com.bankonet.app;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.bankonet.command.AjouterCompteCourantCommand;
import com.bankonet.command.AjouterCompteEpargneCommand;
import com.bankonet.command.AutoriserDecouvertCommand;
import com.bankonet.command.ExitCommand;
import com.bankonet.command.IhmCommand;
import com.bankonet.command.ListerClientsCommand;
import com.bankonet.command.OuvrirCompteCourantCommand;
import com.bankonet.dao.DaoFactory;
import com.bankonet.dao.DaoFactorySQL;
import com.bankonet.metier.ClientService;

public class CommandApp {
	
	//private static DaoFactory factory = new DaoFactoryFile("../bankonet-lib/clients.properties", "../bankonet-lib/comptes.properties");
	private static DaoFactory factory = new DaoFactorySQL("jdbc:mysql://localhost/bankonet", "root", "poupette");
	private ClientService clientService;
	private Scanner input;
	private List<IhmCommand> commandList;
	
	public CommandApp() {
		clientService = new ClientService(factory.getClientDao(), factory.getCompteDao());
		input = new Scanner(System.in);
		commandList = Arrays.asList(	
				new ExitCommand(),
				new OuvrirCompteCourantCommand(clientService, input),
				new ListerClientsCommand(clientService),
				new AjouterCompteCourantCommand(clientService, input),
				new AjouterCompteEpargneCommand(clientService, input),
				new AutoriserDecouvertCommand(clientService, input)
			);
		System.out.print("\n***** APPLICATION CONSEILLER BANCAIRE *****");
	}
	
	public void menu(){
		while(true){
			System.out.println("\n\n");
			for(IhmCommand command:commandList)
				System.out.println(command.getId()+". "+command.getLibelle());
			System.out.println("Action: ");
			int choix = input.nextInt();
			input.nextLine();
			
			for(IhmCommand command:commandList)
				if(command.getId() == choix)
					command.execute();
		}
	}
	
	public static void main(String[] args) {
		CommandApp command = new CommandApp();
		command.menu();
	}
}
