package outils;

import java.util.ArrayList;

import matriceEtArbre.AbExpr;
import matriceEtArbre.MatriceCalcul;
import automate.Automate;
import automate.Chemin;
import automate.Equation;


public class Print {

	
	public static String intTab(int[] tab){	//tableau d'entiers
		String res = "";
		for (int i=0; i<tab.length-1; i++){
			res += tab[i] + " ";
		}
		res+= tab[tab.length-1];
		return res;
	}
	
	public static String stringTab (String[] tab){	//tableau de string
		String res = "";
		for (int i=0; i<tab.length-1; i++){
			res += tab[i] + " ";
		}
		res+= tab[tab.length-1];
		return res;
	}
	
	public static String automate(Automate auto){
		String res = "nombre d'états : " + auto.getListEtats().size() + "\n";
		res += "etat intial : " + auto.getInit() + "\netat final : " + auto.getFinale()+ "\nétats : ";
		res += listEquations(auto.getListEtats());
		return res;
	}
	
	public static String listEquations(ArrayList<Equation> arrayList){	// résultats de la liste des états
		String res = "";
		for (int i=0; i<arrayList.size(); i++)
			res += arrayList.get(i).getRes() + " : " + arrayList.get(i).numeroEq() + " \t";
		return res;
	}
	
	public static String matrice(MatriceCalcul m){		//matrice
		String res = "Matrice d'exposant : " + m.getExp() + "\n";
		int taille = m.getTaille();
		AbExpr[][] mat = m.getMat();
		for (int i=0; i<taille; i++){
			for (int j=0; j<taille; j++)
				res += mat[i][j] + "\t\t";
			res += "\n";
		}
		return res;
	}
	
	public static String arrayStr(ArrayList<String[]> ar){		// liste de string[]
		String res = "";
		for (int i=0; i<ar.size(); i++)
			res += "[" + stringTab(ar.get(i)) + "] ";
		return res;
	}
	
	public static String arrayInt(ArrayList<int[]> ar){
		String res = "";
		for (int i=0; i<ar.size(); i++)
			res += "[" +intTab(ar.get(i)) + "] ";
		return res;
	}
	
	public static String arrayChemin(ArrayList<Chemin> ar){
		String res = "";
		for (int i=0; i<ar.size(); i++)
			//res += "(" + Print.intTab(ar.get(i).getTransitionInt()) + ") ";
			
			res += "(" + ar.get(i) + ") "/*: " + ar.get(i).getEtat() + " etats presents : " + ar.get(i).getListEtat() +"\n"*/;
		return res;
	}
	
	
}
