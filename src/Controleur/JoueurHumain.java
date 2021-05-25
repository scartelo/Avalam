package Controleur;

import Global.Configuration;
import Moteur.Jeu;
import Moteur.Tour;

public class JoueurHumain extends Joueur {
    JoueurHumain(int n, Jeu j) {
        super(n, j);
    }

    @Override
    // Fonction appelée par un joueur humain pour jouer un coup
    public boolean joue(int l, int c){
        if (!jeu.estTermine()) {
            if (tourSelectionnee == null) {
                if (!jeu.plateau().tour(l, c).estJouable()) {
                    if (jeu.plateau().tour(l, c).estInnocupable())
                        Configuration.instance().logger().warning("Hors grille");
                    else
                        Configuration.instance().logger().warning("Cette tour n'est pas jouable");
                    jeu.plateau().play_sound("Error");
                    return false;
                }
                else if (jeu.plateau().estIsole(l, c)) {
                    jeu.plateau().play_sound("Error");
                    Configuration.instance().logger().warning("Cette tour est isolée, elle ne peut pas être sélectionner");
                    return false;
                } else {
                    tourSelectionnee = jeu.plateau().tour(l, c);
                    tourSelectionnee.marquer(
                            Integer.parseInt(Configuration.instance().lis("CouleurSubrillance"),16)
                    );
                    jeu.miseAJour();
                    Configuration.instance().logger().info(
                            "La tour (" + tourSelectionnee.ligne() + ", " + tourSelectionnee.colonne() + ") a été selectionnée");
                    jeu.plateau().play_sound("Pick");
                    return false;
                }
            } else {
                Tour dest = jeu.plateau().tour(l, c);
                if (dest.estJouable() && dest.estDeplacable(tourSelectionnee)) {
                    jeu.plateau().Jouer(tourSelectionnee, dest);
                    Configuration.instance().logger().info(
                            "Déplacement de la tour (" + tourSelectionnee.ligne() + "," + tourSelectionnee.colonne() + ") vers la tour (" + dest.ligne() + "," + dest.colonne() + ") effectué");
                    tourSelectionnee.marquer(0);
                    tourSelectionnee = null;
                    jeu.miseAJour();
                    return true;
                } else {
                    Configuration.instance().logger().warning("Déplacement impossible");
                    jeu.plateau().play_sound("Error");
                    tourSelectionnee.marquer(0);
                    tourSelectionnee = null;
                    jeu.miseAJour();
                    return false;
                }

            }
        }else
            Configuration.instance().logger().info("Partie terminée");
        return false;
    }


}