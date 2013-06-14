package matriceEtArbre;
import outils.Print;
import outils.TabInt;
import outils.TabString;
import automate.Automate;
import automate.Equation;


public class MatriceCalcul {

	private int exposant;
	private AbExpr[][] matrice;
	private int taille;
	private Automate auto;

	public MatriceCalcul (Automate auto){
		this.exposant = 0;
		int nbEtats = auto.nbEtats();
		//System.out.println("nb etats auto : " + nbEtats);
		this.taille = nbEtats;
		this.auto = auto;
		this.matrice = new AbExpr[nbEtats][nbEtats];
		for (int i=0; i<nbEtats; i++)		// on met tous à null
			for (int j=0; j<nbEtats; j++)
				matrice[i][j] = null;
	}
		
	public void initialisation(){	
		for (int i =0; i<auto.nbEtats(); i++){	// puis on met la bonne valeur
			Equation eq = auto.getEtat(i);
			//System.out.println("eq courante : " + eq);
			for (int j=0; j<eq.nbVoisins(); j++){
				int a = eq.numeroEq();		// ligne de la matrice : etat courant
				TabInt b = new TabInt(eq.getVoisin(j));	// resultat du voisin
				//System.out.println("voisin : " + b);
				int c = 0;						// numero du voisin (colonne)
				for (int k = 0; k<auto.getListEtats().size(); k++){
					Equation equ = auto.getEtat(k);
					if (b.equal(equ.getRes()))
						c = equ.numeroEq();
				}
				AbFeuille abf = new AbFeuille(eq.getSolution(j));
				if (matrice[a][c] == null)		
					matrice[a][c] = abf;
				else		// si il a déjà une solution, on met un "+"
					matrice[a][c] = new AbPlus(matrice[a][c], abf); // AbPlus
				}
		}
		this.exposant = 1;
	}

	
	public MatriceCalcul(int exposant, AbExpr[][] mat, int taille, Automate auto){
		this.exposant = exposant;
		this.matrice = mat;
		this.taille = taille;
		this.auto = auto;
	}
	
	public MatriceCalcul cloneMat(){
		MatriceCalcul mc = new MatriceCalcul(this.auto);
		for (int i=0; i<this.taille; i++)
			for (int j=0; j<this.taille; j++){
				mc.setMat(getMat()[i][j], i, j);
			}
		return mc;
	}

	
	public MatriceCalcul routine(int degre){  
		MatriceCalcul mcBase = new MatriceCalcul(this.exposant, this.matrice, this.taille, this.auto);	// i
		MatriceCalcul mcRes = new MatriceCalcul(this.auto);	// i+1
		
		int iPlus1 = degre-1;
		//System.out.println("i+1 : " + iPlus1);
		// tant qu'on est pas à l'exposant "degre"
		
		// calcul de i+1
		for (int i=0; i<taille; i++)
			for (int j=0; j<taille; j++){				
				
				AbEtoile abE = new AbEtoile(mcBase.getMat()[iPlus1][iPlus1]); // M(k) i+1, i+1 *
				//System.out.println("etoile " + iPlus1 + " " + iPlus1 + " " + abE);
				AbConcat abC = new AbConcat(abE, mcBase.getMat()[iPlus1][j]);
				//System.out.println("concat " + iPlus1 + " " + j + " : " + abC);
				AbConcat abC2 = new AbConcat(mcBase.getMat()[i][iPlus1], abC);
				//System.out.println("concat2 " + i + " " + iPlus1+" : " + abC2);
				AbPlus abp = new AbPlus(mcBase.getMat()[i][j], abC2); 
				//System.out.println("plus " + i + " " + j +" : " + abp + "\n");
				
				mcRes.setMat(abp, i, j);
			}
		mcRes.setExp(iPlus1+1);
		return mcRes;
	}
	
	public MatriceCalcul calculExposant(int degre){
		MatriceCalcul mc = new MatriceCalcul(auto);
		mc.initialisation();
		MatriceCalcul mc2 = new MatriceCalcul(mc.getAuto());
		for (int i=2; i<degre+1; i++){
			mc2 = mc.routine(i);
			//System.out.println(Print.matrice(mc2));  //TODO : enlever commentaires
			mc = mc2.cloneMat();
		}
		return mc2;
		
	}
	
	public AbExpr exprReguliere(){
		MatriceCalcul mc = new MatriceCalcul(auto);
		mc.initialisation();
		MatriceCalcul res =  mc.calculExposant(taille);
		if (auto.getInit().getRes().estNull())
			return res.getMat()[0][0];
		else
			return res.getMat()[0][1];
	}
	

	public AbExpr[][] getMat(){
		return this.matrice;
	}

	public void setMat(AbExpr ab, int i, int j){
		this.matrice[i][j] = ab;
	}

	public int getExp(){
		return this.exposant;
	}
	
	public void setExp(int indice){
		this.exposant = indice;
	}

	public Automate getAuto(){
		return this.auto;
	}
	
	public int getTaille(){
		return this.taille;
	}

	public int getNbFacteurs(){
		return auto.getNbFacteurs();
	}
}
