package Controleur;

import Global.Configuration;
import Moteur.Jeu;
import Moteur.Tour;

import java.util.Random;


public class JoueurIA extends Joueur{
    Random r;

    JoueurIA(int n, Jeu j) {
        super(n, j);
        r = new Random();
    }

    /*
    static JoueurIA joueurIA(int n, Jeu j){
        if (instance == null){
            return new JoueurIA(n,j);
        }
        return instance;
    }*/

    @Override
    boolean tempsEcoule(){
        return false;
    }

    Tour selectionAleatoire(){
        int departL = r.nextInt(jeu.plateau().lignes());
        int departC = r.nextInt(jeu.plateau().colonnes());
        Tour departTour = jeu.plateau().tour(departL,departC);
        while (!departTour.estJouable() || jeu.plateau().estIsole(departL, departC)){
            departL = r.nextInt(jeu.plateau().lignes());
            departC = r.nextInt(jeu.plateau().colonnes());
            departTour = jeu.plateau().tour(departL,departC);
        }
        return departTour;
    }
}
