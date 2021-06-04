
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
    int nbCoups = 0; // Coups à l'avance en debut de partie

    JoueurIAMinMax(int n, Jeu jj) {
        super(n, jj);
        r = new Random();
        coupJouables = Configuration.instance().nouvelleSequence();
    }


    private int evaluation() {
        if (num == 0) {
            return jeu.plateau().scoreJ2();
        } else {
            return jeu.plateau().scoreJ1();
        }
        //return r.nextInt(10);
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
        for (int i=biL; i < bsL; i++){
            for (int j=biC; j < bsC; j++){
                Tour tour = p.tour(i, j);
                if (!p.pasDeplacable(tour) && tour.sommetTour() == num){
                    mesTours.insereQueue(tour);
                    nbMesTours++;
                }
            }
        }
        Iterateur it = mesTours.iterateur();
        int positionTourAjouer = r.nextInt(nbMesTours);
        int k = 0;
        while (it.aProchain()){
            Tour tourAJouer = (Tour) it.prochain();
            if (k == positionTourAjouer){
                resultat = tourAJouer;
            }else {
                k++;
            }
        }

        return resultat;
    }

    public void couvrirAdversaire(PlateauDeJeu p) {
        int sommet = (num == 0) ? 0 : 1;
        int zone = r.nextInt(4); // on divise le plateau en 4 zones
        Tour tour = selectionTourDansZone(p, zone);
        Sequence voisins = jeu.plateau().voisins(tour.ligne(), tour.colonne());
        Iterateur vJouables = jeu.plateau().voisinsJouables(voisins).iterateur();
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
        p.Jouer(tour, tourAJouer);
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

        Couple<Integer, Integer> v;
        Couple<Double, Tour> resultat = null;
        Tour tour = null;

        if (nbCoups < 6) {
            couvrirAdversaire(jeu.plateau());
            nbCoups++;
        } else {
            System.out.println("avant min max");
            CoupIA coup = miniMax(jeu.plateau(), null, 2, true);
            System.out.println("apres min max");
            afficheCoup(coup);
            jeu.plateau().Jouer(coup.src(), coup.dest());
            jeu.miseAJour();
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
        plateau.Jouer(coup.src(), coup.dest());
        //coup.fixerValeur(evaluation());
        //plateau.annuler();
        return plateau;
    }

    double miniMax(PlateauDeJeu p ,Tour t, int horizon, boolean joueurMax) {
			// copy de plateau pour les deplacement        
        PlateauDeJeu pcopy = p.clone();

        int vl, vc;
        double score;
        if (horizon == 0 || jeu.estTermine()) {
            assert (c != null);
            CoupIA coup = new CoupIA(c.src(), c.dest());
            //coup.fixerValeur(evaluation());
            //pcopy = jouerCoup(coup, pcopy);
            coup.fixerValeur(evaluation());
            System.out.println("deplacement " + (joueurMax ? "max" : "min") + " / horizon=" + horizon);
            //pcopy.annuler();

            return coup;
        } else {
            int resultat = 0;
            //voisins = jeu.plateau().voisins(t.ligne(), t.colonne());
            //if (joueurMax) {
            //Sequence voisins = pcopy.voisins(t.ligne(), t.colonne());
            //Sequence voisinsJouables = pcopy.voisinsJouables(voisins);
            //Iterateur it = voisinsJouables.iterateur();
            Sequence<Sequence<CoupIA>> tousLesCoups = elaborerCoup(joueurMax);
            Iterateur it = tousLesCoups.iterateur();
            int maxEval = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
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
                System.out.println("min value = " + Integer.MIN_VALUE);

                //pcopy = jouerCoup(coupAJouer, pcopy);

                //result = new CoupIA(t, tourVoisine, maxEval);
                System.out.println("deplacement " + (joueurMax ? "max" : "min") + " / horizon=" + horizon);
                /*CoupIA coupMinMax = miniMax(pcopy, coupAJouer, horizon - 1, !joueurMax);
                if (joueurMax) {
                    coupAJouer = (coupAJouer.valeur() > coupMinMax.valeur()) ? coupAJouer : coupMinMax;
                } else {
                    coupAJouer = (coupAJouer.valeur() < coupMinMax.valeur()) ? coupAJouer : coupMinMax;
                }*/
            }
            return coupAJouer;
        }


    }

    public void afficheCoup(CoupIA c) {
        System.out.println("Coup: (" + c.src().ligne() + "," + c.src().colonne() + ") -> ("
                + c.dest().ligne() + "," + c.dest().colonne() + ") valeur = " + c.valeur());
    }

}
