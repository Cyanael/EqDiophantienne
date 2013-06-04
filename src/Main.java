import java.io.IOException;
import java.util.ArrayList;

import matriceEtArbre.AbExpr;
import matriceEtArbre.MatriceCalcul;

import outils.Print;
import outils.TabInt;

import automate.Automate;
import automate.Chemin;
import automate.EqInit;

public class Main {

	/* ENTRE MAIN :
	 * nombre d'équations : 3
	 * 
	 * liste 1ere facteurs : (-2,3,0,0)
	 * 1ere résultat : 1
	 * 
	 * liste 2ème facteurs : (0,-2,3,0)
	 * 2ème résultat : 1
	 * 
	 * liste 3ème facteurs : (0,0,-2,3)
	 * 3ème résultat : 1
	 */

	public static void main (String args[]) throws IOException{

		long startTime = System.currentTimeMillis();
		int nbEq = Integer.valueOf(args[0]);

		// ******** Gestion des paramètres d'entrée ********//
		TabInt res = new TabInt(nbEq);
		int var[][] = new int[nbEq][];
		for (int i=0; i<nbEq; i++){
			int resultat = Integer.valueOf(args[2*i+2]);
			res.setRes(i, resultat);

			String equation = args[2*i+1].substring(1,args[2*i+1].length()-1);
			String[] eq0 = equation.split(",");
			int[] eq = new int[eq0.length];
			for (int j = 0; j<eq0.length; j++)	// transformation en int
				eq[j] = Integer.valueOf(eq0[j]);
			var[i] = eq;
		}
		
		//******** Creation etat initial ********//
		EqInit a = new EqInit(res, var);
		//System.out.println(a); 
		a.initSol();

		//******** Creation Automate ********//	
		Automate auto = new Automate(var, a);
		auto.initialisation();
		
		
		//******** Creation Matrice ********//
		MatriceCalcul mc = new MatriceCalcul(auto);
		mc.initialisation();
		System.out.println(Print.matrice(mc));
		mc = mc.routine(2);
		System.out.println(Print.matrice(mc));
		//AbExpr abExpr = mc.exprReguliere();
		//System.out.println("expression regulière  : \n" + abExpr);
		
		//******** Recherche des Soluitons Minimales ********//
		// /!\ Solutions trop longues à écrire à partir de 15 etats
		ArrayList<Chemin> ar = new ArrayList<Chemin>();
		ar = auto.solutionsMinimalesInit();		
		System.out.println("solMin init : " + Print.arrayChemin(ar));
		ArrayList<Chemin> ar2 = new ArrayList<Chemin>();
		ar2 = auto.solutionsMinimalesFinales();		
		System.out.println("solMin eq homogènes : " + Print.arrayChemin(ar2));
		
		
		long endTime = System.currentTimeMillis();
		System.out.println("temps d'execution : " + (endTime-startTime));
	}

}

