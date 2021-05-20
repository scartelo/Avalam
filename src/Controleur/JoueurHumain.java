package Controleur;

import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Tour;

public class JoueurHumain extends Joueur {
    JoueurHumain(int n , Jeu j){
        super(n,j);
    }

    @Override
    boolean jeu(int l , int c) {
        jp.clic(l,c);
            return true;

    }
}
