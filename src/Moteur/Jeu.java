package Moteur;

import Patterns.Observable;

import javax.swing.*;

public class Jeu extends Observable {
    public PlateauDeJeu plateau;
    private int Tour_fini;
    public Jeu(PlateauDeJeu p){
        plateau=p;
        Tour_fini=0;
    }
    public void Partie(int new_game){
        if(new_game==1){
            plateau.initialiserGrille();
        }
        while(!plateau.estTermine()){
        }
        int j=plateau.get_winner();
        System.out.println("Voici les scores !\nJoueur 1: "+plateau.score1+" point(s).\nJoueur 2: "+plateau.score2+" point(s).");
        if(j==0){
            System.out.println("Egalité entre les joueurs !");
        }else {
            System.out.println("Joueur " +j + " gagne la partie !");
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
        miseAJour();
    }
    public void clic(int l, int c){
        plateau.position(l,c);
        miseAJour();
    }
    public void sauvegarder(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous sauvegarder ? ","Sauvegarder",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            plateau.sauvegarder();
        }
    }
    public void load(int n_save){
        int res = JOptionPane.showConfirmDialog(null,"Êtes vous sur de vouloir charger la sauvegarde ? ","Charger",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            int res2 = JOptionPane.showConfirmDialog(null,"Voulez vous sauvegarder avant de charger ? ","Sauvegarder",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(res2==JOptionPane.YES_OPTION){
                plateau.sauvegarder();
            }
            plateau.load_sauvegarde(n_save);
            miseAJour();
        }

    }
}
