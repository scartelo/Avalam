package Moteur;

import Global.Configuration;
import Patterns.Observable;
import Structures.Iterateur;
import Structures.Sequence;

import javax.swing.*;

public class Jeu extends Observable {
    public PlateauDeJeu plateau;
    private int tourFini;
    private boolean partieTerminee;
    public String nom_j1,nom_j2;
    public int IA1,IA2,niveauIA1,niveauIA2; // IA1 = 1 si active ou 0 si inactive      niveauIA = 0 facile, 1 moyen, 2 difficile
    private Tour tourSelectionnee;

    public Jeu(PlateauDeJeu p){
        plateau = p;
        tourFini = 0;
        partieTerminee = false;
        IA1=0;IA2=0;niveauIA1=0;niveauIA2=0;
        nom_j1 = "Joueur 1";
        nom_j2 = "Joueur 2";
    }
    /*
    Renvoie une string du joueur ayant le plus grand score ou égalité
    */
    public String get_winner(){
        if(plateau.score1==plateau.score2){
            return "Egalité entre les joueurs.";
        }else if(plateau.score1>plateau.score2){
            return  nom_j1+" gagne la partie.";
        }
        else{
            return nom_j2+" gagne la partie.";
        }
    }
    public void Win_message(){
        JOptionPane.showMessageDialog(null,"La partie est terminée !\n"+get_winner()+"\nMerci d'avoir joué à Avalam.","Partie terminée",JOptionPane.PLAIN_MESSAGE);
    }
    public void nouvellePartie(){
        plateau.initialiserGrille();
        miseAJour();
    }
    /*
    Rejoue un coup qui a été annulé ( si possible )
    */
    public void refaire(){
        plateau.refaireCoup();
        miseAJour();
        if(estTermine()) {
            partieTerminee = true;
            Win_message();
        }
    }
    public void quitter(){
        System.exit(0);
    }
    /*
    Annule le dernier coup ( si possible )
    */
    public void annuler(){
        plateau.annulerCoup();
        if(partieTerminee){
            partieTerminee =false;
        }
        miseAJour();
    }
    public void clic(int l, int c){
        plateau.position(l,c);
        miseAJour();
        if(!partieTerminee && estTermine()){
            partieTerminee =true;
            Win_message();
        }
    }

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
                plateau.tourJoueur=0;
                plateau.Vider_historique();
                Sequence<Coup> seq = S.read_save(n_save);
                Iterateur<Coup> it = seq.iterateur();
                if (seq != null) {
                    while (it.aProchain()) {
                        Coup c = it.prochain();
                        plateau.Jouer_pos(c.src.ligne,c.src.colonne,c.dst.ligne,c.dst.colonne);
                    }
                    for(int i=0;i<S.taille_futur;i++){
                        plateau.annulerCoup();
                    }
                    plateau.update_score();
                    miseAJour();
                    return true;
                } else {
                    System.err.println("Erreur lors de la lecture de la sauvegarde");
                }
            }else {
                System.err.println("La sauvegarde n'existe pas");
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
        plateau.update_score();
        return res;
    }

    public PlateauDeJeu plateau(){
        return plateau;
    }




}
