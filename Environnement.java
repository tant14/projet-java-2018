import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;



public class Environnement {
	static final int NB_COL = 25; //le nombre de colonnes et de lignes pour la matrice des élèves
	static final int NB_LIGNE = 100; 
	static String[][] emploidutemps; 
	static int[][] eleves;
	static ArrayList<Matière> liste_mat;
	
	public Environnement(){
		emploidutemps = new String[12][12];	//les valeurs ici sont probablement à changer
		eleves = new int[NB_LIGNE][NB_COL];
		liste_mat = new ArrayList<Matière>();
	}
	
	//lit le fichier etu, rentre les matières
	public static void lire_etu (String s){
		Charset charset = Charset.forName("ISO_8859_1");
		List<String> lines;
		Path p = Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator + s);
		try{
			lines = Files.readAllLines(p,charset);
			nouvelles_matieres(lines.get(0));
		}
		catch (IOException e){
			System.out.println(e);
		}	
	}
	
	//appelée par lire_etu, rentre toutes les matières dans la liste des matières
	public static void nouvelles_matieres(String ligne){
		String[] tab = ligne.split(";");
		String[] espace;
		for (int i=1; i<tab.length; i++){
			espace = tab[i].split(" ");
			if (espace.length >1){	//il y a de legeres differences entre edt et etu... :-(
				tab[i]=espace[0].concat(espace[1]);
			}
			liste_mat.add(new Matière(tab[i]));
		}
	}
	
	
	//celle-là fonctionne, quand il n'y a rien marqué dans une case, il y a null
	//met tout l'emploi du temps dans emploidutemps (même les jours...)
	public static void lire_edt(String s){
		Charset charset = Charset.forName("ISO_8859_1"); //on dfn un nouveau charset
		List<String> lines;
		String str;
		String[] tab;
		//on dfn le chemin
		Path p = Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator + s);
		try{
			//on récupère toutes les lignes et on les inscrit dans emploidutemps
			lines = Files.readAllLines(p, charset);
			for (int i= 0; i<lines.size(); i++){
				str = lines.get(i);
				tab = str.split(";");
				for (int j= 0; j<tab.length; j++){
					emploidutemps[i][j] = tab[j];
				}
			}
		}
		catch(IOException e){
			System.out.println(e);
			
		}
	}
	
	//on va mettre tous les cours, les td, leurs horaires,... en mémoire
	//A FINIR !!!!!! Il faut réussir à enlever les doublons des listes de cours et de voir pourquoi il y a des numéros 
	//apparaissent quand on affiche toutes les listes de cours
	public static void miseajour_mat(){
		for (int i=1; i<12; i++){	//on parcourt tout l'emploi du temps (sauf les jours)
			for (int j=0; j<12; j++){
				if (emploidutemps[i][j] != null){	//si on a une matière
					String[] espace = emploidutemps[i][j].split(" ");
					for (int l = 0; l<espace.length; l++){	//si on a plusieurs matières en même temps
						String[] tiret = espace[l].split("_");
						for (int k=0; k<liste_mat.size(); k++){	//on cherche la matière correspondante dans la liste des matières
							if (liste_mat.get(k).name.equals(tiret[0])){
								if (tiret.length>1){	//il s'agit d'un td (il y a un numero dans le nom)
									int nb = Integer.parseInt(tiret[1]);
									System.out.println("le td est "+ tiret[0]+ "son numero est " + tiret[1]);
									//on voudrait faire en sorte que dans la liste, on ait par ex "com, com_1, com_2"
									if (liste_mat.get(k).liste_cours.size()<nb)
										liste_mat.get(k).liste_cours.add(new Cours(espace[l], i, j, 0));
									else
										liste_mat.get(k).liste_cours.add(nb, new Cours(espace[l], i, j, 0));
								}
								else{	//il s'agit d'un cours, et on l'ajoute au début de la liste
									liste_mat.get(k).liste_cours.add(0, new Cours(espace[l], i, j, 0));
								}
								break;
							}
						}
					}
				}
			}
		}
		
	}
	
	
	
	
	public static void main (String[] args){
		Environnement e = new Environnement();
		String s = "edt(2).csv";
		lire_edt (s);
		/*for (int i=0; i<8; i++){
			for (int j=0; j<6; j++){
				System.out.println(i+" "+j+emploidutemps[i][j]);
			}
		}*/
		lire_etu("etu.csv");
		miseajour_mat();
		for (int i=0; i<liste_mat.size();i++){
			System.out.println(liste_mat.get(i).liste_cours.size());
			for (int j=0; j<liste_mat.get(i).liste_cours.size(); j++){
				System.out.println(liste_mat.get(i).liste_cours.get(j).nom);
			}
		}
	}
}
