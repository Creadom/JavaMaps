package server.domain.model;

public class Info {

  private int valeur ;
  private int dist;
  private final int baseDist; // travel time with empty roads, from the map data; traffic can never go below it



  public Info(int valeur)
  {
    this.valeur = valeur ;
    this.baseDist = 0 ;
  }

  public Info(int valeur, int dist)
 	{
 		this.valeur = valeur ;
 		this.dist = dist ;
 		this.baseDist = dist ;
 	}


  //getteur

  public int getValeur()
  {
    return valeur ;
  }

  public int getDist()
  {
    return dist ;
  }

  public int getBaseDist()
  {
    return baseDist ;
  }

  //setteur

  public void setValeur(int valeur)
  {
    this.valeur = valeur ;
  }

  public void setDist(int dist)
  {
    this.dist = dist ;
  }

  public String toString()
  {
     return (valeur + " ") ;
  }
}
