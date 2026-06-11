package server.domain.node;
public class Noeud
    {
    private Info info ;
    private Noeud suivant ;

    // constructeurs
    public Noeud()
        {
        info = null ;
        suivant = null ;
        }

   public Noeud(Info info)
        {
        this.info = info ;
        suivant = null ;
        }

  // getteurs

    public Noeud getSuivant()
        {
        return suivant ;
        }

    public Info getInfo()
        {
        return info ;
        }

    // setteurs

    public void setSuivant(Noeud nouveau)
        {
        suivant = nouveau ;
        }
    
	public String toString()
	{
		return info.toString() ;
	}
	
    //utile lors d'�changes de noeuds, donc d'infos
    public void setInfo(Info nouveau)
        {
        info = nouveau ;
        }
    }