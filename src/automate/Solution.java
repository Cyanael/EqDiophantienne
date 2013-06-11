package automate;

import outils.TabInt;
import outils.TabString;

public class Solution {

	private TabString var;	// valeurs des variables
	private double[] eval;	// evaluation de la solution

	public Solution(TabString var, TabInt resultat, int[][] facteurs){
		this.var = var;
		calculerEval(facteurs, resultat);
	}

	public Solution(TabString var, double[] eval){
		this.var = var;
		this.eval = eval;
	}

	public void calculerEval(int[][] facteurs, TabInt resultat){
		double[] eqBase = new double[resultat.size()];
		for (int k = 0; k < eqBase.length; k++)
			eqBase[k] = 0;
		for (int i =0; i<facteurs.length; i++){
			for (int j=0; j<facteurs[i].length; j++){
				//System.out.println("eqBase avant : " + eqBase[i]);
				eqBase[i] += (facteurs[i][j]*Integer.valueOf(var.getTab(j)));
				//System.out.println(facteurs[i][j]+ " * " + Integer.valueOf(var.getTab(j)));
			}
			//System.out.println("res-base/2 : " + resultat[i] + "-" + eqBase[i] + "/2");
			eqBase[i] = (resultat.getRes(i)- eqBase[i])/2;		// ex : (res- x+2y-3z)/2 
		}
		this.eval = eqBase;
		//System.out.println("eval solution: " + res);

	}


	public Solution clonee(){		// pour pouvoir mettre dans la liste des solutions
		Solution s2 = new Solution(getVar().clonee(), getEval());
		return s2;
	}

	public String[] cloneVar(){
		String[] tab = new String[var.size()];
		for (int i = 0; i<var.size(); i++)
			tab[i] = var.getTab(i);
		return tab;
	}


	public TabString getVar(){
		return this.var;
	}

	public double[] getEval(){
		return this.eval;		
	}	

	public String toString(){
		return var.toString();
	}

	public String evalString(){
		String res = "solution : ";
		res += toString();
		res+= " avec l'évaluation : " + eval;
		return res;	
	}

}
