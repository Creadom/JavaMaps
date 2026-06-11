package server.domain.model;
public class Graphe {

	File[] liste;

	int[] lambda;

	int[] predecesseur;

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

	public int[] getLambda() {
		return lambda;
	}

	public int[] getPredecesseur() {
		return predecesseur;
	}

	public void dijkstra(int source) {
		int n = liste.length;

		lambda = new int[n];
		predecesseur = new int[n];

		for (int i = 0; i < n; i++) {
			lambda[i] = MAX;
			predecesseur[i] = -1;
		}

		boolean[] definitif = new boolean[n];
		lambda[source] = 0;

		for (int k = 1; k < n; k++) {
			int sommet = sommetMin(definitif);
			definitif[sommet] = true;

			Noeud actuel = liste[sommet].getPremier();
			while (actuel != null) {
				int voisin = actuel.getInfo().getValeur();
				int temps  = actuel.getInfo().getDist();
				if (lambda[sommet] + temps < lambda[voisin]) {
					lambda[voisin]      = lambda[sommet] + temps;
					predecesseur[voisin] = sommet;
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