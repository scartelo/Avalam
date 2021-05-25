package Controleur;

import Global.Configuration;
import Moteur.Jeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;

import java.util.Random;

public class JoueurIAAleatoire extends JoueurIA{
    Random r;

    JoueurIAAleatoire(int n, Jeu jeu){
        super(n,jeu);
        r = new Random();
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
    @Override
    boolean tempsEcoule() {
        Tour departTour = selectionAleatoire();
        Configuration.instance().logger().info(
                "La tour (" + departTour.ligne() + ", " + departTour.colonne() + ") a été selectionnée");
        Sequence voisinsDepart = jeu.plateau.voisins(departTour.ligne(), departTour.colonne());
        int position = r.nextInt(8); // choix aléatoire parmi les 8 voisins
        int i = 0;
        Iterateur it = voisinsDepart.iterateur();
        while (it.aProchain()){
                if (i == position){
                    Couple c = (Couple) it.prochain();
                    int destL = (int) c.premier();
                    int destC = (int) c.second();
                    Tour destTour = jeu.plateau().tour(destL, destC);
                    if (destTour.estDeplacable(departTour) && destTour.estJouable()){
                        jeu.plateau().Jouer(departTour, destTour);
                        jeu.miseAJour();
                        if(jeu.estTermine()){
                            jeu.Win_message();
                            Configuration.instance().logger().info("Partie terminée");
                        }
                        return true;
                    }
                }else {
                    i++;
                }

        }
        return false;
    }
}
