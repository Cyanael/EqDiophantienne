import java.io.IOException;
import java.util.ArrayList;

import matriceEtArbre.AbExpr;
import matriceEtArbre.MatriceCalcul;

import outils.Print;
import outils.TabInt;

import algo2.Cellule;
import algo2.Dag;
import automate.Automate;
import automate.Chemin;
import automate.EqInit;

public class Main {

	/* ENTRE MAIN :
	 * algo que l'on veut utiliser : automate ou vecteur
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

		if (!args[0].equals("automate") && !args[0].equals("vecteur")){
			System.out.println("Le premier element d'entre doit etre l'algorithme que vous voulez utiliser : \"automate\" ou \"vecteur\"");
			return;
		}


		// ******** Gestion des paramètres d'entrée ********
		int nbEq = (args.length-1)/2;
		TabInt res = new TabInt(nbEq);			// tableau des résultats des equations
		int var[][] = new int[nbEq][];			//tableau des facteurs des variables 
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

		int nbVar = var[0].length;
		for (int k=1; k<var.length; k++){
			if (var[k].length != nbVar){
				System.out.println("Il n'y a pas le même nombre de variable dans chacune de vos équations. Veuilez modifiez votre entrée.");
				System.exit(0);
			}
		}


		long startTime = System.currentTimeMillis();

		if (args[0].equals("automate")){

			//******** Creation etat initial ********
			EqInit a = new EqInit(res, var);
			//System.out.println(a); 
			a.initSol();

			//******** Creation Automate ********
			Automate auto = new Automate(var, a);
			auto.initialisation();
			System.out.println("automate : " + Print.automate(auto));

			//******** Creation Matrice ********
			/*	MatriceCalcul mc = new MatriceCalcul(auto);
			mc.initialisation();
			System.out.println(Print.matrice(mc));
			mc = mc.routine(2);
			System.out.println(Print.matrice(mc));
			//AbExpr abExpr = mc.exprReguliere();
			//System.out.println("expression regulière  : \n" + abExpr);
			 */
			//******** Recherche des Solutions Minimales ********
			// /!\ Solutions trop longues à écrire à partir de 15 etats
			ArrayList<Chemin> ar = new ArrayList<Chemin>();
			ar = auto.solutionsMinimalesInit();		
			System.out.println("solMin init : " + Print.arrayChemin(ar));
			//ArrayList<Chemin> ar2 = new ArrayList<Chemin>();
			//ar2 = auto.solutionsMinimalesFinales();		
			// System.out.println("solMin eq homogènes : " + Print.arrayChemin(ar2));

			long endTime = System.currentTimeMillis();
			System.out.println("temps d'execution : " + (endTime-startTime));
		}
		else {		// algo des vecteurs

			//******** Creation de l'arbre ********
			Dag dag = new Dag(var, res);
			//ArrayList<Cellule> solMinGlouton = dag.solMinGlouton();
			//System.out.println("solMin : " +solMinGlouton.size() + " solutions : "+ solMinGlouton);

			long endTime1 = System.currentTimeMillis();
			//System.out.println("temps d'execution glouton: " + (endTime1-startTime));

			ArrayList<Cellule> solMinOrdre = dag.solMinOrdre();
			System.out.println("solMin 2 : " + solMinOrdre.size() +" solutions : "+ solMinOrdre);
			
			long endTime2 = System.currentTimeMillis();
			System.out.println("temps d'execution ordre: " + (endTime2-endTime1));

			ArrayList<Cellule> solMinPile = dag.solMinPile();
			System.out.println("solMin pile : " + solMinPile.size() + " solutions : "+ solMinPile);

			long endTime3 = System.currentTimeMillis();
			System.out.println("temps d'execution pile: " + (endTime3-endTime2));


		}

	}

}

