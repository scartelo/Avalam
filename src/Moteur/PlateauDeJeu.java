package Moteur;

import Global.Configuration;
import Structures.Couple;
import Structures.Sequence;

public class PlateauDeJeu extends Historique<Coup>{
    private Tour[][] grille;
    private int tourJoueur;
    private int lignes, colonnes;
    private final int INNOCCUPABLE = -1;
    private final int TOURJ1 = 1, TOURJ2 = 9;
    private final int TROU = 0;


    public PlateauDeJeu(){
        lignes = 9;
        colonnes = 9;
        grille = new Tour[lignes][colonnes];
        initialiserGrille();
    }

    public void initialiserGrille() {
        int x = 0;
        for (int l=0; l<lignes; l++){
            for (int c=0; c<colonnes; c++){
                initialiserLignes(l,c);
            }
        }
    }

    private void initialiserLignes(int l, int c){
        switch (l) {
            case 0:
                initialiserLigne0( l, c);
                break;
            case 1:
                initialiserLigne1(l,c);
                break;
            case 2:
                initialiserLigne2(l,c);
                break;
            case 3:
                initialiserLigne3(l,c);
                break;
            case 4:
                initialiserLigne4(l,c);
                break;
            case 5:
                initialiserLigne5(l,c);
                break;
            case 6:
                initialiserLigne6(l,c);
                break;
            case 7:
                initialiserLigne7(l,c);
                break;
            case 8:
                initialiserLigne8(l,c);
                break;
            default:
                System.err.println("Erreur Hors grille");
        }
    }

    private void alterner(int l, int c){
        if((l+c)%2==0)
            placerTour(TOURJ1,l,c);
        else
            placerTour(TOURJ2,l,c);
    }

    private void initialiserLigne8(int l, int c) {
        if (c < 2 || c > 3)
            placerTour(INNOCCUPABLE,l,c);
        else
            alterner(l,c);
    }

    private void initialiserLigne7(int l, int c) {
        if (c < 1 || c > 4)
            placerTour(INNOCCUPABLE,l,c);
        else
            alterner(l,c);

    }

    private void initialiserLigne6(int l, int c) {
        if (c < 1 || c > 6)
            placerTour(INNOCCUPABLE,l,c);
        else
            alterner(l,c);
    }

    private void initialiserLigne5(int l, int c) {
        if (c == 0)
            placerTour(INNOCCUPABLE,l,c);
        else
            alterner(l,c);
    }


    private void initialiserLigne4(int l, int c) {
        if (c == 4)
            placerTour(TROU, l, c);

        else
            alterner(l,c);

    }

    private void initialiserLigne3(int l, int c) {
        if (c==8)
            placerTour(INNOCCUPABLE, l, c);
        else
            alterner(l,c);

    }

    private void initialiserLigne2(int l, int c) {
        if (c < 1 || c > 7)
            placerTour(INNOCCUPABLE, l, c);

        else
            alterner(l,c);

    }

    private void initialiserLigne1(int l, int c) {
        if (c < 4 || c > 7)
            placerTour(INNOCCUPABLE, l, c);

        else
            alterner(l,c);

    }

    private void initialiserLigne0(int l, int c) {
        if (c < 5 || c > 6)
            placerTour(INNOCCUPABLE, l, c);

        else
            alterner(l,c);
    }
    public void afficher_grille(){
        for(int i=0;i<lignes;i++){
            for(int j=0;j<colonnes;j++){
                if(tour(i,j).contenu() != INNOCCUPABLE && tour(i,j).contenu()!= TROU){
                    if(tour(i,j).sommetTour()==1) {
                        System.out.print("\u001B[31m" + tour(i, j).nbPion());
                    }
                    else if(tour(i,j).sommetTour()==0){
                        System.out.print("\u001B[33m" + tour(i, j).nbPion());
                    }
                }else if (tour(i,j).contenu()== TROU){
                    System.out.print(".");
                }else
                    System.out.print(" ");
            }
            System.out.println();
        }
    }

    public boolean estAccessible(int l, int c){
        return (l>0 && l<lignes) && (c>0 && c<colonnes) && !estInnoccupable(l,c);
    }

    private boolean estInnoccupable(int l, int c) {
        return tour(l,c).contenu() == INNOCCUPABLE;
    }

    public Sequence voisins(int l, int c){
        Sequence voisins = Configuration.instance().nouvelleSequence();
        for (int i=l-1; i<=l+1; i++){
            for (int j=c-1; j<=c+1 ; j++){
                if ((i!=l && j!=c) && estAccessible(i,j))
                    voisins.insereTete(new Couple<Integer,Integer>(i,j));
            }
        }
        return voisins;
    }

    public void placerTour(int contenu, int l, int c){
        Tour tour = new Tour(contenu, l, c);
        grille[tour.ligne][tour.colonne] = tour;
    }
    public int lignes(){
        return lignes;
    }
    public int colonnes(){
        return colonnes;
    }
    public Tour tour(int l, int c){
        return grille[l][c];
    }
    public void Jouer(Tour src, Tour dst){
        Tour tmp_src,tmp_dst;
        tmp_src=new Tour(src.contenu(),src.ligne,src.colonne);
        tmp_dst=new Tour(dst.contenu(),dst.ligne,dst.colonne);
        if(dst.ajouteTour(src)){
            Coup c = new Coup(tmp_src,tmp_dst);
            nouveau(c);
        }
    }
    public void Refaire_coup(){
        Coup c = refaire();
        if(c!=null){
            grille[c.dst.ligne][c.dst.colonne].ajouteTour(grille[c.src.ligne][c.src.colonne]);
        }else{
            System.out.println("Ne peut pas refaire le coup");
        }
    }
    public void Annuler_coup(){
        Coup c=annuler();
        if(c!=null) {
            placerTour(c.dst.contenu(),c.dst.ligne,c.dst.colonne);
            placerTour(c.src.contenu(),c.src.ligne,c.src.colonne);
        }
        else{
            System.out.println("Ne peut pas annuler le coup");
        }
    }
}
