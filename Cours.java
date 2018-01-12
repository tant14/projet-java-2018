import java.util.ArrayList;


public class Cours {
	String nom;
	int horaire;	//la ligne sur l'emploi du temps
	int jour;		//la colonne sur l'emploi du temps
	int num_col;	//la colonne sur la matrice des étudiants
	ArrayList<Integer> liste_etudiant;
	
	public Cours(String n, int heure, int j, int n_col){
		nom = n;
		horaire = heure;
		jour = j;
		num_col = n_col;
		liste_etudiant = new ArrayList<Integer>(); //on ne sait pas quels étudiants seront dedans
	}
}
