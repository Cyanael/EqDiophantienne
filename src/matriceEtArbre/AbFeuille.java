package matriceEtArbre;

import outils.TabString;

public class AbFeuille extends AbExpr{

	public AbFeuille(TabString val) {
		super(null, null, val, "(" + val.toString2() + ")");
	}

}
