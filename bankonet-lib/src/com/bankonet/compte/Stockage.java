package com.bankonet.compte;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.bankonet.Civilite;
import com.bankonet.TypeCompte;

public class Stockage {
	
	private String clientsPropertiesPath;
	private String comptesPropertiesPath;
	
	public Stockage(String pclientsPropertiesPath, String pcomptesPropertiesPath){
		clientsPropertiesPath = pclientsPropertiesPath;
		comptesPropertiesPath = pcomptesPropertiesPath;
		
		int nbTotal = 0;
		int nbCourant = 0;
		int nbEpargne = 0;
		
		try(	FileReader comptes = new FileReader(comptesPropertiesPath);
				BufferedReader input_comptes = new BufferedReader(comptes);){
			String ligneCompte;
			while ((ligneCompte=input_comptes.readLine())!=null){
				TypeCompte type = TypeCompte.getType(ligneCompte.split("type:")[1].split("&")[0]);
				if(type.getValue().equals("Courant"))
					nbCourant++;
				else if(type.getValue().equals("Epargne"))
					nbEpargne++;
				nbTotal++;
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		Compte.setNbTotal(nbTotal);
		CompteCourant.setNbComptesCourants(nbCourant);
		CompteEpargne.setNbComptesEpargnes(nbEpargne);
	}
	
	
	public void ajouterModifier(Client client){
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
				if(identifiant.equals(client.getId())){
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
		
		
		fichierSav = new File(comptesPropertiesPath+".save");
		fichierBase = new File(comptesPropertiesPath);
		
		ArrayList<Compte> comptesList = client.getComptesList();
		ArrayList<String> idList = new ArrayList<String>();
		for(Compte i:comptesList){
			if(i.getType().equals("Courant")) idList.add(((CompteCourant)i).genererSauvegarde());
			else if(i.getType().equals("Epargne")) idList.add(((CompteEpargne)i).genererSauvegarde());
		}
		try(	FileReader comptesR = new FileReader(fichierBase);
				BufferedReader input_comptes = new BufferedReader(comptesR);
				FileWriter comptesW = new FileWriter(fichierSav, true);
				BufferedWriter output_comptes = new BufferedWriter(comptesW);){
		
			String ligne;
			while ((ligne=input_comptes.readLine())!=null){
				int num = isSame(ligne, idList);
				if(num > -1){
					output_comptes.write(idList.get(num));
					output_comptes.flush();
					idList.remove(num);
				}else{
					output_comptes.write(ligne+"\n");
					output_comptes.flush();
				}
			}
			for(int i = 0; i < idList.size(); i++){
				output_comptes.write(idList.get(i));
				output_comptes.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fichierBase.delete();
		fichierSav.renameTo(fichierBase);
		
		//////////
		/*
		try(	FileWriter clients = new FileWriter(clientsPropertiesPath, true);
				BufferedWriter output_clients = new BufferedWriter(clients);
				FileWriter comptes = new FileWriter(comptesPropertiesPath, true);
				BufferedWriter output_comptes = new BufferedWriter(comptes);){
		
			output_clients.write(client.genererSauvegarde());
			output_clients.flush();
			
			HashMap<String, Compte> comptesList = client.getComptesList();
			for(Compte i:comptesList.values()){
				if(i.getType().equals("Courant")) output_comptes.write(((CompteCourant)i).genererSauvegarde());
				else if(i.getType().equals("Epargne")) output_comptes.write(((CompteEpargne)i).genererSauvegarde());
				output_clients.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	private int isSame(String s, ArrayList<String> sTab){
		for(int i = 0; i < sTab.size(); i++)
			if(s.split("=")[0].equals(sTab.get(i).split("=")[0])) return i;
		return -1;
	}
	
	private boolean isIn(String s, String[] sTab){
		for(int i = 0; i < sTab.length; i++){
			if(s.equals(sTab[i])) return true;
		}
		return false;
	}
	
	public String afficherTousLesClient(){
		String retour = "";
		try(	FileReader clients = new FileReader(clientsPropertiesPath);
				BufferedReader input_clients = new BufferedReader(clients);){
		
			String ligne;
			while ((ligne=input_clients.readLine())!=null){
				int nbCompteEpargne = 0;
				int nbCompteCourant = 0;
				String identifiant = ligne.split("=")[0];
				String nom = ligne.split("nom:")[1].split("&")[0];
				String prenom = ligne.split("prenom:")[1].split("&")[0];
				
				String[] comptesString = ligne.split("comptes:")[1].split(",");
				try(	FileReader comptes = new FileReader(comptesPropertiesPath);
						BufferedReader input_comptes = new BufferedReader(comptes);){
					String ligneCompte;
					while ((ligneCompte=input_comptes.readLine())!=null){
						String intitule = ligneCompte.split("=")[0];
						if(isIn(intitule, comptesString)){
							TypeCompte type = TypeCompte.getType(ligneCompte.split("type:")[1].split("&")[0]);
							if(type.getValue().equals("Courant"))
								nbCompteCourant++;
							else if(type.getValue().equals("Epargne"))
								nbCompteEpargne++;
						}
					}
				}catch (IOException e) {
					e.printStackTrace();
				}
				retour += "\nIdentifiant: "+identifiant+" / Nom: "+nom+" / Prenom: "+prenom+" / Courant:"+nbCompteCourant+" / Epargne:"+nbCompteEpargne;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retour;
	}
	
	public ArrayList<String[]> retournerClients(){
		ArrayList<String[]> clientsString = new ArrayList<String[]>();
		try(	FileReader clients = new FileReader(clientsPropertiesPath);
				BufferedReader input_clients = new BufferedReader(clients);){
		
			String ligne;
			while ((ligne=input_clients.readLine())!=null){
				int nbCompteEpargne = 0;
				int nbCompteCourant = 0;
				String identifiant = ligne.split("=")[0];
				String nom = ligne.split("nom:")[1].split("&")[0];
				String prenom = ligne.split("prenom:")[1].split("&")[0];
				
				String[] comptesString = ligne.split("comptes:")[1].split(",");
				try(	FileReader comptes = new FileReader(comptesPropertiesPath);
						BufferedReader input_comptes = new BufferedReader(comptes);){
					String ligneCompte;
					while ((ligneCompte=input_comptes.readLine())!=null){
						String intitule = ligneCompte.split("=")[0];
						if(isIn(intitule, comptesString)){
							TypeCompte type = TypeCompte.getType(ligneCompte.split("type:")[1].split("&")[0]);
							if(type.getValue().equals("Courant"))
								nbCompteCourant++;
							else if(type.getValue().equals("Epargne"))
								nbCompteEpargne++;
						}
					}
				}catch (IOException e) {
					e.printStackTrace();
				}
				String[] client = new String[2];
				client[0] = "\nIdentifiant: "+identifiant+" / Nom: "+nom+" / Prenom: "+prenom+" / Courant:"+nbCompteCourant+" / Epargne:"+nbCompteEpargne;
				client[1] = identifiant;
				clientsString.add(client);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return clientsString;
	}
	
	public Client chargerClient(String login){
		Client client = null;
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
					client = new Client(identifiant, mdp, civilite, nom, prenom);
					
					String[] comptesString = ligne.split("comptes:")[1].split(",");
					try(	FileReader comptes = new FileReader(comptesPropertiesPath);
							BufferedReader input_comptes = new BufferedReader(comptes);){
						String ligneCompte;
						while ((ligneCompte=input_comptes.readLine())!=null){
							String intitule = ligneCompte.split("=")[0];
							if(isIn(intitule, comptesString)){
								String numero = ligneCompte.split("numero:")[1].split("&")[0];
								String libelle = ligneCompte.split("libelle:")[1].split("&")[0];
								double solde = Double.parseDouble(ligneCompte.split("solde:")[1].split("&")[0]);
								TypeCompte type = TypeCompte.getType(ligneCompte.split("type:")[1].split("&")[0]);
								if(type.getValue().equals("Courant")){
									double montantDecouvertAutorise = Double.parseDouble(ligneCompte.split("decouvert:")[1].split("&")[0]);
									client.creerCompte(new CompteCourant(numero, type, intitule, libelle, solde, montantDecouvertAutorise));
								}
								else if(type.getValue().equals("Epargne")){
									double tauxInteret = Double.parseDouble(ligneCompte.split("taux:")[1].split("&")[0]);
									client.creerCompte(new CompteEpargne(numero, type, intitule, libelle, solde, tauxInteret));
								}
							}
						}
					}catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return client;
	}
	
	public boolean connexionClient(String login, String pmdp){
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
	
	public void supprimer(String login){
		
	}
}
/*
String line;
StringBuffer sb = new StringBuffer();
int nbLinesRead = 0;
try {
    FileInputStream fis = new FileInputStream("C://Users//user//Documents//NetBeansProjects//jlis//src//jlis//listenoire.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
    while ((line = reader.readLine()) != null) {
        nbLinesRead++;
        line = line.toLowerCase();
        if (nbLinesRead == quel_ligne_supprimer) {
            line = line.replaceFirst(line, "");
        }
        sb.append(line+"n");
    }
    reader.close();
    //ici j'écrit le même fichier dans le même fichier sans la ligne supprimer
    BufferedWriter out = new BufferedWriter(new FileWriter("C://Users//user//Documents//NetBeansProjects//jlis//src//jlis//listenoire.txt"));
    out.write(sb.toString());
    out.close();
*/