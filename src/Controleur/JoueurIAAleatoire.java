package Controleur;

import Moteur.Jeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;
import Structures.SequenceTableau;

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
        while (!departTour.estJouable()){
            departL = r.nextInt(jeu.plateau().lignes());
            departC = r.nextInt(jeu.plateau().colonnes());
            departTour = jeu.plateau().tour(departL,departC);
        }
        return departTour;
    }
    @Override
    boolean tempsEcoule() {
        Tour departTour = selectionAleatoire();
        Sequence voisinsDepart = jeu.plateau.voisins(departTour.ligne(), departTour.colonne());
        int position = r.nextInt(8); // choix parmi les 8 voisins
        int i = 0;
        Iterateur it = voisinsDepart.iterateur();
        while (it.aProchain()){
                if (i == position){
                    Couple c = (Couple) it.prochain();
                    int destL = (int) c.premier();
                    int destC = (int) c.second();
                    Tour destTour = jeu.plateau().tour(destL, destC);
                    if (destTour.estDeplacable(departTour)){
                        jeu.plateau().Jouer(departTour, destTour);
                        return true;
                    }
                }else {
                    i++;
                }

        }
        return false;
    }
}
