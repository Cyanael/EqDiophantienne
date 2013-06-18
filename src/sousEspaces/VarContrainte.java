package sousEspaces;

public class VarContrainte extends Variable {

	
	public VarContrainte(int debut){
		this.debut = debut;
	}
	
	
	public VarContrainte(Variable variable, int debut){
		this.debut = debut + variable.getDebut();
		
	}
	
	public VarContrainte clone(){
		int debut = this.debut;
		VarContrainte clone = new VarContrainte(debut);
		return clone;
	}
	
	public String toString(){
		return "var supérieure à " + debut; 
	}
}
