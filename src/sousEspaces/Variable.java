package sousEspaces;

public abstract class Variable {

	protected int debut;
	
	
	public abstract Variable clone();
	
	public int getDebut(){
		return this.debut;
	}
	
	public void setDebut(int debut){
		this.debut = debut;
	}
}
