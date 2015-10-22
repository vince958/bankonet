package com.bankonet.app;

import java.util.Arrays;
import java.util.List;

import com.bankonet.command.AjouterCompteCourantCommand;
import com.bankonet.command.AjouterCompteEpargneCommand;
import com.bankonet.command.AutoriserDecouvertCommand;
import com.bankonet.command.ExitCommand;
import com.bankonet.command.IhmCommand;
import com.bankonet.command.InitClientsCommand;
import com.bankonet.command.ListerClientsCommand;
import com.bankonet.command.ModifierCommand;
import com.bankonet.command.OuvrirCompteCourantCommand;
import com.bankonet.command.RechercherCommand;
import com.bankonet.command.SupprimerCommand;
import com.bankonet.command.SupprimerTousCommand;
import com.bankonet.dao.DaoFactory;
import com.bankonet.dao.DaoFactoryJpa;
import com.bankonet.metier.ClientService;
import com.bankonet.metier.InitService;
import com.bankonet.utils.others.InputSingleton;

public class CommandApp {
	
	//private static DaoFactory factory = new DaoFactoryFile("../bankonet-lib/clients.properties", "../bankonet-lib/comptes.properties");
	//private static DaoFactory factory = new DaoFactorySQL("jdbc:mysql://localhost/bankonet", "root", "poupette");
	private static DaoFactory factory = new DaoFactoryJpa("bankonet-lib");
	private ClientService clientService;
	private InitService initService;
	private InputSingleton input = InputSingleton.getInstance();
	private List<IhmCommand> commandList;
	
	public CommandApp() {
		clientService = new ClientService(factory.getClientDao(), factory.getCompteDao());
		initService = new InitService(factory.getClientDao(), factory.getCompteDao());
		commandList = Arrays.asList(	
				new ExitCommand(),
				new OuvrirCompteCourantCommand(clientService),
				new ListerClientsCommand(clientService),
				new AjouterCompteCourantCommand(clientService),
				new AjouterCompteEpargneCommand(clientService),
				new AutoriserDecouvertCommand(clientService),
				new InitClientsCommand(initService),
				new RechercherCommand(initService),
				new ModifierCommand(initService),
				new SupprimerCommand(initService),
				new SupprimerTousCommand(initService)
			);
		System.out.print("\n***** APPLICATION CONSEILLER BANCAIRE *****");
	}
	
	public void menu(){
		while(true){
			System.out.println("\n\n");
			for(IhmCommand command:commandList)
				System.out.println(command.getId()+". "+command.getLibelle());
			int choix = input.readInt("Action: ", 0, commandList.size()-1);
			
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
