package algo2;

import java.util.ArrayList;
import java.util.Comparator;

import automate.Chemin;

import outils.TabInt;

public class Cellule implements Comparable{


	private TabInt vecteur;		// résultats des equations
	private TabInt valeur;			// valeur des variables
	private double tailleVecteur;
	private boolean[] freeze;		// true : valeur qu'on ne peut changer


	public Cellule(int[][] facteurs, TabInt valeur, TabInt resultatEq){
		this.valeur = valeur;

		TabInt v = new TabInt(facteurs.length);
		int res;
		for (int i = 0 ; i<facteurs.length; i++){
			res = -(resultatEq.getRes(i));
			for (int j=0; j<facteurs[i].length; j++){
				res+= facteurs[i][j]* valeur.getRes(j);
				}
				
			v.setRes(i, res);
			}
		this.vecteur = v;

		double taille = 0;
		for (int j=0; j<vecteur.size(); j++)
			taille += vecteur.getRes(j)*vecteur.getRes(j);
		this.tailleVecteur =Math.sqrt(taille);

		boolean[] freeze = new boolean[valeur.size()];
		for (int k=0; k<freeze.length; k++)
			freeze[k] = false;
		this.freeze = freeze;
	}

	public Cellule(int[][] facteurs, TabInt valeur, TabInt resultatEq, boolean[] freeze){
		this.valeur = valeur;
		boolean[] f = new boolean[freeze.length];
		for (int i=0; i<freeze.length; i++)
			f[i] = freeze[i];
		this.freeze = f;

		TabInt v = new TabInt(facteurs.length);
		int res;
		for (int i = 0 ; i<facteurs.length; i++){
			res = -(resultatEq.getRes(i));
			for (int j=0; j<facteurs[i].length; j++)
				res+= facteurs[i][j]* valeur.getRes(j);
			v.setRes(i, res);
		}
		this.vecteur = v;

		double taille = 0;
		for (int j=0; j<vecteur.size(); j++)
			taille += vecteur.getRes(j)*vecteur.getRes(j);
		this.tailleVecteur =Math.sqrt(taille);
	}

	public Cellule(Cellule c, boolean[] freeze){
		this.valeur = c.getValeur();
		this.vecteur = c.getVecteur();
		this.freeze = freeze;
		this.tailleVecteur = c.getTaille();
	}

	public Cellule ajoutVecteurGlouton(Cellule vecteur, int[][] facteurs, TabInt resultatEq){
		int nbVar = this.valeur.size();
		TabInt valeur = new TabInt(nbVar);
		for (int i = 0; i<nbVar; i++)
			valeur.setRes(i, this.valeur.getRes(i)+vecteur.getValeur().getRes(i));
		Cellule res = new Cellule(facteurs, valeur, resultatEq);
		return res;
	}

	public Cellule ajoutVecteurFreeze(Cellule vecteur, int[][] facteurs, TabInt resultatEq, boolean[] freeze){
		int nbVar = this.valeur.size();
		TabInt valeur = new TabInt(nbVar);
		for (int i = 0; i<nbVar; i++)
			valeur.setRes(i, this.valeur.getRes(i)+vecteur.getValeur().getRes(i));
		Cellule res = new Cellule(facteurs, valeur, resultatEq, freeze);
		return res;
	}

	public boolean estValide(Cellule c){	// this = Xa, Xb, Xc	c : Ya, Yb, Yc
		double res = tailleVecteur * c.getTaille();
		double a = 0;
		double b = 0;
		double d = 0;

		for (int i = 0; i< vecteur.size(); i++){
			a += vecteur.getRes(i)*c.getVecteur().getRes(i);			//a = XaYa + XbYb + XcYc
			b += vecteur.getRes(i) * vecteur.getRes(i);					//b = Xa²+Ya²+Za²
			d +=c.getVecteur().getRes(i) * c.getVecteur().getRes(i);	//d = Xb²+Yb²+Zb²
		}
		double cos = a / Math.sqrt(b * d);
		res = res * cos;
		if (res < 0){
			return true;
		}
		return false;		
	}

	public boolean estNulle(){
		for (int i=0; i<vecteur.size(); i++)
			if (vecteur.getRes(i) !=0)
				return false;
		return true;
	}

	public boolean estPresentListe(ArrayList<TabInt> liste){	
		if (estNulle())
			return false;
		boolean egal = false;
		int i = 0;
		int tailleListe = liste.size();
		while(!egal && i<tailleListe){
			egal = true;
			for (int j=0; j<vecteur.size(); j++){
				if (vecteur.getRes(j) != liste.get(i).getRes(j))
					egal = false;
			}
			i++;
		}
		return egal;
	}

	public boolean estPresentSol(ArrayList<Cellule> liste){
		boolean egal = false;
		int i = 0;
		int tailleListe = liste.size();
		while(!egal && i<tailleListe){
			egal = true;
			for (int j=0; j<valeur.size(); j++){
				if (valeur.getRes(j) != liste.get(i).getValeur().getRes(j))
					egal = false;
			}
			i++;
		}
		return egal;
	}

	/*public boolean estPlusPetit (Cellule c){ // return vrai si this est plus petit que c
		if (this.estNulle())
			return false;
		for (int i=0; i<valeur.size(); i++){
			if (getValeur(i)> c.getValeur(i)){
				return false;		
			}
		}
		return true;
	}*/
	
	public boolean estPlusGrand (Cellule c){
		if (this.estNulle())
			return false;
		for (int i=0; i<valeur.size(); i++){
			//System.out.println("comparaison : " + getValeur(i) + " " + c.getValeur(i));
			if (getValeur(i)< c.getValeur(i))
				return false;
		}
		return true;
	}

	/*public boolean estComparable(Cellule c){		//TODO : voir si on peut renvoyer le plus petit à la place de faire 2 fct
		if(!c.estPlusPetit(this) && !this.estPlusPetit(c))
			return false;
		return true;
	}*/
	
	public int compareTo(Object c){		// comparer les tailles
		double taille1 = this.tailleVecteur;
		double taille2 = ((Cellule) c).getTaille();

		if (taille1 < taille2) {
			return 1;
		} else if (taille2 < taille1) {
			return -1;        	
		} else {
			return 0;
		}
	}      



	public TabInt getVecteur(){
		return vecteur;
	}

	public TabInt getValeur(){
		return valeur;
	}

	public int getValeur(int indice){
		return valeur.getRes(indice);
	}
	
	public void setValeur(int indice, int val){
		valeur.setRes(indice, val);
	}

	public double getTaille(){
		return tailleVecteur;
	}

	public boolean[] getFreeze(){
		return this.freeze;
	}

	public void setFreeze(boolean[] val){
		this.freeze = val;
	}

	public void setFreeze(int indice, boolean val){
		this.freeze[indice] = val;
	}

	public String toString(){
		String str = " resultat : " + vecteur + " valeur : " + valeur + " freeze :";
		for (int i=0; i<valeur.size(); i++)
			str += " " + freeze[i];
		return str;
	}

	public String toString2(){
		return ("vecteur de taille : " + tailleVecteur + toString());
	}


}
