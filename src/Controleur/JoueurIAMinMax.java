
package Controleur;

import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Sequence;

import java.util.Random;


public class JoueurIAMinMax extends JoueurIA {

    JoueurIAMinMax(int n, Jeu jj) {
        super(n, jj);
    }

    @Override
    boolean tempsEcoule() {
        double[][] scores = new double[9][9];
        double max = 0;
        int ldep = 0, cdep = 0;
        Couple<Double, Couple<Integer, Integer>> resultat = null;
        Couple<Integer, Integer> v = null;

        for (int i = 0; i < jeu.plateau().lignes(); i++) {
            for (int j = 0; j < jeu.plateau().colonnes(); j++) {
                if (jeu.plateau().tour(i, j).estJouable() || !jeu.plateau().estIsole(i, j)) {
                    resultat = miniMax(jeu.plateau().tour(i, j), 3, true);
                    if (max <= resultat.premier()) {
                        max = resultat.premier();
                        v = resultat.second();
                        i = ldep;
                        j = cdep;
                    }
                }

            }
        }
        jeu.plateau.Jouer(jeu.plateau().tour(ldep, cdep), jeu.plateau().tour(v.premier(), v.second()));
        jeu.miseAJour();
        return true;
    }


    Couple<Double, Couple<Integer, Integer>> miniMax(Tour t, int horizon, boolean joueurMax) {
        Couple<Double, Couple<Integer, Integer>> resultat = null;
        int vl, vc;
        double score;
        Sequence<Couple<Integer, Integer>> voisins;
        if (horizon == 0 || jeu.estTermine()) {
            //calculer score
            if (num == 0) {
                score = jeu.plateau().scoreJ1();
                resultat.setPremier(score);
                return resultat;
            } else {

                score = jeu.plateau().scoreJ2();
                resultat.setPremier(score);
                return resultat;
            }
        } else {
            double maxEval;
            voisins = jeu.plateau().voisins(t.ligne(), t.colonne());
            if (joueurMax) {
                maxEval = Double.NEGATIVE_INFINITY;
                while (!voisins.estVide()) {
                    Couple v = voisins.extraitTete();
                    vl = (int) v.premier();
                    vc = (int) v.second();
                    resultat = miniMax(jeu.plateau().tour(vl, vc), horizon - 1, false);
                    double s = resultat.premier();
                    if (s > maxEval) {
                        resultat.setSecond(v);

                    }
                    return resultat;
                    //maxEval = Math.max(maxEval, s);
                }

            } else {
                double minEval = Double.POSITIVE_INFINITY;
                voisins = jeu.plateau.voisins(t.ligne(), t.colonne());
                while (!voisins.estVide()) {
                    Couple v = voisins.extraitTete();
                    vl = (int) v.premier();
                    vc = (int) v.second();
                    resultat = miniMax(jeu.plateau().tour(vl, vc), horizon - 1, false);
                    double s = resultat.premier();
                    if (s < minEval) {
                        resultat.setSecond(v);
                    }
                    //minEval = Math.min(minEval, s);
                }
                return resultat;
            }
        }
        return resultat;
    }


}
