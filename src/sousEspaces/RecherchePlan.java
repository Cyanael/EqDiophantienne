package sousEspaces;

import java.util.ArrayList;

import algo2.Cellule;
import algo2.SolMin;

import outils.TabInt;

public class RecherchePlan {

	private TabInt resultats;
	private int[][] facteurs;
	private ListeVecteurs listVecteurs;

	public RecherchePlan(int[][] facteurs, TabInt res){
		this.resultats = res;
		this.facteurs = facteurs;
		this.listVecteurs = new ListeVecteurs(facteurs);
		//System.out.println("vecteurs : " + listVecteurs);
	}


	public SolMin recherche(){		//TODO : vérifier à chaque premiereSolMin que solMin ne soit pas nulle
		SolMin solMin = new SolMin();
		ArrayList<Contraintes> contrainte = new ArrayList<Contraintes>();

		Contraintes zero = new Contraintes(facteurs, resultats); //aucune contrainte, cellule nulle
		contrainte.add(zero);
		//System.out.println("condition de départ : " + zero);

		ArrayList<Contraintes> feuilles = new ArrayList<Contraintes>();

		while(!contrainte.isEmpty()){
			Contraintes courante = contrainte.get(0);
			contrainte.remove(0);
			//System.out.println("condition dépillée : " + courante);

			System.out.println("recherche avec les containtes : " + courante);
			Cellule c = premiereSolMin(courante, solMin.getHomog());
			System.out.println("premiereSolMin : " + c);

			if (c == null){
				feuilles.add(courante);
				//System.out.println("liste feuilles : " + feuilles);
			}
			else {
				solMin.addHomog(c);

				Contraintes mAjConditions = new Contraintes(c, courante.getListVar(), facteurs, resultats);
				//System.out.println("maj contrainte : " + mAjConditions);
				//int contrainteModifiees = 0;
				int i=0; //indice de variable
				//System.out.println("nb varCond : " + mAjConditions.nbCont());
				while (/*contrainteModifiees < mAjConditions.nbCont() &&*/ i<c.getValeur().size()){ // pour chaque variable, on mAj les contraintes
					//System.out.println("var " + i + " valeur : " + c.getValeur(i));
					//System.out.println("indice : " + i + "taille " + c.getValeur().size());
					if (c.getValeur(i) != 0){
						//System.out.println("est Contrainte : " + (mAjConditions.getVar(i) instanceof VarContrainte));
						if (mAjConditions.getVar(i) instanceof VarContrainte){
							Contraintes nouvelle = new Contraintes(mAjConditions.getCellule(), mAjConditions.getListVar(), facteurs, resultats);	// on fait un clone
							mAjConditions.varContToFix(i, 0, mAjConditions.getCellule().getValeur(i)-1);	// on fixe la variable dans la premiere condition
							nouvelle.mAjVarCont(i, mAjConditions.getCellule().getValeur(i));	// et on augment le minimum dans la deuxième
							contrainte.add(mAjConditions);
							mAjConditions = nouvelle;
							//contrainteModifiees++;
						} /*else {
							Contraintes nouvelle = new Contraintes(mAjConditions.getCellule(), mAjConditions.getListVar(), facteurs, resultats);	// on fait un clone
							// TODO : compléter !!!

							contrainte.add(mAjConditions);
							mAjConditions = nouvelle;
						}*/

					}
					i++;
				}
				//contrainte.add(mAjConditions);
				System.out.println("fin creation fils, list contraintes : " + contrainte+ "\n");
			}
		}
		System.out.println("solMinHomo : " + solMin);
		System.out.println("nb Feuilles : " +feuilles.size() /*+ " " + feuilles*/);
		// recherche des solutions de l'équation sur les feuilles
		if (!resultats.estNull()){
			for (int i=0; i<feuilles.size(); i++){
				//System.out.println("contrainte : " + feuilles.get(i));
				ArrayList<Cellule> solInit = solMinOrdre(feuilles.get(i));
				//System.out.println("solution trouvées : " + solInit);
				for (int j=0; j<solInit.size(); j++)
					solMin.addInit(solInit.get(j));
			}
		}

		// recherche init sur les feuilles
		System.out.println();
		return solMin;	
	}




	public Cellule premiereSolMin(Contraintes contrainte, ArrayList<Cellule> listMin){	// à partir de solMinOrdre
		Cellule cel = contrainte.celMin();
		ArrayList<Variable> listVar = contrainte.getListVar();

		int nbVariable = facteurs[0].length;
		ArrayList<Cellule> file = new ArrayList<Cellule>();
		if (cel.getValeur().estNull()){		
			for (int i=0; i<nbVariable; i++){
				boolean[] freeze = new boolean[nbVariable];
				for (int j=i+1; j<nbVariable; j++)
					freeze[j] = true;
				//Cellule c = new Cellule(listVecteurs.getVecteur(i), freeze);
				Cellule c = new Cellule(facteurs, listVecteurs.getVecteur(i).getValeur(), resultats, freeze);
				file.add(c);
			}
		} else
			file.add(cel);

		//System.out.println("file début : " + file);

		while(!file.isEmpty()){
			//System.out.println("file : " + file);
			Cellule c = file.get(0);
			file.remove(0);
			Vecteur res;
			boolean[] freeze = c.getFreeze();
			System.out.println("cellule courante : " + c);
			for (int i=0; i<nbVariable; i++){
				if (freeze[i] == false){
					res = listVecteurs.getVecteur(i);
					//System.out.println("vecteur à ajouter: " + res);
					if (c.estValideHomog(res)){
						Cellule fille = c.ajoutVecteurFreeze(res, facteurs, resultats, freeze);
						//System.out.println("est valide : " + fille);
						if (respectContaintes(fille, listVar)){
							//System.out.println("respect contrainte");
							freeze[i] = true;
							if (fille.resultatHomogNull()){	
								//System.out.println("fille : " + fille);

								if (!fille.estPlusGrand(listMin) && !fille.estEgale(contrainte.getCellule())){
									//if (!fille.estPresentSol(listMin)){		
									fille.freezeInit();		// les freeze sont réinitialisés à false
									return fille;
								}
							} else 
								file.add(fille);
						}
					}
				}

			}
		}
		return null;	
	}




	public ArrayList<Cellule> solMinOrdre(Contraintes contrainte){
		Cellule cel = contrainte.celMin();
		ArrayList<Variable> listVar = contrainte.getListVar();

		int nbVariable = facteurs[0].length;
		ArrayList<Cellule> solMin = new ArrayList<Cellule>();
		ArrayList<Cellule> file = new ArrayList<Cellule>();
		if (cel.getValeur().estNull()){		
			for (int i=0; i<nbVariable; i++){
				boolean[] freeze = new boolean[nbVariable];
				for (int j=i+1; j<nbVariable; j++)
					freeze[j] = true;
				Cellule c = new Cellule(facteurs, listVecteurs.getVecteur(i).getValeur(), resultats, freeze);
				if (c.resultatNull())
					solMin.add(c);
				else
					file.add(c);
			}
		} else
			file.add(cel);		
		//System.out.println("file début : " + file);

		//while(!file.isEmpty()){
		for (int l=0; l<10; l++){
			if (!file.isEmpty()){
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
							if (respectContaintes(fille, listVar)){
								//System.out.println("respecte les contrainte : " + listVar);
								freeze[i] = true;
								//System.out.println("ajouté : " + fille);
								if (fille.resultatNull()){
									//	System.out.println("est nulle : " + fille );
									if (!fille.estPresentSol(solMin)){
										solMin.add(fille);
									}
								} else {
									if(!fille.estPlusGrand(solMin)){
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

			}
		}
		return solMin;	
	}


	// les bornes sont incluses aux résultats possibles
	public boolean respectContaintes(Cellule c, ArrayList<Variable> listConstraintes){
		int valI, min, max;
		Variable courante;
		for (int i=0; i<c.getValeur().size(); i++){
			courante = listConstraintes.get(i);
			valI = c.getValeur(i);
			min = courante.getDebut();
			if (valI < min)
				return false;
			if (courante instanceof VarFixee){
				max = ((VarFixee) courante).getFin();
				if (valI > max)
					return false;
			}		
		}
		return true;
	}


}
