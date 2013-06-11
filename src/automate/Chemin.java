package automate;
import java.util.ArrayList;

import outils.TabInt;
import outils.TabString;


public class Chemin{

	//private Valeur chemin; // chemin effectué
	private TabInt valeurInt;
	private int tailleChemin;
	private Equation etat ; 		// etat sur lequel on se trouve
	private ArrayList<TabInt> listEtat;	// liste des etats par lesquel on est passé

	public Chemin(TabString t, Equation nouvelEtat){
		//this.chemin = t;
		this.valeurInt = t.bin2dec();
		this.tailleChemin = 1;
		this.etat = nouvelEtat;
		listEtat = new ArrayList<TabInt>();
		listEtat.add(nouvelEtat.getRes());
	}

	public Chemin(TabString t, Equation nouvelEtat, ArrayList<TabInt> list){  //TODO : voir si encore utilisé
		//this.chemin = t;
		this.valeurInt = t.bin2dec();
		this.etat = nouvelEtat;
		this.tailleChemin = 1;
		this.listEtat = new ArrayList<TabInt>();
		for (int i=0; i<list.size(); i++)
			listEtat.add(list.get(i));
		listEtat.add(nouvelEtat.getRes());
	}
	
	public Chemin(TabInt t, int taille, Equation nouvelEtat, ArrayList<TabInt> list){  
		//this.chemin = t;
		this.valeurInt = t;
		this.tailleChemin = taille;
		this.etat = nouvelEtat;
		this.listEtat = new ArrayList<TabInt>();
		for (int i=0; i<list.size(); i++)
			listEtat.add(list.get(i));
		listEtat.add(nouvelEtat.getRes());
	}
	

	public Chemin ajoutTransition(TabString t, Equation nouvelEtat){  // concaténation des chemin pour tester les transitions

		TabInt res = new TabInt(t.size());
		//System.out.println("ajoutTransition : " + this.chemin + " et " + t);
		res = concatDebut(t);
		Chemin c = new Chemin(res, this.getTailleChemin()+1, nouvelEtat, listEtat);
		//System.out.println("fin ajout : " + Print.stringTab(res));
		//System.out.println("res : " + c);
		return c;
	}
	
	public TabInt concatDebut(TabString str){
		int tailleStr = str.size();
		TabInt res = new TabInt(tailleStr);
		for (int i=0; i<tailleStr; i++){
			res.setRes(i, (int) (Math.pow(2, tailleChemin)*Integer.valueOf(str.getTab(i)) + valeurInt.getRes(i)));
		}
		return res;
	}
	
	public int[] concatFin(TabString str){
		int tailleStr = str.size();
		int[] res = new int[tailleStr];	
		for (int i=0; i<tailleStr; i++){
			res[i] = this.valeurInt.getRes(i)*2 + Integer.valueOf(str.getTab(i));
		}
		return res;
	}
	
	public boolean estPlusPetit (Chemin chem2){ // return vrai si trans1 est plus petit que trans2
		if (this.estNull())
			return false;
		for (int i=0; i<valeurInt.size(); i++){
			if (getValeurInt(i)> chem2.getValeurInt(i))
				return false;		
		}
		return true;
	}

	public boolean estComparable(Chemin chem2){		//TODO : voir si on peut renvoyer le plus petit à la place de faire 2 fct
		if(!chem2.estPlusPetit(this) && !this.estPlusPetit(chem2))
			return false;
		return true;
	}

	public boolean etatPresentListe (TabInt etat){
		boolean present = false; 
		int i = 0;
		TabInt courant;
		while(present == false && i<listEtat.size()){
			courant = listEtat.get(i);
			present = courant.equal(etat);	
			i++;
		}
		
		return present;
	}

	public boolean estNull(){
		for (int i=0; i<valeurInt.size(); i++)
			if (valeurInt.getRes(i) != 0)
				return false;
		return true;
	}


	
	
	/*public Valeur getTransition(){
		return chemin;
	}
	
	public int[] getTransitionInt(){
		return bin2dec();
	}*/

	public TabInt getValeurInt(){
		return valeurInt;
	}
	
	public int getValeurInt(int indice){
		return valeurInt.getRes(indice);
	}
	
	public void setValeurInt(int[] val){
		this.valeurInt.setRes(val);
	}
	
	public int getTailleChemin(){
		return tailleChemin;
	}
	
	public void setTailleChemin(int taille){
		this.tailleChemin = taille;		
	}
	
	public Equation getEtat(){
		return etat;
	}

	public void setEtat(Equation nouvelEtat){
		this.etat = nouvelEtat;
	}

	public ArrayList<TabInt> getListEtat(){
		return this.listEtat;
	}

	public String toString(){
		return "(" + valeurInt + ")";
	}
}
