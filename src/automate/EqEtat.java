package automate;
import java.util.ArrayList;

import outils.TabInt;
import outils.TabString;


public class EqEtat extends Equation{

	public EqEtat(TabInt res, int[][] facteurs, int nbEq){
		this.resultat = res;
		this.facteurs = facteurs;
		this.numeroEq = nbEq;
	}
	
	public void initSol(ArrayList<TabString> transitions){
		ArrayList<Solution> sol = new ArrayList<Solution>();
		for (int i=0; i<transitions.size(); i++){
			Solution s = new Solution(transitions.get(i), this.resultat, this.facteurs);
			s.getEval();
			sol.add(s);
		}
		this.listSolutions = sol;

	}
}
