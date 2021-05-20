package Moteur;

import Controleur.Joueur;
import Global.Configuration;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;
public class PlateauDeJeu extends Historique<Coup>{
    private Tour[][] grille;
    public int tourJoueur;
    private int lignes, colonnes;
    private final int INNOCCUPABLE = -1;
    private final int TOURJ1 = 1, TOURJ2 = 9;
    private final int TROU = 0;
    private int x1,y1,x2,y2;
    public int score1,score2;
    public PlateauDeJeu(){
        score1=0;
        score2=0;
        tourJoueur=0;
        lignes = 9;
        colonnes = 9;
        grille = new Tour[lignes][colonnes];
        initialiserGrille();
        Init_pos();
    }
    public void Init_pos(){
        x1=-1;
        y1=-1;
        x2=-1;
        y2=-1;
    }
    public int x1(){
        return x1;
    }
    public int y1(){
        return y1;
    }
    public void position(int l,int c){
        if(l<lignes && c<colonnes) {
            if (x1 == -1 && y1 == -1) {
                if (grille[l][c].estJouable()) {
                    x1 = l;
                    y1 = c;
                } else {
                    System.err.println("Tour non déplaçable");
                }
            } else if (x1 == l && y1 == c) {
                System.err.println("Même positions");
                Init_pos();
            } else {
                if (grille[x1][y1].estDeplacable(grille[l][c])) {
                    x2 = l;
                    y2 = c;
                    Jouer_pos(x1, y1, x2, y2);
                    System.out.println("La tour a été déplacée");
                    Init_pos();
                    tourJoueur=(tourJoueur+1)%2;
                    update_score();
                    System.out.println(score1);
                    System.out.println(score2);
                    System.out.println();
                } else {
                    Init_pos();
                    System.err.println("La tour ne peut pas être déplacé ici");
                }
            }
        }else{
            System.err.println("Hors de la grille");
        }
        /*
        if(estTermine()){
            get_winner();
        }*/
    }
    public void initialiserGrille() {
        for (int l=0; l<lignes; l++){
            for (int c=0; c<colonnes; c++){
                initialiserLignes(l,c);
            }
        }
        Vider_historique();
    }
    public void update_score(){
        int score_1,score_2;
        score_1=0;
        score_2=0;
        for(int i=0;i<lignes;i++){
            for(int j=0;j<colonnes;j++){
                if(!(grille[i][j].estVide()||(grille[i][j].estInnocupable()))){
                    if(grille[i][j].nbPion>1 || (grille[i][j].nbPion==1 && estIsole(i,j))){
                        if(grille[i][j].sommetTour()==0){
                            score_1++;
                        }else{
                            score_2++;
                        }
                    }
                }
            }
        }
        score1=score_1;
        score2=score_2;
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

    public int get_winner(){
        if(score1==score2){
            return 0;
        }else if(score1>score2){
            return 1;
        }
        else{
            return 2;
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
                    voisins.insereTete(new Couple<>(i,j));
            }
        }
        return voisins;
    }
    public boolean estIsole(int l,int c){
        Sequence<Couple<Integer,Integer>> v=voisins(l,c);
        Couple<Integer,Integer> couple;
        boolean res=true;
        while(!v.estVide()){
            couple=v.extraitTete();
            int i= couple.get_premier();
            int j=couple.get_second();
            if(grille[i][j].nbPion()>0){
                res=false;
            }
        }
        return res;
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
    public boolean estTermine(){
        boolean res=true;
        for(int i=0;i<lignes;i++){
            for(int j=0;j<colonnes;j++){
                if(grille[i][j].estJouable()){
                    for(int x=-1;x<2;x++){
                        for(int y=-1;y<2;y++){
                            if((i+x>=0 && x+i<lignes)&&(j+y>=0 && j+y<colonnes))  {
                                if(grille[i][j].estDeplacable(grille[i+x][y+j])){
                                    res = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        update_score();
        return res;
    }
    public void Jouer_pos(int i_1, int j_1, int i_2, int j_2){
        Jouer(grille[i_1][j_1],grille[i_2][j_2]);
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
            update_score();
        }else{
            System.out.println("Ne peut pas refaire le coup");
        }
    }
    public void Annuler_coup(){
        Coup c=annuler();
        if(c!=null) {
            placerTour(c.dst.contenu(),c.dst.ligne,c.dst.colonne);
            placerTour(c.src.contenu(),c.src.ligne,c.src.colonne);
            update_score();
        }
        else{
            System.out.println("Ne peut pas annuler le coup");
        }
    }
    public void sauvegarder(){
        Saves S = new Saves();
        S.write_save(passe,futur);
    }
    public void load_sauvegarde(int n_save){
        Saves S = new Saves();
        if(S.saveExists(n_save)){
            System.out.println(n_save);
            initialiserGrille();
            Vider_historique();
            Sequence<Coup> seq = S.read_save(n_save);
            Iterateur<Coup> it = seq.iterateur();
            if (seq != null) {
                while (it.aProchain()) {
                    Coup c = it.prochain();
                    Jouer_pos(c.src.ligne,c.src.colonne,c.dst.ligne,c.dst.colonne);
                }
                for(int i=0;i<S.taille_futur;i++){
                    Annuler_coup();
                }
                update_score();
            } else {
                System.err.println("Erreur lors de la lecture de la sauvegarde");
            }
        }else {
            System.err.println("La sauvegarde n'existe pas");
        }
    }

}
