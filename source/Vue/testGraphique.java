package Vue;

import Moteur.PlateauDeJeu;

public class testGraphique {
    public static void main(String[] args) {
        PlateauDeJeu plateau =new PlateauDeJeu();
        InterfaceGraphique ig = new InterfaceGraphique(plateau);
        ig.demarrer(plateau);
    }


}
