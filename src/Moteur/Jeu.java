package Moteur;

import Patterns.Observable;

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
}
