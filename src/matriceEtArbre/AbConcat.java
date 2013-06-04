package matriceEtArbre;

public class AbConcat extends AbExpr{

	public AbConcat(AbExpr g, AbExpr d) {
		super(g, d, null, "null");
		if (g == null || d == null || g.getStr() == "null" || d.getStr() == "null"){ //ne rien construire ! => null
			this.gauche = null;
			this.droit = null;
		} 
		else
			this.str = g.getStr() + d.getStr();
		}

}
