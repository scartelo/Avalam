package Moteur;

import Patterns.Observable;
import Structures.Iterateur;
import Structures.Sequence;

import javax.swing.*;

public class Jeu extends Observable {
    public PlateauDeJeu plateau;
    private int Tour_fini;
    private boolean partie_terminee;
    public Jeu(PlateauDeJeu p){
        plateau=p;
        Tour_fini=0;
        partie_terminee=false;
    }

    public String get_winner(){
        if(plateau.score1==plateau.score2){
            return "Egalité entre les joueurs";
        }else if(plateau.score1>plateau.score2){
            return "Joueur 1 gagne la partie";
        }
        else{
            return "Joueur 2 gagne la partie";
        }
    }
    public void NouvellePartie(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous recommencer la partie ? ","Nouvelle partie",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            plateau.initialiserGrille();
            miseAJour();
        }
    }
    public void Refaire(){
        plateau.Refaire_coup();
        miseAJour();
    }
    public void Quitter(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous vraiment quitter ? ","Quitter le jeu",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }

    public void Annule(){
        plateau.Annuler_coup();
        if(partie_terminee){
            partie_terminee=false;
        }
        miseAJour();
    }
    public void clic(int l, int c){
        plateau.position(l,c);
        if(estTermine()){
            partie_terminee=true;
        }
        miseAJour();
    }
    public void sauvegarder(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous sauvegarder ? ","Sauvegarder",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            Saves S = new Saves();
            S.write_save(plateau.passe,plateau.futur);
        }
    }
    public void load(int n_save,int menu){
        int res = JOptionPane.showConfirmDialog(null,"Êtes vous sur de vouloir charger la sauvegarde ? ","Charger",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            if(menu==0) {
                sauvegarder();
            }
            Saves S = new Saves();
            if(S.saveExists(n_save)){
                System.out.println(n_save);
                plateau.initialiserGrille();
                plateau.Vider_historique();
                Sequence<Coup> seq = S.read_save(n_save);
                Iterateur<Coup> it = seq.iterateur();
                if (seq != null) {
                    while (it.aProchain()) {
                        Coup c = it.prochain();
                        plateau.Jouer_pos(c.src.ligne,c.src.colonne,c.dst.ligne,c.dst.colonne);
                    }
                    for(int i=0;i<S.taille_futur;i++){
                        plateau.Annuler_coup();
                    }
                    plateau.update_score();
                } else {
                    System.err.println("Erreur lors de la lecture de la sauvegarde");
                }
            }else {
                System.err.println("La sauvegarde n'existe pas");
            }
        }
        miseAJour();
    }
    public boolean partie_terminee(){
        return partie_terminee;
    }
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
}
