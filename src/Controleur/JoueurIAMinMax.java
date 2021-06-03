
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
                    p = jouerCoup(0, new Coup(0, tourDepart, tourDest), p);
                    if (tourMin(p) > valeur) {
                        coup = new Coup(0, tourDepart, tourDest);
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
                p = jouerCoup(0, new Coup(0, tourDepart, tourDest), p);
                if (tourMin(p) > valeur) {
                    Coup coup = new Coup(0, tourDepart, tourDest);
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
        int resultat = 0;
        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                Tour t = p.tour(i, j);
                if (!t.estVide() && !t.estInnocupable() && t.nbPion() > 1) {
                    if (num == t.sommetTour()) {
                        resultat++;
                    }
                }
            }
        }
        return resultat;
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
        CoupIA coup = miniMax(jeu.plateau(), null, 2, true);
        System.out.println("apres min max");
        jeu.plateau().Jouer(coup.src(), coup.dest());
        jeu.miseAJour();
        return true;
    }

    public Sequence<CoupIA> elaborerCoup() {
        PlateauDeJeu p = jeu.plateau().clone();
        Sequence<CoupIA> coups = Configuration.instance().nouvelleSequence();
        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                if (jeu.plateau().tour(i, j).estJouable()) {
                    Sequence voisins = p.voisins(i, j);
                    Iterateur voisinsJouables = p.voisinsJouables(voisins).iterateur();
                    Tour depart = p.tour(i, j);
                    while (voisinsJouables.aProchain()) {
                        Tour dest = (Tour) voisinsJouables.prochain();
                        CoupIA coup = new CoupIA(depart, dest);
                        coups.insereTete(coup);
                    }
                }
            }
        }
        return coups;
    }

    public PlateauDeJeu jouerCoup(CoupIA coup) {
        PlateauDeJeu plateau = jeu.plateau().clone();
        plateau.Jouer(coup.src(), coup.dest());
        coup.fixerValeur(evaluation(plateau));
        jeu.annuler();
        return plateau;
    }

    double miniMax(PlateauDeJeu p ,Tour t, int horizon, boolean joueurMax) {
			// copy de plateau pour les deplacement        
        PlateauDeJeu pcopy = p.clone();
        int vl, vc;
        double score;
        if (horizon == 0 || jeu.estTermine()) {
            assert(c!=null);
            CoupIA coup = new CoupIA(c.src(), c.dest());
            coup.fixerValeur(evaluation(p));
            return coup;
        } else {
            int resultat = 0;
            //voisins = jeu.plateau().voisins(t.ligne(), t.colonne());
            //if (joueurMax) {
                //Sequence voisins = pcopy.voisins(t.ligne(), t.colonne());
                //Sequence voisinsJouables = pcopy.voisinsJouables(voisins);
                //Iterateur it = voisinsJouables.iterateur();
                Sequence<CoupIA> coups = elaborerCoup();
                Iterateur it = coups.iterateur();
                int maxEval = joueurMax?Integer.MIN_VALUE:Integer.MAX_VALUE;
                CoupIA coupAJouer = null;

                while (it.aProchain()) {
                    //Tour tourVoisine = (Tour) it.prochain();
                    //pcopy.Jouer(t, tourVoisine);
                    //CoupIA coup = (CoupIA) it.prochain();
                    coupAJouer = (CoupIA) it.prochain();
                    afficheCoup(coupAJouer);
                    pcopy = jouerCoup(coupAJouer);
                    //result = new CoupIA(t, tourVoisine, maxEval);
                    //Configuration.instance().logger().info("deplacement "+(joueurMax?"max":"min")+" / horizon="+horizon);
                    CoupIA coupMinMax = miniMax(pcopy, coupAJouer, horizon - 1, !joueurMax);
                    if (joueurMax) {
                        coupAJouer = (coupAJouer.valeur() > coupMinMax.valeur()) ? coupAJouer : coupMinMax;
                    } else {
                        coupAJouer = (coupAJouer.valeur() < coupMinMax.valeur()) ? coupAJouer:coupMinMax;
                    }
                }
                return coupAJouer;
        }


    }

    public void afficheCoup(CoupIA c){
        System.out.println("Coup: (" + c.src().ligne() + "," + c.src().colonne() + ") -> ("
        + c.dest().ligne() + "," + c.dest().colonne() + ") valeur = " + c.valeur());
    }

}
