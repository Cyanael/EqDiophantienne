package sousEspaces;

import outils.TabInt;

public class Vecteur {

	
	private TabInt vecteur;		// résultats des equations
	private TabInt valeur;			// valeur des variables
	private double taille;
	
	
	public Vecteur(int[][] facteurs, TabInt valeur){
		this.valeur = valeur;
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
		this.vecteur = v;
		double taille = 0;
		for (int j=0; j<vecteur.size(); j++)
			taille += vecteur.getRes(j)*vecteur.getRes(j);
		this.taille = Math.sqrt(taille);
	}
	
	public TabInt getVecteur(){
		return vecteur;
	}
	
	public int getVecteur(int indice){
		return vecteur.getRes(indice);
	}
	
	public TabInt getValeur(){
		return valeur;
	}
	
	public int getValeur(int indice){
		return valeur.getRes(indice);
	}
	
	public double getTaille(){
		return taille;
	}
	
	public String toString(){
		return valeur + " " + valeur;
	}
}
