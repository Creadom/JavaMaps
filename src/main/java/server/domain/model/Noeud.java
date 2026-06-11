package server.domain.model;

public class Noeud {
    private Info info;
    private Noeud suivant;

    // constructeurs
    public Noeud() {
        info = null;
        suivant = null;
    }

    public Noeud(Info info) {
        this.info = info;
        suivant = null;
    }

    // getteurs

    public Noeud getSuivant() {
        return suivant;
    }

    public void setSuivant(Noeud nouveau) {
        suivant = nouveau;
    }

    // setteurs

    public Info getInfo() {
        return info;
    }

    // utile lors d'échanges de noeuds, donc d'infos
    public void setInfo(Info nouveau) {
        info = nouveau;
    }

    public String toString() {
        return info.toString();
    }
}