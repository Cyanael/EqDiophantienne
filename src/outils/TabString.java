package outils;

public class TabString {

	private String[] tableau;
	
	
	public TabString(int taille){
		this.tableau = new String[taille];
	}
	
	public TabString(TabString v){
		String[] res = new String[v.size()];
		for (int i=0; i<v.size(); i++)
			res[i] = v.getTab(i);
		this.tableau = res;
	}
	

	
	public int[] bin2dec(){ //Binaire >> Decimal
		int[] res = new int[tableau.length];
		for (int i = 0; i<tableau.length; i++){
			res[i] =Integer.parseInt(tableau[i], 2);
		}         
		return res;
	}
	
	public TabString clonee(){
		TabString clone = new TabString(tableau.length);
		for (int i=0; i<tableau.length; i++)
			clone.setTab(i, tableau[i]);
		return clone;
	}
	
	
	
	public String[] getTab(){
		return this.tableau;
	}
	
	public String getTab(int indice){
		return this.tableau[indice];
	}
	
	public void setTab(String[] tab){
		this.tableau = tab;
	}
	
	public void setTab(int indice, String str){
		this.tableau[indice] = str;
	}
	
	public int size(){
		return tableau.length;
	}
	
	public String toString(){
		return "(" + toString2() + ")";
			
	}
	
	public String toString2(){
		String res = "";
		for (int i = 0; i<tableau.length-1; i++)
			res += tableau[i] + " ";
		res += tableau[tableau.length-1];
		return res;
	}
	
	
}
