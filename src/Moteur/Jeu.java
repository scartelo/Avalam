package Moteur;

import Global.Configuration;
import Patterns.Observable;
import Structures.Iterateur;
import Structures.Sequence;

public class Jeu extends Observable implements Cloneable {
    public PlateauDeJeu plateau;
    private int tourFini;
    private boolean partieTerminee;
    public String nom_j1,nom_j2;
    public int IA1,IA2,niveauIA1,niveauIA2,IA1_ref,IA2_ref,tourDep;// IA1 = 1 si active ou 0 si inactive      niveauIA = 0 facile, 1 moyen, 2 difficile
    public boolean couleur;// true si dans l'ordre "normal" ; false si inversé
    int scoreJ1, scoreJ2;
    public Jeu(PlateauDeJeu p){
        plateau = p;
        tourFini = 0;
        partieTerminee = false;
        IA1=0;IA2=0;niveauIA1=0;niveauIA2=0;IA1_ref=0;IA2_ref=0;
        nom_j1 = "Joueur 1";
        nom_j2 = "Joueur 2";
        scoreJ1 = 0;
        scoreJ2 = 0;
    }
    public void change_ia_state(int IA, int ref){
        if(IA==0){
            IA1_ref=ref;
        }else{
            IA2_ref=ref;
        }
    }
    /*
    Renvoie une string du joueur ayant le plus grand score ou égalité
    */
    public String get_winner(){
        if(scoreJ1 == scoreJ2){
            return "Egalité entre les joueurs.";
        }else if(scoreJ1 > scoreJ2){
            return  nom_j1 + " gagne la partie.";
        }
        else{
            return nom_j2 + " gagne la partie.";
        }
    }
    public String Win_message(){
        String message = "";
        message = "La partie est terminée !" + "\n";
        message +=  get_winner() + "\n";
        message += "Merci d'avoir joué à Avalam." + "\n";
        return message;
    }
    public void nouvellePartie(){
        plateau=new PlateauDeJeu();
        plateau.tourJoueur=tourDep;
        partieTerminee = false;
        miseAJour();
    }
    /*
    Rejoue un coup qui a été annulé ( si possible )
    */
    public boolean refaire(){
        boolean b=plateau.refaireCoup();
        if(estTermine()) {
            partieTerminee = true;
            Win_message();
        }
        MAJScore();
        miseAJour();
        return b;
    }
    public void quitter(){
        System.exit(0);
    }
    /*
    Annule le dernier coup ( si possible )
    */
    public boolean annuler(){
        boolean b = plateau.annulerCoup();
        if(partieTerminee){
            partieTerminee = false;
        }
        MAJScore();
        miseAJour();
        return b;
    }
    /*
    public void clic(int l, int c){
        plateau.position(l,c);
        miseAJour();
        if(!partieTerminee && estTermine()){
            partieTerminee =true;
            Win_message();
        }
    }*/

    /*
    Sauvegarde la partie ( historique )
    */
    public void sauvegarder(){
        Saves S = new Saves(this);
        S.write_save(plateau.passe,plateau.futur);
        miseAJour();
    }

    /*
    Charge une partie si elle existe en jouant tout les coups contenu dans l'historique,
    puis en annulant les coups faisant partie du futur.
    */
    public boolean load(int n_save,int menu){
            Saves S = new Saves(this);
            if(S.saveExists(n_save)){
                plateau.initialiserGrille();
                plateau.Vider_historique();
                Sequence<Coup> seq = S.read_save(n_save);
                plateau.tourJoueur=tourDep;
                Iterateur<Coup> it = seq.iterateur();
                if (seq != null) {
                    while (it.aProchain()) {
                        Coup c = it.prochain();
                        plateau.Jouer_pos(c.src.ligne,c.src.colonne,c.dest.ligne,c.dest.colonne);
                        //jouer(c.src(), c.dest());
                    }
                    for(int i=0;i<S.taille_futur;i++){
                        //plateau.annulerCoup();
                        annuler();
                    }
                    //plateau.update_score();
                    MAJScore();
                    miseAJour();
                    return true;
                } else {
                    Configuration.instance().logger().severe("Erreur lors de la lecture de la sauvegarde");
                }
            }else {
                Configuration.instance().logger().severe("La sauvegarde n'existe pas");
            }
        return false;
    }
    public boolean partieTerminee(){
        return partieTerminee;
    }

    /*
    Regarde si aucune tour ne peut être déplacée sur le plateau
    Si c'est le cas, la partie est considérée comme terminée
    */
    public boolean estTermine(){
        boolean res=true;
        for(int i=0;i<plateau.lignes();i++){
            for(int j=0;j<plateau.colonnes();j++){
                if(plateau.grille()[i][j].estJouable()){
                    for(int x=-1;x<2;x++){
                        for(int y=-1;y<2;y++){
                            if((i+x>=0 && x+i<plateau.lignes())&&(j+y>=0 && j+y<plateau.colonnes()))  {
                                if(plateau.grille()[i][j].estDeplacable(plateau.grille()[i+x][y+j])){
                                    res = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        //plateau.update_score();
        //MAJScore();
        return res;
    }
    public boolean isIA(int joueur){
        if((joueur==0&&IA1==1)||(joueur==1&&IA2==1)){
            return true;
        }
        return false;
    }

    public void jouer(Tour src, Tour dest){
        plateau.jouer(src, dest);
        //plateau = this.plateau();
        MAJScore();
    }
    public PlateauDeJeu plateau(){
        return plateau;
    }

    @Override
    public Jeu clone(){
        Jeu clone = null;
        try {
            clone = (Jeu) super.clone();
        } catch (CloneNotSupportedException e) {
            Configuration.instance().logger().severe("Clone échoué");
            e.printStackTrace();
            System.exit(1);
        }
        return clone;
    }

    // Renvoie le score du joueur de numéro num
    public int score(int num){
        if (num == 0){
            return scoreJ1;
        }else if (num == 1){
            return scoreJ2;
        }
        return 0;
    }

    // Met à jour le score de la partie
    public void MAJScore(){
        int score1 = 0;
        int score2 = 0;
        for (int i=0; i<plateau.lignes(); i++){
            for (int j=0; j<plateau.colonnes(); j++){
                Tour tour = plateau.tour(i,j);
                if (!tour.estInnocupable() && !tour.estVide()){
                    if(plateau.pasDeplacable(tour)){
                        if (tour.sommetTour() == 0)
                            score1 += 1;
                        else
                            score2 += 1;
                    }
                }
            }
        }
        scoreJ1 = score1;
        scoreJ2 = score2;
    }

    public int scoreJ1() {
        return scoreJ1;
    }
    public int scoreJ2() {
        return scoreJ2;
    }
}
