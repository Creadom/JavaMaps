package server.domain.model;
public class File {

  private int longueur ;
  private Noeud premier ;
  private Noeud dernier ;

  //constructeur
  public File()
  {
    longueur = 0 ;
    premier = null ;
    dernier = null ;
  }

    // getteurs
  public int getLongueur()
  {
    return longueur ;
  }

  public Noeud getPremier()
  {
    return premier ;
  }

  public Noeud getDernier()
  {
    return dernier ;
  }

    // setteurs
  public void setLongueur(int longueur)
  {
    this.longueur = longueur ;
  }

  public void setPremier(Noeud premier)
  {
    this.premier = premier ;
  }

  public void setDernier(Noeud dernier)
  {
    this.dernier = dernier;
  }

    // m�thodes

  public boolean estVide()
  {
    return (longueur == 0) ;
  }

  public void enfile(Noeud nouveau)
  {
    if (estVide())
      premier = nouveau ;
    else
      dernier.setSuivant(nouveau) ;

    dernier = nouveau ;

    ++longueur ;
  }

  public Noeud defile()
  {
    if (longueur == 0)
      return null ;

    Noeud debut = premier ;
    premier = premier.getSuivant() ;

    if (longueur == 1)
      dernier = null ;
    else
      debut.setSuivant(null) ;

    --longueur ;
    return debut ;
  }


  public void concat(File file2)
  {
    if (file2.estVide())
      return ;

    if (estVide())
      premier = file2.getPremier();
    else
      dernier.setSuivant(file2.getPremier()) ;

    dernier = file2.getDernier() ;
    longueur += file2.getLongueur() ;

    file2.setLongueur(0);
    file2.setPremier(null);
    file2.setDernier(null);
  }



  public void affiche()
  {
    Noeud courant = premier ;

    while (courant != null)
    {
      System.out.print(courant.getInfo().toString()+ " ");
      courant = courant.getSuivant() ;
    }
    System.out.println();

  }
  
 

  public String toString()
  {
    String chaine = "" ;
    Noeud courant = premier ;

    while (courant != null)
    {
      chaine += courant.getInfo().toString() + " ";
      courant = courant.getSuivant() ;
    }
    return chaine ;
  }


}
