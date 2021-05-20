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
    public void clic(int l, int c){
        plateau.position(l,c);
        miseAJour();
    }
    public void load(int n_save){
        plateau.load_sauvegarde(n_save);
        miseAJour();
    }
}
