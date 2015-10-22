package com.bankonet.app;

import java.util.Arrays;
import java.util.List;

import com.bankonet.command.DepotCommand;
import com.bankonet.command.ExitCommand;
import com.bankonet.command.IhmCommand;
import com.bankonet.command.ListeComptesCommand;
import com.bankonet.command.RetraitCommand;
import com.bankonet.command.VirementInterneCommand;
import com.bankonet.dao.DaoFactory;
import com.bankonet.dao.DaoFactoryJpa;
import com.bankonet.metier.ClientService;
import com.bankonet.utils.others.InputSingleton;

public class CommandApp {
	
	//private static DaoFactory factory = new DaoFactoryFile("../bankonet-lib/clients.properties", "../bankonet-lib/comptes.properties");
	//private static DaoFactory factory = new DaoFactorySQL("jdbc:mysql://localhost/bankonet", "root", "poupette");
	private static DaoFactory factory = new DaoFactoryJpa("bankonet-lib");
	private ClientService clientService;
	private String login;
	private InputSingleton input = InputSingleton.getInstance();
	private List<IhmCommand> commandList;
	
	public CommandApp() {
		clientService = new ClientService(factory.getClientDao(), factory.getCompteDao());
		login = ouvrirSession();
		commandList = Arrays.asList(	
				new ExitCommand(),
				new ListeComptesCommand(clientService, login),
				new DepotCommand(clientService, login),
				new RetraitCommand(clientService, login),
				new VirementInterneCommand(clientService, login)
			);
		System.out.print("\n***** APPLICATION CLIENT *****");
	}
	
	public String ouvrirSession(){
		boolean state = true;
		String login = null;
		while(state){
			login = input.readString("Login: ");
			String mdp = input.readString("Mot de Passe: ");
			
			if(clientService.connexionClient(login, mdp)){
				state = false;
			}else
				System.out.println("Login ou mdp incorrect!");
		}
		return login;
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

