
package Controleur;

import Global.Configuration;
import Moteur.Coup;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;

import java.util.Random;


public class JoueurIAMinMax extends JoueurIA {
    Random r;
    Sequence<Coup> coupJouables;

    JoueurIAMinMax(int n, Jeu jj) {
        super(n, jj);
        r = new Random();
        coupJouables = Configuration.instance().nouvelleSequence();
    }

    int minMax(PlateauDeJeu p) {
        return tourMax(p);
    }

    private int tourMax(PlateauDeJeu p) {
        if (jeu.estTermine()) {
            evaluation(p);
        }
        int valeur = Integer.MIN_VALUE;
        Coup coup = null;
        Tour tourDepart = selectionAleatoire();
        if (!p.pasDeplacable(tourDepart)) {
            Sequence voisins = p.voisins(tourDepart.ligne(), tourDepart.colonne());
            Iterateur it = voisins.iterateur();
            while (it.aProchain()) {
                Couple<Integer, Integer> couple = (Couple<Integer, Integer>) it.prochain();
                int destL = couple.premier();
                int destC = couple.second();
                Tour tourDest = p.tour(destL, destC);
                //Sequence<Coup> coups;
                if (tourDest.estDeplacable(tourDepart) && tourDest.estJouable()) {
                    //coups = Configuration.instance().nouvelleSequence();
                    //coups.insereTete(new Coup(tourDepart, tourDest)); // ensembles de coups jouables
                    p = jouerCoup(0, new Coup(tourDepart, tourDest), p);
                    if (tourMin(p) > valeur) {
                        coup = new Coup(tourDepart, tourDest);
                        valeur = tourMin(p);
                        coupJouables.insereTete(coup);
                    }
                }
            }
        }
        return valeur;
    }

    private int tourMin(PlateauDeJeu p) {
        int resultat = -2;
        int valeur = Integer.MAX_VALUE;
        if (jeu.estTermine()) {
            resultat = evaluation(p);
        }
        Tour tourDepart = selectionAleatoire();
        Sequence voisins = p.voisins(tourDepart.ligne(), tourDepart.colonne());
        Iterateur it = voisins.iterateur();
        while (it.aProchain()) {
            Couple<Integer, Integer> couple = (Couple<Integer, Integer>) it.prochain();
            int destL = couple.premier();
            int destC = couple.second();
            Tour tourDest = p.tour(destL, destC);
            //Sequence<Coup> coups;
            if (tourDest.estDeplacable(tourDepart) && tourDest.estJouable()) {
                //coups = Configuration.instance().nouvelleSequence();
                //coups.insereTete(new Coup(tourDepart, tourDest)); // ensembles de coups jouables
                p = jouerCoup(0, new Coup(tourDepart, tourDest), p);
                if (tourMin(p) > valeur) {
                    Coup coup = new Coup(tourDepart, tourDest);
                    valeur = tourMin(p);
                    coupJouables.insereTete(coup);
                }

            }
        }
        return valeur;
    }

    private PlateauDeJeu jouerCoup(int num, Coup coup, PlateauDeJeu plateau) {
        PlateauDeJeu p = plateau.clone();
        p.Jouer(coup.src(), coup.dest());
        plateau = p;
        return plateau;

    }

    private int evaluation(PlateauDeJeu p) {
        return jeu.plateau().scoreJ1() - jeu.plateau().scoreJ2();
    }

    @Override
    boolean tempsEcoule() {
        double[][] scores = new double[9][9];
        double max = 0;
        int ldep = 0, cdep = 0;
        double resultat;
        Couple<Integer, Integer> v ;

        for (int i = 0; i < jeu.plateau().lignes(); i++) {
            for (int j = 0; j < jeu.plateau().colonnes(); j++) {
                if (jeu.plateau().tour(i, j).estJouable() || !jeu.plateau().estIsole(i, j)) {
                    resultat = miniMax(jeu.plateau(),jeu.plateau().tour(i, j), 2, true);
                    if (max <= resultat) {
                        max = resultat;
                        ldep = i;
                        cdep = j;
                    }
                }

            }
        }
        //Configuration.instance().logger().info("i et  : " + ldep + ", " + cdep + ") a été selectionnée");
        Sequence<Couple<Integer, Integer>> voisins;
        voisins = jeu.plateau().voisins(ldep,cdep);
        v = voisins.extraitTete();
        int vl = (int) v.premier();
        int vc = (int) v.second();
        jeu.plateau().Jouer(jeu.plateau().tour(ldep,cdep), jeu.plateau().tour(vl,vc));
        jeu.miseAJour();
        return true;
    }


    double miniMax(PlateauDeJeu p ,Tour t, int horizon, boolean joueurMax) {
			// copy de plateau pour les deplacement        
        PlateauDeJeu pcopy = p.clone();
        int vl, vc;
        double score;
        Sequence<Couple<Integer, Integer>> voisins;
        if (horizon == 0 || jeu.estTermine()) {
            //calculer score
            if (num == 0) {
                return  jeu.plateau().scoreJ1();
            } else {
                return  jeu.plateau().scoreJ2();
            }
        } else {
            double maxEval;
            //voisins = jeu.plateau().voisins(t.ligne(), t.colonne());
            voisins = pcopy.voisins(t.ligne(), t.colonne());
            if (joueurMax) {
                maxEval = Double.NEGATIVE_INFINITY;
                while (!voisins.estVide()) {
                    Couple v = voisins.extraitTete();
                    vl = (int) v.premier();
                    vc = (int) v.second();
                    pcopy.Jouer(t, pcopy.tour(vl,vc));
                    Configuration.instance().logger().info("deplacement " + vl +"et" + vc);
                    double s =  miniMax(pcopy,pcopy.tour(vl, vc), horizon - 1, false);
                    maxEval = Math.max(maxEval, s);
                }
                return maxEval;

            } else {
                double minEval = Double.POSITIVE_INFINITY;
                //voisins = jeu.plateau.voisins(t.ligne(), t.colonne());
                voisins = pcopy.voisins(t.ligne(), t.colonne());
                while (!voisins.estVide()) {
                    Couple v = voisins.extraitTete();
                    vl = (int) v.premier();
                    vc = (int) v.second();
                    pcopy.Jouer(t, pcopy.tour(vl,vc));
                    Configuration.instance().logger().info("deplacement " + vl +"et" + vc);
                    double s = miniMax(pcopy,pcopy.tour(vl, vc), horizon - 1, true);
                    minEval = Math.min(minEval, s);
                }
                return minEval;
            }
        }


}


}
