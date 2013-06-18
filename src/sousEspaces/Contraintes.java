package sousEspaces;

import java.util.ArrayList;

import algo2.Cellule;

import outils.TabInt;

public class Contraintes {

	private ArrayList<Variable> listVar;
	private Cellule c;
	private int[][] facteurs;
	private TabInt resultat;

	public Contraintes(int[][] facteurs, TabInt resultat){
		this.facteurs = facteurs;
		this.resultat = resultat;
		int nbVar = facteurs[0].length;
		TabInt t = new TabInt(nbVar);
		this.c = new Cellule(facteurs, t, resultat);
		this.listVar = new ArrayList<Variable>();
		for (int i=0; i<nbVar; i++){
			Variable v = new VarContrainte(0);
			listVar.add(v);		
		}
	}

	public Contraintes(Cellule c, ArrayList<Variable> contraintes, int[][] facteurs, TabInt resultat){
		this.facteurs = facteurs;
		this.resultat = resultat;
		this.c = c; 
		ArrayList<Variable> nouvelle = new ArrayList<Variable>();
		for (int i=0; i<contraintes.size(); i++){
			Variable v = contraintes.get(i).clone();
			nouvelle.add(v);
		}
		this.listVar = nouvelle;	
	}

	public void varContToFix(int indice, int debut, int fin){	// décalle l'intervale si besoin
		VarFixee v = new VarFixee(listVar.get(indice), debut, fin);
		listVar.remove(indice);
		listVar.add(indice, v);
	}

	public void mAjVarCont(int indice, int debut){
		VarContrainte v = new VarContrainte(listVar.get(indice), debut);
		listVar.remove(indice);
		listVar.add(indice, v);
	}

	//public void mAjVarFix(int indice, int debut)

	public Cellule celMin(){
		int nbVar = c.getValeur().size();
		TabInt tab = new TabInt(nbVar);
		for (int i=0; i<nbVar; i++)
			tab.setRes(i, listVar.get(i).getDebut());
		Cellule cel = new Cellule(facteurs, tab, resultat);
		return cel;
	}

	public int nbFixee(){
		int nb = 0;
		for (int i = 0; i<listVar.size(); i++)
			if (listVar.get(i) instanceof VarFixee)
				nb++;

		return nb;
	}

	public int nbCont(){
		int nb = 0;
		for (int i = 0; i<listVar.size(); i++)
			if (listVar.get(i) instanceof VarContrainte)
				nb++;

		return nb;
	}

	public Cellule getCellule(){
		return this.c;
	}

	public ArrayList<Variable> getListVar(){
		return this.listVar;
	}

	public Variable getVar(int indice){
		return listVar.get(indice);
	}

	public String toString(){
		String res = "\ncellule " + c;
		for (int i=0; i<listVar.size(); i++){
			res += "\n var " + i + " debut : " + listVar.get(i).debut;
			if (listVar.get(i) instanceof VarFixee)
				res += " fin : " + ((VarFixee) listVar.get(i)).getFin();
		}
		return res;
	}
}
