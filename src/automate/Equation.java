package automate;
import java.util.ArrayList;

import outils.Print;


public abstract class Equation {

	protected ResEquation resultat;
	protected int[][] facteurs;
	protected ArrayList<Solution> listSolutions;
	protected int numeroEq;
	

	public ResEquation getRes(){
		return this.resultat;
	}	

	public int[][] getFacteurs(){
		return this.facteurs;
	}
		//facteurs de l'équation indice
	public int[] getFacteurs(int indice){
		return this.facteurs[indice];
	}
		// facteur n° j de l'équation i
	public int getFacteur(int i, int j){
		return this.facteurs[i][j];
	}

	public ArrayList<Solution> getSolutions(){
		return this.listSolutions;
	}
		//facteurs de la solution n° indice
	public Valeur getSolution(int indice){
		return  listSolutions.get(indice).getVar();
	}
		//résultat du voisin n° indice
	public ResEquation getVoisin(int indice){
		double[] resVoisin = listSolutions.get(indice).getEval();
		ResEquation res = new ResEquation(resVoisin.length);
		for (int i=0; i<resVoisin.length; i++)
			res.setRes(i, (int) resVoisin[i]);
		return res;
	}
	
	public ArrayList<int[]> listDesVoisins(){
		ArrayList<int[]> ar = new ArrayList<int[]>();
		for (int i=0; i<listSolutions.size(); i++){
			double[] d = listSolutions.get(i).getEval();
			int[] dToInt = new int[d.length];
			for (int j=0; j<d.length; j++)
				dToInt[j] = (int) d[j];
			ar.add(dToInt);
		}
		return ar;		
	}
	
	public boolean estVoisin(Equation e){
		ArrayList<int[]> voisins = listDesVoisins();
		for (int i=0; i<voisins.size(); i++){
			boolean v = true;
			for (int j=0; j<voisins.get(i).length; j++){
				if (voisins.get(i)[j]!=e.getRes().getRes(j))
					v = false;
			}
			if (v == true)
				return true;
		}
		return false;
	}

	
	public int nbVoisins(){
		return listSolutions.size();
	}

	public int numeroEq(){
		return this.numeroEq;
	}
	
	public String toString(){
		String str = "resultat : " + this.resultat + "\n";/*facteur :" ;
		for (int i = 0; i<facteurs.length; i++)
			str += " " + Print.intTab(this.facteurs[i])+ "\n";*/
		return str;
	}
}
