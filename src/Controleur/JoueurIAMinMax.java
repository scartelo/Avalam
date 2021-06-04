
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
        double resultat;
        Couple<Integer, Integer> v ;

        for (int i = 0; i < jeu.plateau().lignes(); i++) {
            for (int j = 0; j < jeu.plateau().colonnes(); j++) {
                if (jeu.plateau().tour(i, j).estJouable()) {
                    System.out.println(" i = " + i + ", j = " + j);
                    //resultat = miniMax(jeu.plateau(),jeu.plateau().tour(i, j), 2, true);
                    System.out.println("score  :" + resultat);
                    if (max <= resultat.premier()) {
                        max = resultat.premier();
                        ldep = i;
                        cdep = j;
                    }
                }

            }
        }
        //Configuration.instance().logger().info("i et  : " + ldep + ", " + cdep + ") a été selectionnée");
        //Sequence<Couple<Integer, Integer>> voisins;
        //voisins = jeu.plateau().voisins(ldep,cdep);
        //Sequence voisinsJouables = jeu.plateau().voisinsJouables(voisins);
        //Tour tourVoisine = (Tour) voisinsJouables.extraitTete();
        //int vl = (int) v.premier();
        //int vc = (int) v.second();
        jeu.plateau().Jouer(jeu.plateau().tour(ldep, cdep), tour);
        Configuration.instance().logger().info("i et  : " + ldep + ", " + cdep + ") a été selectionnée");

        Configuration.instance().logger().info("idest et  : " + tour.ligne() + ", " + tour.colonne() + ") a été selectionnée");
         */
        System.out.println("avant min max");
        CoupIA coup = miniMax(jeu.plateau(), null, 4, true);
        System.out.println("apres min max");
        afficheCoup(coup);
        jeu.plateau().Jouer(coup.src(), coup.dest());
        jeu.miseAJour();
        return true;
    }

    public Sequence<Sequence<CoupIA>> elaborerCoup(boolean joueurMax) {
        PlateauDeJeu p = jeu.plateau().clone();
        Sequence<Sequence<CoupIA>> sequenceCoups = Configuration.instance().nouvelleSequence();
        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                if (p.tour(i,j).estJouable()) {
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
            System.out.println("deplacement "+(joueurMax?"max":"min")+" / horizon="+horizon);
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
                while (coupCourantIt.aProchain()){
                    CoupIA coup = (CoupIA) coupCourantIt.prochain();
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
                System.out.println("deplacement "+(joueurMax?"max":"min")+" / horizon="+horizon);
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
