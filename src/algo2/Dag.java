package algo2;

import java.util.ArrayList;
import java.util.Collections;

import outils.TabInt;
import sousEspaces.ListeVecteurs;
import sousEspaces.Vecteur;

public class Dag {

	private TabInt resultats;	//TODO : faire les cas où les résultats sont non nulls
	private int[][] facteurs;
	private ListeVecteurs listVecteurs;

	public Dag(int[][] facteurs, TabInt res){
		this.resultats = res;
		this.facteurs = facteurs;
		this.listVecteurs = new ListeVecteurs(facteurs);
		//System.out.println("vecteurs : " + listVecteurs);
	}


	public SolMin solMinGlouton(){
		int nbVariable = facteurs[0].length;
		SolMin solMin = new SolMin();
		//ArrayList<Cellule> solMin = new ArrayList<Cellule>();
		ArrayList<Cellule> file = new ArrayList<Cellule>();
		for (int i=0; i<nbVariable; i++){		// ajout des vecteurs simples dans la file
			Cellule c = new Cellule(facteurs, listVecteurs.getVecteur(i).getValeur(), resultats);
			file.add(c);
			if (c.resultatNull())
				solMin.addInit(c);
			if (c.resultatHomogNull())
				solMin.addHomog(c);
		}
		//System.out.println("solMinG, visitées : " + visitée);
		//System.out.println("file début : " + file);

		while(!file.isEmpty()){
			Cellule c = file.get(0);
			file.remove(0);
			Vecteur res;
			for (int i = 0; i<listVecteurs.size(); i++){
				res = listVecteurs.getVecteur(i);
				//System.out.println("cellule : " + res + " parent : " + c);
				if (c.estValide(res)){
					Cellule fille = c.ajoutVecteurGlouton(res, facteurs, resultats);
					if (fille.resultatHomogNull() || fille.resultatNull()){
						if (fille.resultatNull()){
							if (!fille.estPresentSol(solMin.getInit())){
								solMin.addInit(fille);
								//System.out.println("SOL MIN : " + fille.getValeur());
							}
						}
						else if (!fille.estPresentSol(solMin.getHomog()))
							solMin.addHomog(fille);

					} else {
						if (!solMin.getInit().isEmpty() && !solMin.getHomog().isEmpty()){
							//	System.out.println("comparaison : " +fille.estPlusPetit(solMin.getInit()));
							if (!fille.estPlusGrand(solMin.getInit()) && !fille.estPlusGrand(solMin.getHomog())){
								file.add(fille);
							}
						}
					}
				}	
			}
		}
		//System.out.println("sol visitées : " + visitée);
		//System.out.println("solMin : " + solMin);
		return solMin;
	}

	public SolMin solMinOrdre(){
		//long startTime = System.currentTimeMillis();
		SolMin solMin = new SolMin();
		int nbVariable = facteurs[0].length;
		ArrayList<Cellule> file = new ArrayList<Cellule>();
		for (int i=0; i<nbVariable; i++){
			boolean[] freeze = new boolean[nbVariable];
			for (int j=i+1; j<nbVariable; j++)
				freeze[j] = true;
			Cellule c = new Cellule(facteurs, listVecteurs.getVecteur(i).getValeur(), resultats, freeze);
			file.add(c);
			if (c.resultatNull())
				solMin.addInit(c);
			else if (c.resultatHomogNull())
				solMin.addHomog(c);
		}
		//System.out.println("file début : " + file);

		while(!file.isEmpty()){
			Cellule c = file.get(0);
			file.remove(0);
			Vecteur res;
			boolean[] freeze = c.getFreeze();
			//System.out.println("cellule courante : " + c);
			for (int i=0; i<nbVariable; i++){
				if (freeze[i] == false){
					res = listVecteurs.getVecteur(i);
					//System.out.println("vecteur à ajouter: " + res);
					if (c.estValide(res)){
						Cellule fille = c.ajoutVecteurFreeze(res, facteurs, resultats, freeze);
						freeze[i] = true;
						//System.out.println("resHNull : " + fille.resultatHomogNull());
						if (fille.resultatNull()){
							if (!fille.estPresentSol(solMin.getInit())){
								solMin.addInit(fille);
								//System.out.println("SOL MIN : " + fille);
							}
						} if (fille.resultatHomogNull()){
							if (!fille.estPresentSol(solMin.getHomog())){
								solMin.addHomog(fille);
								//System.out.println("SOL HOMOG : " + fille);
							}
						} if (!(fille.resultatHomogNull() && fille.resultatNull())) {
							if (!solMin.getInit().isEmpty() && !solMin.getHomog().isEmpty()){
								//System.out.println("comparaison init : " +fille.estPlusGrand(solMin.getInit()));
								//System.out.println("comparaison homog : " +fille.estPlusGrand(solMin.getHomog()));
								//System.out.println("donc => " + !(fille.estPlusGrand(solMin.getInit()) && fille.estPlusGrand(solMin.getHomog())));
								if (!(fille.estPlusGrand(solMin.getInit()) && fille.estPlusGrand(solMin.getHomog()))){
									file.add(fille);
									//System.out.println("ajout dans la file de " + fille);
								}

								/*for (int j=0; j<solMin.getInit().size(); j++){
									min = solMin.getInit(j);
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
							}*/
							} else{
								file.add(fille);
								//System.out.println("ajout dans la file de " + fille);
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
					Vecteur vect = listVecteurs.getVecteur(i);
					//System.out.println(" vecteur " + i + " : " + vect);
					//System.out.println("validité : " + (vect.estValide(courant)) + " " +  (boolCourant[i]==false));
					if (boolCourant[i] == false && courant.estValide(vect) ){ 
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
		for (int v = 0; v<listVecteurs.size(); v++){
			Cellule c = new Cellule(facteurs, listVecteurs.getVecteur(v).getValeur(), resultats);
			pile.add(c);
		}
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
