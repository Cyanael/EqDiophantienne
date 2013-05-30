package matriceEtArbre;

import automate.Valeur;


public class AbExpr {

	protected AbExpr gauche;
	protected AbExpr droit;
	protected Valeur val;		// Valeur lorsque l'arbre est une feuille
	protected String str;	// écriture de l'arbre en string


	public AbExpr (AbExpr g, AbExpr d, Valeur val, String str){
		this.gauche = g;
		this.droit = d;
		this.str = str;
		this.val = val;
	}

	public AbExpr getGauche() {
		return this.gauche;
	}

	public void setGauche(AbExpr gauche) {
		this.gauche = gauche;
	}

	public AbExpr getDroit() {
		return this.droit;
	}

	public void setRight(AbExpr droit) {
		this.droit = droit;
	}

	public Valeur getVal(){
		return this.val;
	}

	public void setVal(Valeur str){
		this.val = str;
	}

	public String getStr(){
		return this.str;
	}

	public void setStr(String tag){
		this.str = tag;
	}

	public String toString() {
		return str;
	}

}
