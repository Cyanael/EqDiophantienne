import java.io.IOException;
import java.util.ArrayList;

import matriceEtArbre.AbExpr;
import matriceEtArbre.MatriceCalcul;

import outils.Print;

import automate.Automate;
import automate.Chemin;
import automate.EqInit;
import automate.ResEquation;

public class Main {

	/* ENTRE MAIN :
	 * nombre d'équations : 2
	 * 
	 * liste 1ere facteurs : (-4,5,0)
	 * 1ere résultat : 1
	 * 
	 * liste 2ème facteurs : (0,-4,5)
	 * 2ème résultat : 1
	 */

	public static void main (String args[]) throws IOException{

		long startTime = System.currentTimeMillis();
		int nbEq = Integer.valueOf(args[0]);

		ResEquation res = new ResEquation(nbEq);
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
		EqInit a = new EqInit(res, var);
		System.out.println(a); 
		a.initSol();
		/*for (int i=0; i<nbEq; i++){
			System.out.println(Print.intTab(a.getFacteurs(i)));
			System.out.println("trans paire: " + a.getTransitionsPaires(i));
			System.out.println("trans impaire: " + a.getTransitionsImpaires(i));
		}*/

		
		Automate auto = new Automate(var, a);
		auto.initialisation();
		System.out.println("voisins : " + Print.arrayInt(a.listDesVoisins()));
		System.out.println("sol init : " + a.getSolutions());
		System.out.println(Print.automate(auto));
		System.out.println("sol finale : " + auto.getFinale());
		System.out.println("listSol : " + auto.getFinale().getSolutions());
		System.out.println("voisins : " + Print.arrayInt(auto.getFinale().listDesVoisins()));

		
		MatriceCalcul mc = new MatriceCalcul(auto);
		//System.out.println(Print.matrice(mc));
		mc.initialisation();
		System.out.println(Print.matrice(mc));
	
		mc = mc.routine(2);
		System.out.println(Print.matrice(mc));
		
		AbExpr abExpr = mc.exprReguliere();
		System.out.println("expression regulière  : \n" + abExpr);
		
		//ArrayList<Chemin> ar = new ArrayList<Chemin>();
		//ar = auto.solutionsMinimalesInit();		// TODO : chemins trop longs !!!
		//System.out.println("solMin init : " + Print.arrayChemin(ar));
		ArrayList<Chemin> ar2 = new ArrayList<Chemin>();
		ar2 = auto.solutionsMinimalesFinales();		
		System.out.println("solMin eq homogènes : " + Print.arrayChemin(ar2));
		
		

		long endTime = System.currentTimeMillis();
		System.out.println("temps d'execution : " + (endTime-startTime));
	}

}

// Eqution => transformer la hashMap en ArrayList<Solution>

/* On concatène après
 */
