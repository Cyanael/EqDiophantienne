package sousEspaces;

public class VarFixee extends Variable {

	private int fin;
	
	public VarFixee(int debut, int fin){
		this.debut = debut;
		this.fin = fin;
	}
	
	public VarFixee(Variable variable, int debut, int fin){
		this.debut = variable.getDebut() + debut;
		this.fin = variable.getDebut() + fin;
	}
	
	public VarFixee clone(){
		int debut = this.debut;
		int fin = this.fin;
		VarFixee clone = new VarFixee(debut, fin);
		return clone;
	}
	
	
	public int getFin(){
		return this.fin;
	}
	
	public void setFin(int fin){
		this.fin = fin;
	}
	
	public String toString(){
		return "var fixée entre [" + debut + ", " + fin +"]"; 
	}
}
