
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
    PlateauDeJeu plateauDeJeu;
    Jeu clone;
    int nbCoups = 2; // Coups à l'avance en debut de partie

    JoueurIAMinMax(int n, Jeu jj) {
        super(n, jj);
        r = new Random();
        coupJouables = Configuration.instance().nouvelleSequence();
    }


    public Tour selectionTourDansZone(PlateauDeJeu p, int zone) {
        int infBorneL = 0, supBorneL = 0, infBorneC = 0, supBorneC = 0;
        switch (zone) {
            case 0:
                infBorneL = 0;
                supBorneL = 5;
                infBorneC = 0;
                supBorneC = 5;
                break;
            case 1:
                infBorneL = 0;
                supBorneL = 5;
                infBorneC = 5;
                supBorneC = 9;
                break;
            case 2:
                infBorneL = 5;
                supBorneL = 9;
                infBorneC = 0;
                supBorneC = 5;
                break;
            case 3:
                infBorneL = 5;
                supBorneL = 9;
                infBorneC = 5;
                supBorneC = 9;
                break;
            default:
                Configuration.instance().logger().severe("Zone inconnue");
                System.exit(1);

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
                if (!p.pasDeplacable(tour) && tour.sommetTour() == num) {
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
        int sommet = (num == 0) ? 0 : 1;
        int zone = r.nextInt(4); // on divise le plateau en 4 zones
        Tour tour = selectionTourDansZone(copy.plateau(), zone);
        Sequence voisins = copy.plateau().voisins(tour.ligne(), tour.colonne());
        Iterateur vJouables = copy.plateau().voisinsJouables(voisins).iterateur();
        Sequence toursAdverses = Configuration.instance().nouvelleSequence();
        while (vJouables.aProchain()) {
            Tour tourVoisines = (Tour) vJouables.prochain();
            if (tourVoisines.sommetTour() != num) {
                if (tour.estDeplacable(tourVoisines)) {
                    toursAdverses.insereQueue(tourVoisines);
                }
            }
        }
        Tour tourAJouer = (Tour) toursAdverses.extraitTete();
        //p.Jouer(tour, tourAJouer);
        copy.jouer(tour, tourAJouer);
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
        double[][] scores = new double[9][9];
        double max = 0;
        int ldep = 0, cdep = 0;
        CoupIA meilleurCoup = null;
        double meilleurScore = Double.NEGATIVE_INFINITY;
        int horizon = 2;
        clone = jeu.clone();

        Couple<Integer, Integer> v;
        Couple<Double, Tour> resultat = null;
        Tour tour = null;
        if (nbCoups < 2) {
            couvrirAdversaire(jeu);
            nbCoups++;
        } else {
            System.out.println("avant min max");
            plateauDeJeu = jeu.plateau();

            for (int i=0; i<clone.plateau().lignes(); i++){
                for (int j=0; j<clone.plateau().colonnes(); j++){
                    Tour selectionnee = clone.plateau().tour(i,j);
                    if (selectionnee.estJouable() && !clone.plateau().estIsole(selectionnee.ligne(), selectionnee.colonne())){
                        for (int x=0; x<clone.plateau().lignes(); x++){
                            for (int y=0; y<clone.plateau().colonnes(); y++){
                                Tour dest = clone.plateau().tour(x,y);
                                if (dest.estJouable() && selectionnee.estDeplacable(dest)){
                                    meilleurScore = Math.max(meilleurScore, miniMax(clone, horizon, true)) ;

                                    meilleurCoup = new CoupIA(selectionnee, dest);
                                }

                            }
                        }

                    }

                }
            }
            System.out.println("apres min max");
            afficheCoup(meilleurCoup);
            System.out.println("meilleur score = " + meilleurScore);
            clone.jouer(meilleurCoup.src(), meilleurCoup.dest());

            //double valeur = miniMax(jeu,2, true);

            //afficheCoup(coup);
            //jeu.plateau().Jouer(coup.src(), coup.dest());
        }
        //jeu.miseAJour();
        clone = jeu.clone();
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
        plateau.Jouer(coup.src(), coup.dest());
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

    private double evaluation(PlateauDeJeu p){
        double diff = 0.0;
        if (num == 0){
            diff = p.scoreJ1() - p.scoreJ2();
        }else{
            diff = p.scoreJ2() - p.scoreJ1();
        }
        return diff;
    }

    double miniMax(PlateauDeJeu p ,Tour t, int horizon, boolean joueurMax) {
			// copy de plateau pour les deplacement        
        PlateauDeJeu pcopy = p.clone();

        int vl, vc;
        double score;
        if (horizon == 0 || copy.estTermine()) {
            /*assert (c != null);
            CoupIA coup = new CoupIA(c.src(), c.dest());
            //coup.fixerValeur(evaluation());
            //pcopy = jouerCoup(coup, pcopy);
            coup.fixerValeur(evaluation());
            System.out.println("deplacement " + (joueurMax ? "max" : "min") + " / horizon=" + horizon);
            //pcopy.annuler();
               */
            return evaluation(copy.plateau());
        } else {
            int resultat = 0;
            //int maxEval = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            /*Sequence<Sequence<CoupIA>> tousLesCoups = elaborerCoup(joueurMax);
            Iterateur it = tousLesCoups.iterateur();

            CoupIA coupAJouer = new CoupIA(null, null);
            coupAJouer.fixerValeur(maxEval);

            while (it.aProchain()) {
                //Tour tourVoisine = (Tour) it.prochain();
                //pcopy.Jouer(t, tourVoisine);
                Sequence<CoupIA> coupCourant = (Sequence<CoupIA>) it.prochain();
                Iterateur coupCourantIt = coupCourant.iterateur();
                while (coupCourantIt.aProchain()) {
                    CoupIA coup = (CoupIA) coupCourantIt.prochain();
                    System.out.print("coup dans while = ");
                    afficheCoup(coup);
                    CoupIA coupMinMax = miniMax(pcopy, coup, horizon - 1, !joueurMax);
                    if (joueurMax) {
                        coupAJouer = (coupAJouer.valeur() > coupMinMax.valeur()) ? coupAJouer : coupMinMax;
                    } else {
                        coupAJouer = (coupAJouer.valeur() < coupMinMax.valeur()) ? coupAJouer : coupMinMax;
                    }
                }
                //coupAJouer = (CoupIA) it.prochain();
                //coupAJouer.fixerValeur(maxEval);
                afficheCoup(coupAJouer);
                System.out.println("min value = " + Integer.MIN_VALUE);*/

            //pcopy = jouerCoup(coupAJouer, pcopy);

            //result = new CoupIA(t, tourVoisine, maxEval);
            double maxEval = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            for (int i = 0; i < copy.plateau().lignes(); i++) {
                for (int j = 0; j < copy.plateau().colonnes(); j++) {
                    Tour depart = copy.plateau().tour(i, j);
                    if ( depart.estJouable() && !copy.plateau().estIsole(depart.ligne(), depart.colonne())) {
                        Sequence v = copy.plateau().voisins(depart.ligne(), depart.colonne());
                        Iterateur jouables = v.iterateur();
                        while (jouables.aProchain()) {
                            /*Couple<Integer, Integer> couple = (Couple<Integer, Integer>) jouables.prochain();
                            int l = couple.premier();
                            int c = couple.second();
                            Tour dest = copy.plateau().tour(l,c);*/
                            Tour dest = (Tour) jouables.prochain();
                            if (depart.estDeplacable(dest)) {
                                copy.plateau().Jouer(depart, dest);
                                double valeurMinMax = miniMax(copy, horizon - 1, !joueurMax);
                                //copy.plateau().annuler();
                                if (joueurMax) {
                                    maxEval = (maxEval > valeurMinMax) ? maxEval : valeurMinMax;
                                } else {
                                    maxEval = (maxEval < valeurMinMax) ? maxEval : valeurMinMax;
                                }
                            }
                        }
                    }
                    System.out.println("deplacement " + (joueurMax ? "max" : "min") + " / horizon=" + horizon);
                /*CoupIA coupMinMax = miniMax(pcopy, coupAJouer, horizon - 1, !joueurMax);
                if (joueurMax) {
                    coupAJouer = (coupAJouer.valeur() > coupMinMax.valeur()) ? coupAJouer : coupMinMax;
                } else {
                    coupAJouer = (coupAJouer.valeur() < coupMinMax.valeur()) ? coupAJouer : coupMinMax;
                }*/
                }

            }

            return maxEval;
        }
    }

        public void afficheCoup(CoupIA c){
            System.out.println("Coup: (" + c.src().ligne() + "," + c.src().colonne() + ") -> ("
                    + c.dest().ligne() + "," + c.dest().colonne() + ") valeur = " + c.valeur());
        }

    }
