
package Controleur;

import Global.Configuration;
import Moteur.*;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;

import java.util.Random;


public class JoueurIAMinMax extends JoueurIA {
    Random r;
    Sequence<Coup> coupJouables;
    //PlateauDeJeu plateauDeJeu;
    //Jeu clone;
    int nbCoups = 0; // Coups à l'avance en debut de partie

    JoueurIAMinMax(int n, Jeu jj) {
        super(n, jj);
        r = new Random();
        coupJouables = Configuration.instance().nouvelleSequence();
    }


    public Tour selectionTourDansZone(PlateauDeJeu p, int zone) {
        int infBorneL = 0, supBorneL = 0, infBorneC = 0, supBorneC = 0;
        switch (zone) {
            case 0 -> {
                infBorneL = 0;
                supBorneL = 5;
                infBorneC = 0;
                supBorneC = 5;
            }
            case 1 -> {
                infBorneL = 0;
                supBorneL = 5;
                infBorneC = 5;
                supBorneC = 9;
            }
            case 2 -> {
                infBorneL = 5;
                supBorneL = 9;
                infBorneC = 0;
                supBorneC = 5;
            }
            case 3 -> {
                infBorneL = 5;
                supBorneL = 9;
                infBorneC = 5;
                supBorneC = 9;
            }
            default -> {
                Configuration.instance().logger().severe("Zone inconnue");
                System.exit(1);
            }
        }
        Tour tour = selectionAleatoireAvecBorne(p, infBorneL, infBorneC, supBorneL, supBorneC);
        return tour;

    }

    public Tour selectionAleatoireAvecBorne(PlateauDeJeu p, int biL, int biC, int bsL, int bsC) {
        /*int departL = biL + r.nextInt(bsL - biL);
        int departC = biC + r.nextInt(bsC - biC);
        Tour tour = p.tour(departL, departC);*/
        Sequence mesTours = Configuration.instance().nouvelleSequence();
        int nbMesTours = 0;
        Tour resultat = null;
        for (int i = biL; i < bsL; i++) {
            for (int j = biC; j < bsC; j++) {
                Tour tour = p.tour(i, j);
                if (tour.estJouable() && !p.pasDeplacable(tour) && tour.sommetTour() == num) {
                    mesTours.insereQueue(tour);
                    nbMesTours++;
                }
            }
        }
        Iterateur it = mesTours.iterateur();
        int positionTourAjouer = r.nextInt(nbMesTours);
        int k = 0;
        while (it.aProchain()) {
            Tour tourAJouer = (Tour) it.prochain();
            if (k == positionTourAjouer) {
                resultat = tourAJouer;
            } else {
                k++;
            }
        }

        return resultat;
    }

    public void couvrirAdversaire(Jeu copy) {
        int zone = r.nextInt(4); // on divise le plateau en 4 zones
        Tour tour = selectionTourDansZone(copy.plateau(), zone);
        Sequence<Tour> voisins = copy.plateau().voisins(tour.ligne(), tour.colonne());
        Iterateur<Tour> vJouables = copy.plateau().voisinsJouables(voisins).iterateur();
        Sequence<Tour> toursAdverses = Configuration.instance().nouvelleSequence();
        while (vJouables.aProchain()) {
            Tour tourVoisines = vJouables.prochain();
            if (tourVoisines.sommetTour() != num) {
                if (tour.estDeplacable(tourVoisines)) {
                    toursAdverses.insereQueue(tourVoisines);
                }
            }
        }
        Tour tourAJouer = toursAdverses.extraitTete();
        //p.Jouer(tour, tourAJouer);
        copy.jouer(tour, tourAJouer);
        copy.plateau().deselection_ia();
        copy.plateau().selection_ia(tour, tourAJouer);
    }
   /* private int evaluation(PlateauDeJeu p) {
        int resultat = 0;
        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                Tour t = p.tour(i, j);
                if (!t.estVide() && !t.estInnocupable() && t.nbPion() > 1) {
                    if (num == t.sommetTour()) {
                        System.out.println("avant resultat = " + resultat);
                        resultat++;
                        System.out.println("apres resultat = " + resultat);
                    }
                }
            }
        }
        return resultat;
    }*/

    @Override
    boolean tempsEcoule() {
        //double[][] scores = new double[9][9];
        //double max = 0;
        //int ldep = 0, cdep = 0;
        Coup meilleurCoup = null;
        double meilleurScore = Double.NEGATIVE_INFINITY;
        int horizon = 0;

        //Couple<Integer, Integer> v;
        //Couple<Double, Tour> resultat = null;
        //Tour tour = null;
        if (nbCoups < 2) {
            couvrirAdversaire(jeu);
            nbCoups++;
        } else {
            System.out.println("avant min max");
            //plateauDeJeu = jeu.plateau();

            for (int i = 0; i < jeu.plateau().lignes(); i++) {
                for (int j = 0; j < jeu.plateau().colonnes(); j++) {
                    Tour selectionnee = jeu.plateau().tour(i, j);
                    if (selectionnee.estJouable() && !jeu.plateau().pasDeplacable(selectionnee)) {
                        Sequence<Tour> voisines = jeu.plateau().voisins(selectionnee.ligne(), selectionnee.colonne());
                        Iterateur<Tour> vJouables = jeu.plateau().voisinsJouables(voisines).iterateur();
                        while (vJouables.aProchain()) {
                            Tour dest = vJouables.prochain();
                            if (dest.estJouable() && selectionnee.estDeplacable(dest)) {
                                jeu.jouer(selectionnee, dest);
                                meilleurScore = Math.max(meilleurScore, miniMax(jeu, horizon, true));
                                jeu.annuler();
                                meilleurCoup = new Coup(selectionnee, dest);
                            }

                        }

                    }

                }

            }

            System.out.println("apres min max");
            meilleurCoup.afficheCoup();
            System.out.println("meilleur score = " + meilleurScore);
            jeu.jouer(meilleurCoup.src(), meilleurCoup.dest());
            jeu.plateau().deselection_ia();
            jeu.plateau().selection_ia(meilleurCoup.src(), meilleurCoup.dest());
            Tour tour1 = jeu.plateau().tour(3, 4);
            Tour tour2 = jeu.plateau().tour(2, 4);
            jeu.jouer(tour1, tour2);
            //double valeur = miniMax(jeu,2, true);

            //afficheCoup(coup);
            //jeu.plateau().Jouer(coup.src(), coup.dest());
            //jeu.miseAJour();
            //clone = jeu.clone();
        }
        return true;
}

    public Sequence<Sequence<CoupIA>> elaborerCoup(boolean joueurMax) {
        PlateauDeJeu p = jeu.plateau().clone();
        Sequence<Sequence<CoupIA>> sequenceCoups = Configuration.instance().nouvelleSequence();
        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                if (p.tour(i, j).estJouable()) {
                    Sequence voisins = p.voisins(i, j);
                    Iterateur voisinsJouables = p.voisinsJouables(voisins).iterateur();
                    Sequence<CoupIA> coupsPossibles = Configuration.instance().nouvelleSequence();
                    Tour depart = p.tour(i, j);
                    while (voisinsJouables.aProchain()) {
                        Tour dest = (Tour) voisinsJouables.prochain();
                        if (depart.estDeplacable(dest)) {
                            int valeur = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                            CoupIA coup = new CoupIA(depart, dest);
                            coup.fixerValeur(valeur);
                            coupsPossibles.insereQueue(coup);
                        }
                    }
                    sequenceCoups.insereQueue(coupsPossibles);
                }
            }
        }
        return sequenceCoups;
    }

    public PlateauDeJeu jouerCoup(CoupIA coup, PlateauDeJeu p) {
        PlateauDeJeu plateau = p.clone();
        plateau.jouer(coup.src(), coup.dest());
        //coup.fixerValeur(evaluation());
        //plateau.annuler();
        return plateau;
    }

    /*private double evaluation(Jeu jj) {
        double resultat = 1.0;
        for (int i=0; i<jj.plateau().lignes(); i++){
            for (int j=0; j<jj.plateau().colonnes(); j++){
                Tour tour = jj.plateau().tour(i,j);
                if (tour.estJouable()){
                    if (tour.nbPion() == 1){
                        resultat = 2.0;
                    }else if (tour.nbPion() == 2){
                        resultat = 0.25;
                    }else if (tour.nbPion() == 3){
                        resultat = 0.50;
                    }else if (tour.nbPion() == 4){
                        resultat = 0.75;
                    }else if (tour.nbPion() == 5){
                        resultat = 1.0;
                    }
                }
            }
        }
        return resultat;
    }*/

    private double evaluation(Jeu copy) {
        double diff = 0.0;
        if (num == 0) {
            diff = copy.score(num) - copy.score((num + 1) % 2);
        } else {
            diff = copy.score((num + 1) % 2) - copy.score(num);
        }
        return diff;
    }

    double miniMax(Jeu copy, int horizon, boolean joueurMax) {
        System.out.println("joueurMax =  " + (joueurMax ? "max" : "min") + " / horizon=" + horizon);
        if (horizon == 2 || copy.estTermine()) {
            return evaluation(copy);
        } else {
            int resultat = 0;

            double maxEval = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (int i = 0; i < copy.plateau().lignes(); i++) {
                System.out.println("debut ligne i = " + i);
                for (int j = 0; j < copy.plateau().colonnes(); j++) {
                    System.out.println("debut colonne j = " + j);
                    Tour depart = copy.plateau().tour(i, j);
                    if (depart.estJouable() && !jeu.plateau().pasDeplacable(depart)) {
                        Sequence v = copy.plateau().voisins(depart.ligne(), depart.colonne());
                        Iterateur jouables = jeu.plateau().voisinsJouables(v).iterateur();
                        while (jouables.aProchain()) {
                            Tour dest = (Tour) jouables.prochain();
                            if (depart.estDeplacable(dest)) {
                                copy.plateau().jouer(depart, dest);
                                double valeurMinMax = miniMax(copy, horizon + 1, !joueurMax);
                                System.out.println("annulation = ");
                                depart.afficher();
                                dest.afficher();
                                copy.annuler();
                                if (joueurMax) {
                                    maxEval = (maxEval > valeurMinMax) ? maxEval : valeurMinMax;
                                } else {
                                    maxEval = (maxEval < valeurMinMax) ? maxEval : valeurMinMax;
                                }
                            }
                        }
                    }
                }

            }

            return maxEval;
        }
    }

}
