package Moteur;

public class Tour {
    // 1 octet
    //sequence de pion| nb de pion
    // 0 0 0 1 1  | 1 0 0                   100
    byte sequencePion;
    byte nbPion;
    int contenu;
    int ligne, colonne;
    private final byte INNOCCUPABLE = -1;
    private final byte TROU = 0;

    public Tour(int cont, int l, int c){
        contenu = cont;
        ligne = l;
        colonne = c;
        sequencePion =sequencePion();
        nbPion = nbPion();
    }

    public int contenu(){
        return contenu;
    }

    public byte nbPion(){
        return (byte) (contenu & 7);
    }

    public byte sequencePion(){
        return (byte) (contenu >> 3);
    }

    public byte sommetTour(){
        return (byte) (contenu >> 3+nbPion()-1);
    }

    public boolean ajouteTour(Tour t){
        if(estDeplacable(t)){
            sequencePion |= t.sequencePion << nbPion;
            nbPion += t.nbPion;
            contenu = (sequencePion << 3) | nbPion;
            t.viderTour();
            return true;
        }
        else{return false;}
    }
    public boolean estDeplacable(Tour T){
        return estVoisin(T) && (nbPion+T.nbPion()<=5);
    }
    public boolean estVoisin(Tour T){ return (T.contenu()!=INNOCCUPABLE && T.contenu() != TROU && (T.ligne==ligne || T.ligne+1==ligne || T.ligne-1 == ligne) && (T.colonne==colonne || T.colonne +1==colonne || T.colonne -1==colonne)); }
    public boolean estInnocupable(){ return contenu==-1;}
    public boolean estVide(){ return contenu==0; }
    public boolean estJouable(){
        return nbPion<5;
    }
    public boolean estComplete(){
        return nbPion == 5;
    }
    public void viderTour(){
        contenu = 0;
        nbPion = 0;
        sequencePion = 0;
    }


}
