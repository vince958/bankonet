package com.bankonet.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.bankonet.metier.Compte;
import com.bankonet.metier.CompteCourant;
import com.bankonet.metier.CompteEpargne;
import com.bankonet.metier.utils.TypeCompte;

public class CompteDaoFile implements CompteDao {

	private String comptesPropertiesPath;
	
	public CompteDaoFile(String pcomptesPropertiesPath){
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
	
	@Override
	public ArrayList<Compte> chargerComptes(String[] comptesString) {
		ArrayList<Compte> comptesRetour = new ArrayList<Compte>();
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
						comptesRetour.add(new CompteCourant(numero, type, intitule, libelle, solde, montantDecouvertAutorise));
					}else if(type.getValue().equals("Epargne")){
						double tauxInteret = Double.parseDouble(ligneCompte.split("taux:")[1].split("&")[0]);
						comptesRetour.add(new CompteEpargne(numero, type, intitule, libelle, solde, tauxInteret));
					}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return comptesRetour;
	}

	@Override
	public void ajouterModifier(ArrayList<Compte> comptesList) {
		File fichierSav = new File(comptesPropertiesPath+".save");
		File fichierBase = new File(comptesPropertiesPath);
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
	}

	@Override
	public void supprimer(String[] ident) {
		// TODO Auto-generated method stub
		
	}

}
