package automate;

public class ResEquation {

	private int[] resultat;
	
	
	public ResEquation(int taille){
		int[] res = new int[taille];
		for (int i=0; i<taille; i++)
			res[i] = 0;
		this.resultat = res;
	}
	
	public ResEquation (ResEquation r){
		int[] res = new int[r.size()];
		for (int i=0; i<r.size(); i++)
			res[i] = r.getRes(i);
		this.resultat = res;
	}
	
	public boolean estNull(){
		for (int i = 0 ; i<resultat.length; i++)
			if (resultat[i]!=0)
				return false;
		return true;
	}
	
	public boolean equal(ResEquation rEq){
		if (size() != rEq.size())
			return false;			
		for (int i=0; i<size(); i++)
			if (resultat[i] != rEq.getRes(i))
				return false;
		return true;
	}
	
	
	public int size(){
		return resultat.length;
	}
	
	public int[] getRes(){
		return resultat;
	}
	
	public int getRes(int indice){
		return resultat[indice];
	}
	
	public void setRes(int[] val){
		this.resultat = val;
	}
	
	public void setRes(int indice, int val){
		this.resultat[indice] = val;
	}
	
	public String toString(){
		String res = "(";
		for (int i=0; i<resultat.length-1; i++)
			res+= resultat[i] + " ";
		res += resultat[resultat.length-1] + ")";
		return res;
	}
}
