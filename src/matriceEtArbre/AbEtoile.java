package matriceEtArbre;

public class AbEtoile extends AbExpr{

	public AbEtoile(AbExpr g) {
		super(g, null, null, "(" + g.getStr() + ")*");
		if (g.getStr() == "null")
			this.str = "";
		
	}

}
