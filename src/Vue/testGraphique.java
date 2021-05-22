package Vue;

import Moteur.PlateauDeJeu;
import Moteur.Jeu;
public class testGraphique {
    public static void main(String[] args) {
        PlateauDeJeu plateau =new PlateauDeJeu();
        Jeu jeu= new Jeu(plateau);
        Controleur.ControleurMediateur controle = new Controleur.ControleurMediateur(jeu);
        InterfaceGraphique ig = new InterfaceGraphique(jeu,controle);
        plateau.Jouer(plateau.tour(3,4),plateau.tour(3,5));
        plateau.Jouer(plateau.tour(2,5),plateau.tour(3,5));
        ig.demarrer(jeu,controle);
    }

}
