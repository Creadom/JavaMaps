package server.domain.model;
public class Graphe {

	File[] liste;

	int[] lambda;

	final int MAX = 10000;

	public Graphe() {

	}

	public Graphe(int n) {

		liste = new File[n];
		for (int i = 0; i < n; i++)
			liste[i] = new File();

	}

	public Graphe(int matrice[][]) {
		int n = matrice.length; // n=nombre de sommets

		liste = new File[n];
		for (int i = 0; i < n; i++)
			liste[i] = new File();

		for (int i = 0; i < matrice.length; i++) // boucle sur la matrice
			for (int j = 0; j < matrice[i].length; j++)
				liste[i].enfile(new Noeud(new Info(matrice[i][j])));

	}

	public Graphe(int matriceS[][], int matriceL[][]) {
		int n = matriceS.length; // n=nombre de sommets

		liste = new File[n];
		for (int i = 0; i < n; i++)
			liste[i] = new File();

		for (int i = 0; i < matriceS.length; i++) // boucle sur la matrice
			for (int j = 0; j < matriceS[i].length; j++)
				liste[i].enfile(new Noeud(new Info(matriceS[i][j], matriceL[i][j])));
	}

	// Affichages
	// _____________________________________________________________________________________________________

	public void afficheListe() {
		for (int i = 0; i < liste.length; i++) {
			System.out.print("file[" + i + "]: ");
			liste[i].affiche();

			//System.out.println();
		}

	}

	public void afficheSuccEtDist() {
		for (int i = 0; i < liste.length; i++) {
			System.out.print("ligne " + i + ": ");
			Noeud courant = liste[i].getPremier();
			while (courant != null) {
				System.out.print(courant.getInfo().getValeur() + "/");
				System.out.print(courant.getInfo().getDist() + "\t");
				courant = courant.getSuivant();
			}
			System.out.println();
		}
		System.out.println();

	}

	public void afficheLambda() {
		for (int i = 0; i < lambda.length; i++)
			System.out.print(lambda[i] + "\t");

		System.out.println();
	}

	




// Algorithme de Dijkstra 
//-----------------------------------------------------------------------------
// pr�conditions : valeurs >= 0 et tous les sommets atteignables

	public void dijkstra() // ICI DE 0
	{
		int n = liste.length; // n = nombre de sommets

		lambda = new int[n];
		

		for (int i = 0; i < n; i++) // initialisation
			lambda[i] = MAX;

	
		boolean definitif[] = new boolean[n];

	
		lambda[0] = 0;

		for (int k = 1; k < n; k++) // n étapes : PRECONDITIONS TOUS LES SOMMETS ATTEIGNABLES
		{
			// affichage
			afficheLambda();
			// recherche du sommet minimum -> (pas optimal -> TAS)
			int sommet = sommetMin(definitif);
			definitif[sommet] = true;


			// ajustement des valeurs
			Noeud actuel = liste[sommet].getPremier();
			while (actuel != null) {
				if(lambda[sommet] + actuel.getInfo().getDist() < lambda[actuel.getInfo().getValeur()])
				{
					lambda[actuel.getInfo().getValeur()] = lambda[sommet] + actuel.getInfo().getDist();
				}

				actuel = actuel.getSuivant();
			}
			
			
		}

	}
	private int sommetMin(boolean[] definitif) {
		int sommet = -1;
		int min = MAX;

		for (int i = 0; i < liste.length; i++) {
			if (!definitif[i] && lambda[i] < min) {
				min = lambda[i];
				sommet = i;
			}
		}

		return sommet;
	}
	
	

	

}