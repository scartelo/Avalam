package Controleur;

import Moteur.Jeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Sequence;

import java.util.Random;


public class JoueurIA extends Joueur{

    JoueurIA(int n, Jeu jj) {
        super(n, jj);
    }

    @Override
    boolean tempsEcoule(){
        return false;
    }
}
