package algo2;

import java.util.ArrayList;
import java.util.Collections;

import outils.TabInt;

public class Dag {

	private TabInt resultats;	//TODO : faire les cas où les résultats sont non nulls
	private int[][] facteurs;
	private ArrayList<Cellule> listVecteurs;

	public Dag(int[][] facteurs, TabInt res){
		this.resultats = res;
		this.facteurs = facteurs;
		ArrayList<Cellule> vecteurs = new ArrayList<Cellule>();
		for (int i=0; i<facteurs[0].length; i++){
			TabInt variable = new TabInt(facteurs[0].length);
			variable.setRes(i, 1);
			Cellule c = new Cellule(facteurs, variable, resultats);
			vecteurs.add(c);
		}
		this.listVecteurs = vecteurs;
		//System.out.println("vecteurs : " + listVecteurs);
	}


	public ArrayList<Cellule> solMinGlouton(){
		int nbVariable = facteurs[0].length;
		ArrayList<Cellule> solMin = new ArrayList<Cellule>();
		ArrayList<Cellule> file = new ArrayList<Cellule>();
		for (int i=0; i<nbVariable; i++){		// ajout des vecteurs simples dans la file
			Cellule c = listVecteurs.get(i);
			file.add(c);
			if (c.resultatNull())
				solMin.add(c);
		}
		//System.out.println("solMinG, visitées : " + visitée);
		//System.out.println("file début : " + file);

		while(!file.isEmpty()){
			Cellule c = file.get(0);
			file.remove(0);
			Cellule res;
			for (int i = 0; i<listVecteurs.size(); i++){
				res = listVecteurs.get(i);
				//System.out.println("cellule : " + res + " parent : " + c);
				if (res.estValide(c)){
					Cellule fille = c.ajoutVecteurGlouton(res, facteurs, resultats);
					if (fille.resultatNull()){
						if (!fille.estPresentSol(solMin)){
							solMin.add(fille);
							//System.out.println("SOL MIN : " + fille.getValeur());
						}
					}
					else {
						boolean ajout = true;
						Cellule min = null;
						if (!solMin.isEmpty()){
							for (int j=0; j<solMin.size(); j++){
								min = solMin.get(j);
								//System.out.println("min choisi : " + min + " à ajouter : " + fille);
								if(fille.estPlusGrand(min)){
									ajout = false;
									//System.out.println("plus grand");
								}
							}
						}	
						if (ajout){
							//System.out.println("entree condition valide");
							file.add(fille);
							//System.out.println("file : " + file);
						}
					}
				}
			}	
		}
		//System.out.println("sol visitées : " + visitée);
		//System.out.println("solMin : " + solMin);
		return solMin;
	}

	public ArrayList<Cellule> solMinOrdre(){
		//long startTime = System.currentTimeMillis();

		int nbVariable = facteurs[0].length;
		ArrayList<Cellule> solMin = new ArrayList<Cellule>();
		ArrayList<Cellule> file = new ArrayList<Cellule>();
		for (int i=0; i<nbVariable; i++){
			boolean[] freeze = new boolean[nbVariable];
			for (int j=i+1; j<nbVariable; j++)
				freeze[j] = true;
			Cellule c = new Cellule(listVecteurs.get(i), freeze);
			file.add(c);
			if (c.resultatNull())
				solMin.add(c);
		}
		//System.out.println("file début : " + file);

		while(!file.isEmpty()){
			Cellule c = file.get(0);
			file.remove(0);
			Cellule res;
			boolean[] freeze = c.getFreeze();
			//System.out.println("cellule courante : " + c);
			for (int i=0; i<nbVariable; i++){
				if (freeze[i] == false){
					res = listVecteurs.get(i);
					//System.out.println("vecteur à ajouter: " + res);
					if (res.estValide(c)){
						Cellule fille = c.ajoutVecteurFreeze(res, facteurs, resultats, freeze);
						freeze[i] = true;
						//System.out.println("ajouté : " + fille);
						if (fille.resultatNull()){
							if (!fille.estPresentSol(solMin)){
								solMin.add(fille);
							}
						}
						else {
							boolean ajout = true;
							Cellule min = null;
							if (!solMin.isEmpty()){
								for (int j=0; j<solMin.size(); j++){
									min = solMin.get(j);
									//System.out.println("min choisi : " + min + " à ajouter : " + fille);
									if(fille.estPlusGrand(min)){
										ajout = false;
										//System.out.println("plus grand");
									}
								}
							}	
							if (ajout){
								file.add(fille);
								//System.out.println("file : " + file);
							}
							else{
								//	System.out.println("files : " + file);
							}
						}
					}
				}
			}
			
		}
		return solMin;	
	}

	public ArrayList<Cellule> solMinPile(){
		int nbVal = facteurs[0].length;
		ArrayList<Cellule> solMin = new ArrayList<Cellule>();
		ArrayList<Cellule> pile = triTailleVecteur();

		//System.out.println("listVecteurs : " + listVecteurs);
		//System.out.println("pile : " + pile);
		Cellule courant;	

		while(!pile.isEmpty()){
			//for (int k=0; k<100; k++){
			//System.out.println("\npile : " + pile);
			courant = pile.get(0);	
			pile.remove(0);			// suppression haut de pile
			//System.out.println("cellule courante : " + courant);

			if (courant.getVecteur().estNull() /*&& !courant.getValeur().estNull()*/){
				solMin.add(courant);
				//System.out.println("SOL MIN: " + courant);
			}

			else {
				boolean[] boolCourant = new boolean[courant.getFreeze().length]; // TODO: vois si on peut remplacer par =courant.getFreeze();
				for (int t=0; t<boolCourant.length; t++)
					boolCourant[t] = courant.getFreeze(t);
				
				
				for (int i = 0; i<nbVal; i++){
					Cellule vect = listVecteurs.get(i);
					//System.out.println(" vecteur " + i + " : " + vect);
					//System.out.println("validité : " + (vect.estValide(courant)) + " " +  (boolCourant[i]==false));
					if (boolCourant[i] == false && vect.estValide(courant) ){ 
						Cellule fille = courant.ajoutVecteurFreeze(vect, facteurs, resultats, boolCourant); 
						//System.out.println("cellule fille : " + fille);

						boolean ajout = true;
						Cellule min = null;
						//System.out.println("solMin vide ? " + solMin.isEmpty());
						if (!solMin.isEmpty()){	// on regarde si on peut comparer avec les sol déjà connues
							for (int j=0; j<solMin.size(); j++){
								min = solMin.get(j);
								//System.out.println("min choisi : " + min + " à ajouter : " + fille);
								if(fille.estPlusGrand(min)){
									ajout = false;
									//System.out.println("plus grand");
								}
							}
						}
						
						if (ajout){
							pile.add(0, fille);
							boolCourant[i] = true;
							//System.out.println("ajout cellule fille " + fille);
						}	

					}
				}
			}
		}
		return solMin;
	}



	public ArrayList<Cellule> triTailleVecteur(){
		int nbVal = facteurs[0].length;
		ArrayList<Cellule> pile = new ArrayList<Cellule>(); 
		for (int v = 0; v<listVecteurs.size(); v++)
			pile.add(listVecteurs.get(v));
		Collections.sort(pile);

		for (int f = nbVal-1; f >= 0; f--){	// mise à jour des variables freezees
			int g=0;	// indice de variable
			int freeze = 0;	// nb de freez fait
			int indicePile = nbVal - f-1;
			Cellule vect = pile.get(indicePile);
			while(freeze<f){
				if (vect.getValeur(g) == 0){
					pile.get(indicePile).setFreeze(g, true);
					freeze++;
				}
				g++;
			}
		}
		return pile;
	}

}
