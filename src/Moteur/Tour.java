package Moteur;

public class Tour {
    public boolean propose;
    // 1 octet
    //sequence de pion| nb de pion
    //    0 0 0 1 1   | 1 0 0
    byte sequencePion;
    byte nbPion;
    int contenu;
    int ligne, colonne;
    boolean selection;
    public boolean selection_ia;
    public boolean voisin;
    private final byte INNOCCUPABLE = -1;
    private final byte TROU = 0;
    boolean proposeDest;

    public Tour(int cont, int l, int c) {
        contenu = cont;
        ligne = l;
        colonne = c;
        sequencePion = sequencePion();
        nbPion = nbPion();
        selection=false;
        selection_ia=false;
        voisin=false;
        propose=false;
    }
    public boolean marqueVoisin(){
        return voisin;}
    public int contenu(){
        return contenu;
    }
    /*
    Nombre de pion d'une tour
    */
    public byte nbPion() {
        return (byte) (contenu & 7);
    }
    /*
    Renvoie la partie de l'octet contenant la sequence de pion formant la tour
    */
    public byte sequencePion(){
        return (byte) (contenu >> 3);
    }
    /*
    Renvoie la valeur du pion au dessus de la tour
    */
    public byte sommetTour(){
        return (byte) (contenu >> 3+nbPion()-1);
    }

    /*
    Ajoute la tour t au dessus de la tour(self)
    */
    public boolean ajouteTour(Tour t) {
        if (estDeplacable(t)) {
            sequencePion |= t.sequencePion << nbPion;
            nbPion += t.nbPion;
            contenu = (sequencePion << 3) | nbPion;
            t.viderTour();
            return true;
        } else {
            return false;
        }
    }
    public byte niemePion(int i){
        byte res;
        switch(i){
            case 1:
                res=(byte) (contenu & 8);
                break;

            case 2:
                res=(byte) (contenu & 16);
                break;

            case 3:
                res=(byte) (contenu & 32);
                break;

            case 4:
                res=(byte) (contenu & 64);
                break;

            case 5:
                return sommetTour();

            default:
                return 1;
        }

        return (byte) (res >> 3+(i-1));
    }

    /*
    Renvoie si la tour T est déplacable sur la tour (self)
    */
    public boolean estDeplacable(Tour T) {
        return estVoisin(T) && (nbPion + T.nbPion() <= 5);
    }
    /*
    Renvoie si la tour T est voisine de la tour self
    */
    public boolean estVoisin(Tour T){ return (! T.estInnocupable() && !T.estVide()) && ((T.ligne==ligne || T.ligne+1==ligne || T.ligne-1 == ligne) && (T.colonne==colonne || T.colonne +1==colonne || T.colonne -1==colonne) && (!(T.ligne == ligne && T.colonne == colonne))); }
    public boolean estInnocupable(){ return contenu==-1;}
    public boolean estVide(){ return contenu==0; }
    public boolean estJouable(){
        return (nbPion<5) && (! estInnocupable() )&& (!estVide());
    }
    public boolean estComplete(){
        return nbPion == 5;
    }
    public void viderTour(){
        contenu = 0;
        nbPion = 0;
        sequencePion = 0;
    }

    public int ligne(){
        return ligne;
    }
    public int colonne(){
        return colonne;
    }

    // marquer une tour c-à-d mettre une couleur sur les 3 octects de poids forts de contenu
    // surlignage

    public void marquer(int valeur) {
        contenu = (contenu & 0xFF) | (valeur << 8);
    }

    public void marquer(boolean b) {
        selection = b;
    }

    public int marque() {
        return contenu >> 8;
    }
    public boolean estSelectionee(){
        return selection;
    }

    public boolean est_select_ia() {
        return selection_ia;
    }

    public void afficher() {
        System.out.println("Tour (" + this.ligne() + "," + this.colonne() + "), contenu = " + this.contenu()  );
    }

    public boolean marquePropose() {
        return propose;
    }

    public boolean marqueProposeDest() {
        return proposeDest;
    }
}

                                       
