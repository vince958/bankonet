package com.bankonet.dao.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bankonet.dao.dto.ClientComptesDTO;
import com.bankonet.dao.dto.IdLibelleComptesDTO;
import com.bankonet.utils.Client;
import com.bankonet.utils.others.Civilite;

public class ClientDaoFile implements ClientDao {

	private String clientsPropertiesPath;
	
	public ClientDaoFile(String pclientsPropertiesPath){
		clientsPropertiesPath = pclientsPropertiesPath;
	}
	
	@Override
	public void ajouterModifier(Client client) {
		boolean state = false;
		int nbLignes = 0;
		File fichierSav = new File(clientsPropertiesPath+".save");
		File fichierBase = new File(clientsPropertiesPath);
		try {
			fichierSav.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try(	FileReader clientsR = new FileReader(fichierBase);
				BufferedReader input_clients = new BufferedReader(clientsR);
				FileWriter clientsW = new FileWriter(fichierSav, true);
				BufferedWriter output_clients = new BufferedWriter(clientsW);){
		
			String ligne;
			while ((ligne=input_clients.readLine())!=null){
				nbLignes++;
				String identifiant = ligne.split("=")[0];
				if(identifiant.equals(client.getLogin())){
					state = true;
					output_clients.write(client.genererSauvegarde());
					output_clients.flush();
				}else{
					output_clients.write(ligne+"\n");
					output_clients.flush();
				}
			}
			if(!state || nbLignes==0){
				output_clients.write(client.genererSauvegarde());
				output_clients.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fichierBase.delete();
		fichierSav.renameTo(fichierBase);
	}

	@Override
	public List<IdLibelleComptesDTO> retournerIdClients() {
		List<IdLibelleComptesDTO> dto = new ArrayList<IdLibelleComptesDTO>();
		try(	FileReader clients = new FileReader(clientsPropertiesPath);
				BufferedReader input_clients = new BufferedReader(clients);){
		
			String ligne;
			while ((ligne=input_clients.readLine())!=null){
				String identifiant = ligne.split("=")[0];
				String nom = ligne.split("nom:")[1].split("&")[0];
				String prenom = ligne.split("prenom:")[1].split("&")[0];

				List<String> comptesList= new ArrayList<String>();
				String lib = "\nLogin: "+identifiant+" / Nom: "+nom+" / Prenom: "+prenom;
				for(String string:ligne.split("comptes:")[1].split(","))
					comptesList.add(string);
				dto.add(new IdLibelleComptesDTO(identifiant, lib, comptesList));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public ClientComptesDTO chargerClient(String login) {
		ClientComptesDTO dto = null;
		try(	FileReader clients = new FileReader(clientsPropertiesPath);
				BufferedReader input_clients = new BufferedReader(clients);){
		
			String ligne;
			boolean state = true;
			while ((ligne=input_clients.readLine())!=null && state){
				String identifiant = ligne.split("=")[0];
				if(identifiant.equals(login)){
					state = false;
					String nom = ligne.split("nom:")[1].split("&")[0];
					String prenom = ligne.split("prenom:")[1].split("&")[0];
					String mdp = ligne.split("mdp:")[1].split("&")[0];
					Civilite civilite = Civilite.getCivilite(ligne.split("civilite:")[1].split("&")[0]);
					
					List<String> comptesString = new ArrayList<String>();
					for(String string:ligne.split("comptes:")[1].split(","))
						comptesString.add(string);
					
					dto = new ClientComptesDTO(new Client(identifiant, mdp, civilite, nom, prenom), comptesString);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dto;
	}

	@Override
	public boolean connexionClient(String login, String pmdp) {
		boolean state = false;
		try(	FileReader clients = new FileReader(clientsPropertiesPath);
				BufferedReader input_clients = new BufferedReader(clients);){
		
			String ligne;
			while ((ligne=input_clients.readLine())!=null && !state){
				String identifiant = ligne.split("=")[0];
				if(identifiant.equals(login)){
					String mdp = ligne.split("mdp:")[1].split("&")[0];
					if(mdp.equals(pmdp))
						state = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public void supprimer(String login) {
		// A ecrire
	}

	@Override
	public List<ClientComptesDTO> rechercher(String nom, String prenom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void toutSupprimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ClientComptesDTO> getAllClients() {
		// TODO Auto-generated method stub
		return null;
	}

}
