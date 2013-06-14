package algo2;

import java.util.ArrayList;
import outils.TabInt;

public class Cellule implements Comparable{

	private TabInt resultatEq;
	private TabInt vecteur;		// résultats des equations
	private TabInt vecteurHomog;	// résultat de l'eq homogène
	private TabInt valeur;			// valeur des variables
	private double tailleVecteur;
	private double tailleVecteurHomog;	
	private boolean[] freeze;		// true : valeur qu'on ne peut changer


	public Cellule(int[][] facteurs, TabInt valeur, TabInt resultatEq){
		this.resultatEq = resultatEq;
		this.valeur = valeur;
		this.vecteurHomog = calculVecteurHomog(facteurs, resultatEq);
		this.vecteur = calculVecteur(vecteurHomog, resultatEq);
		this.tailleVecteur = calculTaille();
		this.tailleVecteurHomog = calculTailleHomog();

		boolean[] freeze = new boolean[valeur.size()];
		for (int k=0; k<freeze.length; k++)
			freeze[k] = false;
		this.freeze = freeze;
	}

	public Cellule(int[][] facteurs, TabInt valeur, TabInt resultatEq, boolean[] freeze){
		this.valeur = valeur;
		this.resultatEq = resultatEq;
		boolean[] f = new boolean[freeze.length];
		for (int i=0; i<freeze.length; i++)
			f[i] = freeze[i];
		this.freeze = f;
		this.vecteurHomog = calculVecteurHomog(facteurs, resultatEq);
		this.vecteur = calculVecteur(vecteurHomog, resultatEq);
		this.tailleVecteur = calculTaille();
		this.tailleVecteurHomog = calculTailleHomog();
	}

	public Cellule(Cellule c, boolean[] freeze){
		this.resultatEq = c.getResEq();
		this.valeur = c.getValeur();
		this.vecteur = c.getVecteur();
		this.vecteurHomog = c.getVecteurHomog();
		this.freeze = freeze;
		this.tailleVecteur = c.getTaille();
		this.tailleVecteurHomog = c.getTailleHomog();
	}
	
	public TabInt calculVecteurHomog(int[][] facteurs, TabInt resultat){
		int nbEq = facteurs.length;
		TabInt v = new TabInt(nbEq);
		int res;
		for (int i = 0 ; i<nbEq; i++){
			res = 0;
			for (int j=0; j<facteurs[i].length; j++){
				res+= facteurs[i][j]* valeur.getRes(j);
				}
				
			v.setRes(i, res);
			}
		return v;
	}
	
	public TabInt calculVecteur(TabInt vecteurHomog, TabInt resultat){
		int nbEq = vecteurHomog.size();
		TabInt init = new TabInt(nbEq);
		for (int k=0; k<nbEq; k++){
			init.setRes(k, vecteurHomog.getRes(k)- resultat.getRes(k));
		}
		return init;
	}
	
	public double calculTaille(){
		double taille = 0;
		for (int j=0; j<vecteur.size(); j++)
			taille += vecteur.getRes(j)*vecteur.getRes(j);
		return Math.sqrt(taille);
	}
	
	public double calculTailleHomog(){
		double taille = 0;
		for (int j=0; j<vecteurHomog.size(); j++)
			taille += vecteurHomog.getRes(j)*vecteurHomog.getRes(j);
		return Math.sqrt(taille);
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

	public boolean estValideHomog(Cellule c){
		//System.out.println("cos entre : " + this.vecteurHomog + " " + c.getVecteurHomog());
		double res = tailleVecteurHomog * c.getTailleHomog();
		double a =0;
		double b = 0;
		double d = 0;
		for (int i = 0; i< vecteurHomog.size(); i++){
			a += vecteurHomog.getRes(i)*c.getVecteurHomog().getRes(i);			//a = XaYa + XbYb + XcYc
			//System.out.println("a : " + a);
			b += vecteurHomog.getRes(i) * vecteurHomog.getRes(i);					//b = Xa²+Ya²+Za²
			//System.out.println("b : " + b);
			d +=c.getVecteurHomog().getRes(i) * c.getVecteurHomog().getRes(i);	//d = Xb²+Yb²+Zb²
			//System.out.println("d : " + d);
			
		}
		double cos = a / Math.sqrt(b * d);
		//System.out.println("cos : " + cos);
		res = res * cos;
		if (res < 0){
			//System.out.println("res cos : " + res);
			return true;
		}
		//System.out.println(("res cos : " + res));
		return false;
	}
	
	public boolean estValide(Cellule c){	
		double res = tailleVecteur * c.getTaille();
		double a =0;
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
			//System.out.println("res cos : " + res);
			return true;
		}
		//System.out.println(("res cos : " + res));
		return false;		
	}
	

	public boolean resultatNull(){
		for (int i=0; i<vecteur.size(); i++)
			if (vecteur.getRes(i) !=0)
				return false;
		return true;
	}
	
	public boolean resultatHomogNull(){
		for (int i=0; i<vecteurHomog.size(); i++)
			if (vecteurHomog.getRes(i) !=0)
				return false;
		return true;
	}
	
	public boolean estEgale(Cellule c){
		for (int i=0; i<this.valeur.size(); i++){
			if (getValeur(i) != c.getValeur(i))
				return false;
		}
		return true;
	}

	public boolean estPresentListe(ArrayList<TabInt> liste){	
		if (resultatNull())
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

	public boolean estPlusPetit (Cellule c){ // return vrai si this est plus petit que c
		if (this.resultatNull())
			return false;
		for (int i=0; i<valeur.size(); i++){
			if (getValeur(i)> c.getValeur(i)){
				return false;		
			}
		}
		return true;
	}
	
	public boolean estPlusGrand (Cellule c){
		
		if (this.getValeur().estNull())
				return false;
		for (int i=0; i<valeur.size(); i++){
			//System.out.println("comparaison val : " + getValeur(i) + " " + c.getValeur(i));
			if (getValeur(i)< c.getValeur(i))
				return false;
		}
		return true;
	}
	
	public boolean estPlusGrand(ArrayList<Cellule> ar){
		for (int i=0; i<ar.size(); i++){
			//System.out.println("comparaison " + this.valeur + ar.get(i).getValeur() );
			if (estPlusGrand(ar.get(i)))
				return true;
		}
		return false;
	}

	/*public boolean estComparable(Cellule c){	
		if(!c.estPlusPetit(this) && !this.estPlusPetit(c))
			return false;
		return true;
	}
	
	public boolean estComparable(ArrayList<Cellule> ar){
		for (int i=0; i<ar.size(); i++)
			if (estComparable(ar.get(i))){
				return true;
			}
		return false;
	}*/
	
	
	public void freezeInit(){
		for(int i=0; i<valeur.size(); i++)
			freeze[i] = false;
	}
	
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


	public TabInt getResEq(){
		return resultatEq;
	}

	public TabInt getVecteur(){
		return vecteur;
	}

	public TabInt getVecteurHomog(){
		return this.vecteurHomog;
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

	public double getTailleHomog(){
		return tailleVecteurHomog;
	}
	
	public boolean[] getFreeze(){
		return this.freeze;
	}

	public boolean getFreeze(int indice){
		return this.freeze[indice];
	}
	
	public void setFreeze(boolean[] val){
		this.freeze = val;
	}

	public void setFreeze(int indice, boolean val){
		this.freeze[indice] = val;
	}

	public String toString(){
		String str = " resultat : " + vecteur + " || " + vecteurHomog + " valeur : " + valeur + " freeze :";
		for (int i=0; i<valeur.size(); i++)
			str += " " + freeze[i];
		return str;
	}

	public String toString2(){
		return ("vecteur de taille : " + tailleVecteur + toString());
	}


}
