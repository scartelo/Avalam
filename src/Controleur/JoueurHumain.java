package Controleur;

import Global.Configuration;
import Moteur.Jeu;
import Moteur.Tour;

public class JoueurHumain extends Joueur {
    JoueurHumain(int n , Jeu j){
        super(n,j);
    }

    @Override
    boolean joue(int l , int c) {
            if (tourSelectionnee == null) {
                if (jeu.plateau().estIsole(l,c)){
                    Configuration.instance().logger().warning("Cette tour est isolée, elle ne peut pas être sélectionner");
                    return false;
                }else if (!jeu.plateau().tour(l,c).estJouable()){
                    Configuration.instance().logger().warning("Cette tour n'est pas jouable");
                    return false;
                }else {
                    tourSelectionnee = jeu.plateau().tour(l, c);
                    aSelectionneTour = !aSelectionneTour;
                    Configuration.instance().logger().info("La tour a été sélectionné");
                    return false;
                }
            }else{
                    Tour dest = jeu.plateau().tour(l,c);
                    if (dest.estDeplacable(tourSelectionnee)) {
                        jeu.plateau().Jouer(tourSelectionnee, dest);
                        //aDeplaceTour = !aDeplaceTour;
                        //aSelectionneTour = !aSelectionneTour;
                        Configuration.instance().logger().info(
                                "Déplacement de la tour (" + tourSelectionnee.ligne() + "," + tourSelectionnee.colonne() + ") vers la tour (" + dest.ligne() + "," + dest.colonne() + ") effectué");
                        tourSelectionnee = null;
                        return true;
                    }else {
                        Configuration.instance().logger().warning("Déplacement impossible");
                        tourSelectionnee = null;
                        return false;
                    }

                }
        }

    }
