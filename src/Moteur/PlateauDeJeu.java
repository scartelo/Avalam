package Moteur;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

public class PlateauDeJeu extends Historique<Coup> implements Cloneable{
    private Tour[][] grille;
    public int tourJoueur;
    private int lignes, colonnes;
    private final int INNOCCUPABLE = -1;
    private final int TOURJ1 = 1, TOURJ2 = 9;
    private final int TROU = 0;
    private int x1,y1,x2,y2;
    public int score1,score2;


    public PlateauDeJeu() {
        score1=0;
        score2=0;
        lignes = 9;
        colonnes = 9;
        grille = new Tour[lignes][colonnes];
        initialiserGrille();
        //Init_pos();
    }
    /*
    Initialise les positions pour les deux clics jouant un coup
    */
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
    public int lignes(){
        return lignes;
    }
    public int colonnes(){
        return colonnes;
    }
    public Tour[][] grille(){return grille;}
    public Tour tour(int l, int c){
        return grille[l][c];
    }
    public int scoreJ1(){
        return score1;
    }
    public int scoreJ2(){
        return score2;
    }
    private boolean estInnoccupable(int l, int c) {
        return tour(l,c).contenu() == INNOCCUPABLE;
    }
    public boolean estAccessible(int l, int c){
        return (l>0 && l<lignes) && (c>0 && c<colonnes) && !estInnoccupable(l,c);
    }
    /*
    Renvoie un booléen désignant si un pion est isolé sur la grille
    */
    public boolean estIsole(int l,int c){
        /*Sequence<Couple<Integer,Integer>> v=voisins(l,c);
        Couple<Integer,Integer> couple;*/
        Sequence toursVoisines = voisins(l,c);
        Sequence voisinesJouables = voisinsJouables(toursVoisines);
        Iterateur it = toursVoisines.iterateur();
        while(it.aProchain()){
            //couple=v.extraitTete();
            Tour voisine = (Tour) it.prochain();
            int i= voisine.ligne();
            int j=voisine.colonne();
            if(!grille[i][j].estVide() && !grille[i][j].estInnocupable() && grille[i][j].estDeplacable(grille[l][c]) ){
                if(grille[i][j].nbPion()>0){
                    return false;
                }
            }
        }
        return true;
    }
    /*
    Initialise la grille
    */
    public void initialiserGrille() {
        for (int l=0; l<lignes; l++){
            for (int c=0; c<colonnes; c++){
                initialiserLignes(l,c);
            }
        }
        Vider_historique();
        //Init_pos();
    }
    /*
    Initialise une case de la grille selon sa position sur la grille
    */
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
                Configuration.instance().logger().severe("Erreur Hors grille");
        }
    }
    /*
    Fonction servant à initialiser la grille alternant rouge et jaune
    */
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
        if (c < 2 || c > 7)
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
    /*
    Renvoie une séquence contenant les positions (i,j) des tours voisines qui ne sont pas hors grille
    */
    public Sequence voisins(int l, int c){
        Sequence voisins = Configuration.instance().nouvelleSequence();
        for (int i=l-1; i<=l+1; i++){
            for (int j=c-1; j<=c+1 ; j++){
                if((!(i==l && j==c)) && i<lignes && j<colonnes && i>=0 && j>=0) {
                    Tour tour = grille[i][j];
                    //voisins.insereTete(new Couple<>(i, j));
                    voisins.insereQueue(tour);
                }
            }
        }
        return voisins;
    }

    // Retourne les tours voisines encore jouables
    public Sequence voisinsJouables(Sequence voisins){
        Sequence resultat = Configuration.instance().nouvelleSequence();
        Iterateur it = voisins.iterateur();
        while (it.aProchain()){
            /*Couple<Integer, Integer> couple = (Couple<Integer, Integer>) it.prochain();
            Tour t = tour(couple.premier(),couple.second());*/
            Tour t = (Tour) it.prochain();
            if (t.estJouable()){
                resultat.insereTete(t);
            }
        }
        return resultat;
    }


    /*
    Créer une tour selon son contenu et la place sur la grille à la position i,j
    */
    public void placerTour(int contenu, int l, int c){
        Tour tour = new Tour(contenu, l, c);
        grille[tour.ligne][tour.colonne] = tour;
    }
    /*
    Déplace une tour selon ses coordonnées sur la grille (i,j)
    */
    public void Jouer_pos(int i_1, int j_1, int i_2, int j_2){
        jouer(grille[i_1][j_1],grille[i_2][j_2]);
    }

    /*
    Déplace la tour src sur la tour dst
    */
    public void jouer(Tour src, Tour dest){
        Tour tmp_src,tmp_dst;
        tmp_src=new Tour(src.contenu(),src.ligne,src.colonne);
        tmp_dst=new Tour(dest.contenu(),dest.ligne,dest.colonne);
        if(dest.ajouteTour(src)){
            Coup c = new Coup(tourJoueur,tmp_src,tmp_dst);
            nouveau(c);
            System.out.println("j'ai créé un coup avec : ");
            src.afficher();
            dest.afficher();
            tourJoueur=(tourJoueur+1)%2;
            System.out.println("j'ai joué coup  = ");
            c.afficheCoup() ;
            //Init_pos();
            //update_score();
            //play_sound("Drop");
        }
    }
    /*
    Rejoue le dernier coup annulé ( s'il existe )
    */
    public boolean refaireCoup(){
        Coup c = refaire();
        if(c!=null){
            grille[c.dest.ligne][c.dest.colonne].ajouteTour(grille[c.src.ligne][c.src.colonne]);
            //update_score();
            tourJoueur=(tourJoueur+1)%2;
            //Init_pos();
            return true;
        }else{
            System.out.println("Ne peut pas refaire le coup");
            return false;
        }
    }
    /*
    Annule le dernier coup joué ( si possible )
    */
    public boolean annulerCoup(){
        Coup c = annuler();
        if(c!=null) {
            placerTour(c.dest.contenu(),c.dest.ligne,c.dest.colonne);
            placerTour(c.src.contenu(),c.src.ligne,c.src.colonne);
            //update_score();
            tourJoueur=(tourJoueur+1)%2;
            //Init_pos();
            System.out.println("j'ai annulé le coup");
            c.afficheCoup();
            return true;
        }
        else{
            System.out.println("Ne peut pas annuler le coup");
            return false;
        }
    }
    /*
    Permet de jouer un coup
    Si il s'agit du premier clic : selection de la tour ( si elle n'est pas vide ou hors grille )
    Si il s'agit du deuxième clic : Si le coup définit par les deux clics est jouable alors le coup est joué
                                    Sinon le premier clic est deselectionné
    */
    /*public void position(int l,int c){
        if(l<lignes && c<colonnes&&l>=0 && c>=0) {
            //Premier clic
            if (x1 == -1 && y1 == -1) {
                if (grille[l][c].estJouable() && !pasDeplacable(grille[l][c])) {
                    x1 = l;
                    y1 = c;
                    play_sound("Pick");
                } else {
                    play_sound("Error");
                    System.err.println("Tour non déplaçable");
                }
            //Deuxième clic équivalent au premier = on annule le premier clic
            } else if (x1 == l && y1 == c) {
                System.err.println("Même positions");
                //Init_pos();
            } else {
                //deuxième clic différent du premier
                if (grille[x1][y1].estDeplacable(grille[l][c])) {
                    //Si jouable, alors on déplace la tour désignée par le premier clic sur celle désignée par le deuxième
                    x2 = l;
                    y2 = c;
                    Jouer_pos(x1, y1, x2, y2);
                } else {
                    play_sound("Error");
                    //Init_pos();
                    System.err.println("La tour ne peut pas être déplacé ici");
                }
            }
        }else{
            play_sound("Error");
            System.err.println("Hors de la grille");
        }
    }*/
    /*
    Met à jour le score des deux joueurs selon l'état du plateau
    */
    public void update_score(){
        int score_1,score_2;
        score_1=0;
        score_2=0;
        for(int i=0;i<lignes;i++){
            for(int j=0;j<colonnes;j++){
                if(!(grille[i][j].estVide()||(grille[i][j].estInnocupable()))){
                    //if(grille[i][j].nbPion()>1 || estIsole(i,j)){// VERSION_1
                    if(pasDeplacable(grille[i][j])){ // VERSION_2
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
    /*
    Joue un son dans res/Audio selon sound_name ( nom du fichier )
    *//*
    public void play_sound(String sound_name){
        Audio sound= null;
        try {
            sound = new Audio(sound_name);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        sound.play();
    }*/
    /*
    Affiche la grille sur le terminal
    */

    public void afficherGrille(){
        for(int i=0;i<lignes;i++){
            for(int j=0;j<colonnes;j++){
                if(tour(i,j).contenu() != INNOCCUPABLE && tour(i,j).contenu()!= TROU){
                    if(tour(i,j).sommetTour()==0) {
                        System.out.print("\u001B[31m" + tour(i, j).nbPion());
                    }
                    else if(tour(i,j).sommetTour()==1){
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
    public boolean pasDeplacable(Tour t){
        /*Sequence<Couple<Integer,Integer>> v=voisins(t.ligne,t.colonne);
        Couple<Integer,Integer> couple;*/
        Sequence toursVoisines = voisins(t.ligne(), t.colonne());
        Iterateur it = toursVoisines.iterateur();
        while(it.aProchain()){
            /*couple=v.extraitTete();
            int i= couple.premier();
            int j=couple.second();*/
            Tour voisine = (Tour) it.prochain();
            if(t.estDeplacable(grille[voisine.ligne()][voisine.colonne()])){
                return false;
            }
        }
        return true;
    }

    public void deselection_ia() {
        for(int i=0;i<lignes;i++){
            for(int j=0;j<colonnes;j++){
                if(grille[i][j].selection_ia){
                    grille[i][j].selection_ia=false;
                }
            }
        }
    }

    public void selection_ia(Tour departTour, Tour destTour) {
        departTour.selection_ia=true;
        destTour.selection_ia=true;
    }

    @Override
    public PlateauDeJeu clone(){
        PlateauDeJeu clone = null;
        try {
            clone = (PlateauDeJeu) super.clone();
            clone.grille = grille.clone();

        } catch (CloneNotSupportedException e) {
            Configuration.instance().logger().severe("Clone échoué");
            e.printStackTrace();
            System.exit(1);
        }
        return clone;
    }
    public void deselect_propose(){
        for(int i=0;i<lignes;i++){
            for(int j=0;j<colonnes;j++){
                if(grille[i][j].propose){
                    grille[i][j].propose=false;
                }
            }
        }
    }
}
