package Vue;

import Moteur.PlateauDeJeu;

public class testGraphique {
    public static void main(String[] args) {
        PlateauDeJeu plateau =new PlateauDeJeu();
        InterfaceGraphique ig = new InterfaceGraphique(plateau);
        plateau.Jouer(plateau.tour(3,4),plateau.tour(3,5));
        plateau.Jouer(plateau.tour(2,5),plateau.tour(3,5));
        ig.demarrer(plateau);
    }


}
