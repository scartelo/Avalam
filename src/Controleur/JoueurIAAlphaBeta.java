package Controleur;

import Global.Configuration;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;

public class JoueurIAAlphaBeta extends JoueurIA {
    public JoueurIAAlphaBeta(int n, Jeu j) {
        super(n,j);
    }
}
