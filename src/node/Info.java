package node;

public class Info {

  private int valeur ;
  private int dist;
 
 

  public Info(int valeur)
  {
    this.valeur = valeur ;
  }
  
  public Info(int valeur, int dist) 
 	{
 		this.valeur = valeur ;	
 		this.dist = dist ;
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
