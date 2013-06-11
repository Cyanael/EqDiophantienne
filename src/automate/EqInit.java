package automate;
import java.util.ArrayList;

import outils.TabInt;
import outils.TabString;


public class EqInit extends Equation{

	private ArrayList<TabString>[] listTransitionsPaires;
	private ArrayList<TabString>[] listTransitionsImpaires;

	public EqInit(TabInt res, int[][] facteurs){
		this.resultat = res;
		this.facteurs = facteurs;
		this.numeroEq = 0;
	}

	public void initSol(){	// parcours en profondeur pour tester toutes les solutions
		int nbVar = facteurs[0].length;
		int nbEq = this.resultat.size();
		ArrayList<Solution> listSol = new ArrayList<Solution>();
		ArrayList<TabString>[] transitionsPaires = new ArrayList[nbEq];
		ArrayList<TabString>[] transitionsImpaires = new ArrayList[nbEq];
		for (int l=0; l<nbEq; l++){
			transitionsPaires[l] = new ArrayList<TabString>();
			transitionsImpaires[l] = new ArrayList<TabString>();
		}
		TabString solution = new TabString(nbVar);
		for (int i =0; i<nbVar; i++)
			solution.setTab(i,  "0");

		Solution s = new Solution(solution, this.resultat, this.facteurs);
		double[] res = s.getEval();
		TabString trans = new TabString(nbVar);
		for (int i = 0; i<nbVar; i++)
			trans.setTab(i, solution.getTab(i));
		for (int k=0; k<nbEq; k++){
			//System.out.println("res : " + res[k]);
			if(res[k] == (int)res[k]){
				if (resultat.getRes(k)%2 == 0)
					transitionsPaires[k].add(trans);
				else
					transitionsImpaires[k].add(trans);
			}
			else{
				if (resultat.getRes(k)%2 == 0)
					transitionsImpaires[k].add(trans);
				else
					transitionsPaires[k].add(trans);
			}
		}
		boolean boucle = true;
		while(boucle){
			int indice = 0;
			while(indice<nbVar && solution.getTab(indice)=="1"){
				solution.setTab(indice, "0");
				indice++;
			}
			if (indice<nbVar){
				solution.setTab(indice, "1");
				//System.out.println("chemin "+ solution);
				Solution s1 = new Solution(solution, this.resultat, this.facteurs);
				res = s1.getEval();
				//System.out.println("solution  : " + solution);
				TabString trans1 = new TabString(nbVar);
				for (int i=0; i<nbVar; i++)
					trans1.setTab(i, solution.getTab(i));

				boolean estSol = true;
				for (int m=0; m<nbEq; m++){		// pour toutes les équations du système
					//System.out.println(" res : " + res[m]);
					if(res[m] == (int)res[m]){
						if (resultat.getRes(m)%2 == 0)
							transitionsPaires[m].add(trans1);
						else
							transitionsImpaires[m].add(trans1);
					}
					else {
						estSol = false;
						if (resultat.getRes(m)%2 == 0)
							transitionsImpaires[m].add(trans1);
						else	
							transitionsPaires[m].add(trans1);
					}
				}
				if (estSol == true){
					Solution sOk = new Solution(trans1, res);
					listSol.add(sOk);
				}
			}
			else
				boucle = false; // on a dépassé la dernière case du tableau
		}
		
		this.listSolutions = listSol;
		this.listTransitionsPaires = transitionsPaires;
		this.listTransitionsImpaires = transitionsImpaires;		
		System.out.println("init, liste des transitions possibles : " + listSol);
	}


	
	public ArrayList<TabString>[] getTransitionsPaires(){
		return this.listTransitionsPaires;
	}

	public ArrayList<TabString>[] getTransitionsImpaires(){
		return this.listTransitionsImpaires;
	}

	public ArrayList<TabString> getTransitionsPaires(int indice){
		return this.listTransitionsPaires[indice];
	}

	public ArrayList<TabString> getTransitionsImpaires(int indice){
		return this.listTransitionsImpaires[indice];
	}
}