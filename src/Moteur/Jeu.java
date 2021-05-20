package Moteur;

import Patterns.Observable;

import javax.swing.*;

public class Jeu extends Observable {
    public PlateauDeJeu plateau;

    public Jeu(PlateauDeJeu p){
        plateau=p;
    }

    public void Refaire(){
        plateau.Refaire_coup();
        miseAJour();
    }
    public void Annule(){
        plateau.Annuler_coup();
        miseAJour();
    }
    public void Quitter(){
        JOptionPane.showMessageDialog(null, "Merci d'avoir jouer");
        System.exit(0);
    }
}
