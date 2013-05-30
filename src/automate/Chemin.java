package automate;
import java.util.ArrayList;


public class Chemin{

	private Valeur chemin; // chemin effectué
	private Equation etat ; 		// etat sur lequel on se trouve
	private ArrayList<ResEquation> listEtat;	// liste des etats par lesquel on est passé

	public Chemin(Valeur t, Equation nouvelEtat){
		this.chemin = t;
		this.etat = nouvelEtat;
		listEtat = new ArrayList<ResEquation>();
		listEtat.add(nouvelEtat.getRes());
	}

	public Chemin(Valeur t, Equation nouvelEtat, ArrayList<ResEquation> list){
		this.chemin = t;
		this.etat = nouvelEtat;
		this.listEtat = new ArrayList<ResEquation>();
		for (int i=0; i<list.size(); i++)
			listEtat.add(list.get(i));
		listEtat.add(nouvelEtat.getRes());
	}

	public Chemin ajoutTransition(Valeur t, Equation nouvelEtat){  // concaténation des chemin pour tester les transitions

		Valeur res = new Valeur(t.size());
		System.out.println("ajoutTransition : " + this.chemin + " et " + t);
		res = chemin.concatDebut(t);
		Chemin c = new Chemin(res, nouvelEtat, listEtat);
		//System.out.println("fin ajout : " + Print.stringTab(res));
		System.out.println("res : " + c);
		return c;
	}

	private int[] bin2dec(){ //Binaire >> Decimal       
		return chemin.bin2dec();
	}

	public boolean estPlusPetit (Chemin chem2){ // return vrai si trans1 est plus petit que trans2
		int[] trans1 = this.bin2dec();
		int[] trans2 = chem2.bin2dec();
		if (this.estNull())
			return false;
		for (int i=0; i<chemin.size(); i++){
			if (trans1[i]> trans2[i])
				return false;		
		}
		return true;
	}

	public boolean estComparable(Chemin chem2){		//TODO : voir si on peut renvoyer le plus petit à la place de faire 2 fct
		if(!chem2.estPlusPetit(this) && !this.estPlusPetit(chem2))
			return false;
		return true;
	}

	public boolean etatPresentListe (ResEquation etat){
		boolean present = false; 
		int i = 0;
		ResEquation courant;
		while(present == false && i<listEtat.size()){
			courant = listEtat.get(i);
			present = courant.equal(etat);	
			i++;
		}
		
		return present;
	}

	public boolean estNull(){
		for (int i=0; i<chemin.size(); i++)
			if (chemin.getTab(i) != "0")
				return false;
		return true;
	}


	public Valeur getTransition(){
		return chemin;
	}
	
	public int[] getTransitionInt(){
		return bin2dec();
	}

	public Equation getEtat(){
		return etat;
	}

	public void setEtat(Equation nouvelEtat){
		this.etat = nouvelEtat;
	}

	public ArrayList<ResEquation> getListEtat(){
		return this.listEtat;
	}

	public String toString(){
		return chemin.toString();
	}
}
