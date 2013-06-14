package automate;
import java.util.ArrayList;

import outils.TabInt;
import outils.TabString;


public class Automate {

	private int[][] facteurs;
	private EqInit init;
	private Equation finale;
	private ArrayList<Equation> listEtats;	// contient les états initial et finaux
	private Tree arbreListes;


	public Automate(int[][] facteurs, EqInit a){
		this.facteurs = facteurs;
		this.init = a;
		this.finale = null;
		this.listEtats = new ArrayList<Equation>();
		this.arbreListes = new Tree();
		arbreListes = arbreListes.initialisation(a.getTransitionsPaires(), a.getTransitionsImpaires());	
	}

	public void initialisation(){
		int nb;			// numero de l'état, init = 0, final = 1
		listEtats.add(init);
		if (init.getRes().estNull())
			nb = creation(init, 1);
		else
			nb = creation(init,2);	// numéro du prochain état : 2

		int indice = 1;		// on a déjà créé init donc on commence à l'indice 1
		while(indice < listEtats.size()){	// creation des voisins de chaque etat de la liste
			nb = creation(listEtats.get(indice), nb);
			indice++;
		}
		//System.out.println("liste des etats : " + listEtats);
		if (finale == null){
			System.out.println("Aucun état final n'a été trouvé");
			System.exit(0);
		}

	}

	public int creation(Equation eq, int indice){
		//System.out.println("\netat courant : " + eq.getRes());
		int indice1 = indice;
		ArrayList<Solution> listSol = eq.getSolutions();
		//System.out.println("listeSol : " + eq.getSolutions());
		for (int i=0; i<listSol.size(); i++){	// pour tous les voisins de eq
			double[] r = listSol.get(i).getEval();
			TabInt res = new TabInt(facteurs.length);
			for (int j=0; j<res.size(); j++)
				res.setRes(j, (int) r[j]);
			//System.out.println("etat voisin : " + res);

			if (res.estNull() && finale == null && init.getRes().estNull())
				finale = init;

			else if (!etatPresent(res)){	// si l'état est déjà présent on ne le créé pas
				EqEtat e = null;	// ne sera jamais l'état initial
				if (res.estNull()){
					//System.out.println("création état final");
					e = new EqEtat(res, facteurs, 1);
					this.finale = e;
				}
				else{
					//System.out.println("création état : " + res + " indice : " + indice1);
					e = new EqEtat(res, facteurs, indice1);
					indice1++;
				}
				e.initSol(arbreListes.recherche(e.getRes()));

				listEtats.add(e);
				//System.out.println("apres ajout : " + listEtats + " taille list : " + listEtats.size());
			}
		}
		return indice1;
	}

	public boolean etatPresent(TabInt resultat){   // on regarde si l'etat-equation a déjà été crée
		if (listEtats.size() == 0)	// si la liste est vide
			return false;
		if (init.getRes().equal(resultat)) 
			return true;
		else {
			Equation etat;
			for (int i=0; i<listEtats.size(); i++){	// on recherche dans la liste des etat déjà crés
				etat = listEtats.get(i);
				if (etat.getRes().equal(resultat))
					return true;
			}
			return false;
		}
	}

	public ArrayList<Chemin> solutionsMinimalesInit(){		
		ArrayList<Chemin> solMin = new ArrayList<Chemin>();
		ArrayList<Chemin> file = new ArrayList<Chemin>();

		for (int i=0; i<init.getSolutions().size(); i++){	// premiers elements dans la file : chemins partants de init
			TabInt etat = new TabInt(init.getVoisin(i));
			//System.out.println("etat courant : " + etat + " numero " + getEtatNb(etat) + "\n");
			if (!etat.equal(init.getRes())){		// les boucles allant de init à init sont évitées //TODO : à vérifier
				TabString transition = new TabString(init.getSolution(i));
				Chemin c = new Chemin(transition, getEtatNb(etat));
				if (getEtatNb(etat).getRes() == finale.getRes()){
					if (!c.estNull())	// on ne veut pas les boucles nulles
						solMin.add(c);	// ajout des chemin init => init
				}
				else
					file.add(c);		// ajout des chemin init => !finale
			}
		}

		while(file.size()!=0 && file.get(0).getTailleChemin() <listEtats.size()){	
			Chemin courant = file.get(0); 	// pour chaque element de la file : courant
			//System.out.println("courant : " + courant + " nombre d'états dans le chemin : " + courant.getListEtat().size());
			file.remove(0); 
			
			for (int i=0; i<courant.getEtat().getSolutions().size(); i++){	// on cree un chemin partant de l'etat courant 
				TabInt etat = new TabInt(courant.getEtat().getVoisin(i));		// etat : voisin de courant
				//System.out.println(etat + " estPresent " + courant.getListEtat() +" : ");
				//System.out.println(courant.etatPresentListe(etat));
				if (etat != init.getRes() && !courant.etatPresentListe(etat)){	// pour éviter les boucles
					//System.out.println("pas present");
					TabString transition = new TabString(courant.getEtat().getSolution(i));	
					Chemin c = courant.ajoutTransition(transition, getEtatNb(etat));
					if (solMin.size() == 0 && c.getEtat() == finale){
						solMin.add(c);
						System.out.println("sol min trouvé : " + c.getValeurInt());
					}
					else {

						boolean comparabilité = false;
						for (int j=0; j<solMin.size(); j++){
							if (c.estComparable(solMin.get(j))){
								comparabilité = true;
								if (c.estPlusPetit(solMin.get(j)))
									if (getEtatNb(etat).getRes() == finale.getRes()){
										solMin.remove(j);			// alors on la remplace
										solMin.add(c);
									}
									else
										file.add(c);
							}
						}
						if (comparabilité == false){	//si c n'était comparable avec aucun chemin de solMin
							if (getEtatNb(etat).getRes() == finale.getRes())
								solMin.add(c);
							else
								file.add(c);
						}
					}
				}
			}
		}
		return solMin;
	}



	public ArrayList<Chemin> solutionsMinimalesFinales(){		
		ArrayList<Chemin> solMin = new ArrayList<Chemin>();
		ArrayList<Chemin> file = new ArrayList<Chemin>();

		for (int i=0; i<finale.getSolutions().size(); i++){	// premiers elements dans la file : chemins partants de finale
			TabInt etat = new TabInt(finale.getVoisin(i));
			TabString transition = new TabString(finale.getSolution(i));
			Chemin c = new Chemin(transition, getEtatNb(etat));
			if (getEtatNb(etat).getRes() == finale.getRes()) {
				if (!c.estNull())
					solMin.add(c);		// ajouts des chemins finale => finale
			} else
				file.add(c);		// ajout des chemin finale => !finale
		}
		//System.out.println("solMin init : " + solMin);
		//System.out.println("file : " + file);

		while(file.size()!=0 && file.get(0).getTailleChemin() <listEtats.size()){	
			Chemin courant = file.get(0); 	// pour chaque element de la file
			file.remove(0);
			//System.out.println(" nombre d'états dans le chemin : " + courant.getListEtat().size());
			//System.out.println(file.size());
			
			for (int i=0; i<courant.getEtat().getSolutions().size(); i++){// on cree un chemin partant de l'etat courant 
				TabInt etat = new TabInt(courant.getEtat().getVoisin(i));
				if (!courant.etatPresentListe(etat)){	// pour éviter les boucles
					TabString transition = new TabString(courant.getEtat().getSolution(i));
					Chemin c = courant.ajoutTransition(transition, getEtatNb(etat));
					if (solMin.size() == 0 && c.getEtat() == finale)
						solMin.add(c);
					else {

						boolean comparabilité = false;
						for (int j=0; j<solMin.size(); j++){
							if (c.estComparable(solMin.get(j))){
								comparabilité = true;
								if (c.estPlusPetit(solMin.get(j))){
									if (getEtatNb(etat).getRes() == finale.getRes()){
										solMin.remove(j);			// alors on la remplace
										solMin.add(c);
									} else
										file.add(c);
								}
							}
						}
						if (comparabilité == false){
							if (getEtatNb(etat).getRes() == finale.getRes())
								solMin.add(c);
							else
								file.add(c);
						}
					}
				}
			}
		 // on l'efface
		}
		return solMin;
	}



	public int[][] getFacteurs(){
		return this.facteurs;
	}

	public int[] getFacteur(int indice){
		return this.facteurs[indice];
	}

	public int getNbFacteurs(){
		return this.facteurs.length;
	}

	public Equation getInit(){
		return init;
	}

	public Equation getFinale(){
		return finale;
	}

	public ArrayList<Equation> getListEtats(){
		return listEtats;
	}
	//equation a la place indice dans la lsite des etats
	public Equation getEtat(int indice){
		return this.listEtats.get(indice);
	}
	// equation dont le resultat est indice
	public Equation getEtatNb(TabInt indice){	//TODO : enlever Syso
		//System.out.println("entre getEtatNb, indice : " + indice);
		//System.out.println("listEtat : " + listEtats);
		Equation eq = null;
		boolean trouve = false;
		int i = 0;
		while((trouve == false) && i<listEtats.size()){
			trouve = true;
			TabInt courante = new TabInt(listEtats.get(i).getRes());
			//System.out.println("comparaison : " + indice + " et " + courante);
			for (int j=0; j<indice.size(); j++){
				if (courante.getRes(j) != indice.getRes(j)){
					//System.out.println("comparaison : " + courante.getRes(j) +" et : " + indice.getRes(j));
					trouve = false;
					//System.out.println("trouve = " + trouve);
				}
			}
			if (trouve == true){
				eq = listEtats.get(i);
			}
			i++;
		}
		//System.out.println("fin getEtatNb : " + eq);
		return eq;			
	}


	public int nbEtats(){
		return listEtats.size();
	}

	public Tree getArbre(){
		return this.arbreListes;
	}

}
