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
            NouvellePartie();
        }
        while(!plateau.estTermine()){
        }
        int j=plateau.get_winner();
        System.out.println("Voici les scores !\nJoueur 1: "+plateau.Joueur1.score+" point(s).\nJoueur 2: "+plateau.Joueur2.score+" point(s).");
        if(j==0){
            System.out.println("Egalité entre les joueurs !");
        }else {
            System.out.println("Joueur " +j + " gagne la partie !");
        }
    }

    public void NouvellePartie(){
        plateau.initialiserGrille();
    }
    public void Refaire(){
        plateau.Refaire_coup();
        miseAJour();
    }
    public void Quitter(){
        JOptionPane.showMessageDialog(null, "Merci d'avoir jouer");
        System.exit(0);
    }
    public void Annule(){
        plateau.Annuler_coup();
        miseAJour();
    }
    public void clic(int l, int c){
        plateau.position(l,c);
        miseAJour();
    }
    public void load(int n_save){
        plateau.load_sauvegarde(n_save);
        miseAJour();
    }
}
