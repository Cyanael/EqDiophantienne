package sousEspaces;

import java.util.ArrayList;

import outils.TabInt;
import algo2.Cellule;

public class ListeVecteurs {

	private ArrayList<Vecteur> listVect;


	public ListeVecteurs(int [][] facteurs){
		int nbVar = facteurs[0].length;
		ArrayList<Vecteur> ar = new ArrayList<Vecteur>();
		for (int i=0; i<nbVar; i++){
			TabInt tab = new TabInt(nbVar);
			tab.setRes(i, 1);
			Vecteur v = new Vecteur(facteurs, tab);
			ar.add(v);
		}
		this.listVect = ar;
	

	}
	
	public ArrayList<Vecteur> getListVecteurs(){
		return listVect;
	}
	
	public Vecteur getVecteur(int indice){
		return listVect.get(indice);
	}
	
	public int size(){
		return listVect.size();
	}

}
