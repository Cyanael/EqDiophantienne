package matriceEtArbre;

import automate.Valeur;

public class AbFeuille extends AbExpr{

	public AbFeuille(Valeur val) {
		super(null, null, val, "(" + val.toString2() + ")");
	}

}
