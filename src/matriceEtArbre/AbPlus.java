package matriceEtArbre;


public class AbPlus extends AbExpr{

	public AbPlus(AbExpr g, AbExpr d) {
		super(g, d, null, "null");
		if (g == null && d == null)
			str = "null";
		else if (g == null || g.getStr() == "null")
			str = d.getStr();
		else if (d == null || d.getStr() == "null" )
			str = g.getStr();
		else {
			if (g instanceof AbFeuille || g instanceof AbEtoile)
				str = g.getStr();
			else
				str = "(" + g.getStr() + ")";
			if (d instanceof AbFeuille || d instanceof AbEtoile)
				str += "+" + d.getStr();
			else
				str += "+(" + d.getStr() + ")";
		}

	}


}
