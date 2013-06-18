package algo2;

import java.util.ArrayList;

public class SolMin {

	private ArrayList<Cellule> solHomogene;		// solutions de l'eq homogène
	private ArrayList<Cellule> solInit;			//solutions de l'equation
	
	public SolMin(){
		this.solHomogene = new ArrayList<Cellule>();
		this.solInit = new ArrayList<Cellule>();
	}
	
	
	
	public void addHomog(Cellule c){
		solHomogene.add(c);
	}
	
	public ArrayList<Cellule> getHomog(){
		return solHomogene;
	}
	
	public Cellule getHomog(int indice){
		return solHomogene.get(indice);
	}
	
	public void addInit(Cellule c){
		solInit.add(c);
	}
	
	public ArrayList<Cellule> getInit(){
		return solInit;
	}
	
	public Cellule getInit(int indice){
		return solInit.get(indice);
	}
	
	public String toString(){
		return "solutions initiale : "+ solInit + "\n solutions homogènes : " + solHomogene;
	}
	
}
