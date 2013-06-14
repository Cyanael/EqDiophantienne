package automate;

import java.util.ArrayList;

import outils.TabInt;
import outils.TabString;

public class Tree {

	private ArrayList<TabString> listCourante;
	private Tree gauche;	// liste paire
	private Tree droit;		// liste impaire
	private int etage;

	public Tree (){
		this.listCourante = new ArrayList<TabString>();
		this.gauche = null;
		this.droit = null;
		this.etage = 0;
	}

	public Tree (ArrayList<TabString> list, int etage){
		this.listCourante = list;
		this.gauche = null;
		this.droit = null;
		this.etage = etage;
	}

	public void nouvelEtage(ArrayList<TabString> pair, ArrayList<TabString> impair){
		Tree gauche = new Tree(pair, getEtage()+1);
		Tree droit = new Tree(impair, getEtage()+1);
		this.setGauche(gauche);
		this.setDroit(droit);
	}

	public Tree initialisation(ArrayList<TabString>[] pair, ArrayList<TabString>[] impair){
		int nbEq = pair.length;
		Tree racine = new Tree();
		racine.nouvelEtage(pair[0], impair[0]); // création racine.gauche et racine.droit

		ArrayList<Tree> file = new ArrayList<Tree>();
		file.add(racine.gauche);
		file.add(racine.droit);

		while(file.size() != 0){	
			Tree feuille;
			//System.out.println("file : " + file);
			feuille = file.get(0); // position courante
			ArrayList<TabString> feuillePair = new ArrayList<TabString>();
			ArrayList<TabString> feuilleImpair = new ArrayList<TabString>();
			ArrayList<TabString> listFeuille = feuille.getList();
			int etage = feuille.getEtage();
			for (int k=0; k<listFeuille.size(); k++){
				//System.out.println("valeur en cours : " + listFeuille.get(k));
				//System.out.println("etage : " + etage);
				if (pair[etage-1].contains(listFeuille.get(k)))
					feuillePair.add(listFeuille.get(k));
			}
			for (int k=0; k<listFeuille.size(); k++){
				if (impair[etage-1].contains(listFeuille.get(k)))
					feuilleImpair.add(listFeuille.get(k));
			}
			feuille.nouvelEtage(feuillePair, feuilleImpair);		
			if (feuille.getEtage()<nbEq-1){
				file.add(feuille.gauche);
				file.add(feuille.droit);
			}	
			//System.out.println("etage sup: " + feuille.getEtage());
			//System.out.println("liste paire : " + feuillePair);
			//System.out.println("liste impaire : " + feuilleImpair);
			file.remove(0);
		}
		return racine;
	}




	public ArrayList<TabString> recherche(TabInt indice){	//recherche de la bonne liste en fonction des résultats des équations
		Tree courant;
		//System.out.println("recherche arbre, indice : " + indice);
		if (indice.getRes(0)%2 == 0)
			courant = this.gauche;
		else
			courant = this.droit;
		for (int i=1; i<indice.size(); i++){
			if (indice.getRes(i)%2 == 0)
				courant = courant.getGauche();
			else
				courant = courant.getDroit();
		}
		return courant.getList();
	}



	public Tree getGauche(){
		return this.gauche;
	}

	public void setGauche(Tree gauche){
		this.gauche = gauche;
	}
	public Tree getDroit(){
		return this.droit;
	}

	public void setDroit(Tree droit){
		this.droit = droit;
	}

	public ArrayList<TabString> getList(){
		return this.listCourante;
	}

	public int getEtage(){
		return this.etage;
	}

	public void setEtage(int e){
		this.etage = e;
	}

	public String toString(){
		if (gauche == null && droit == null)
			return listCourante.toString();
		else
			return "(" + gauche.toString() + " / " + droit.toString() + ")";
	}


}
