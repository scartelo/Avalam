
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



    //Nouvelle version d'evaluation
    //estilation de la chance de p
    public int evaluationV2(PlateauDeJeu p ){

        Sequence<Integer> liste = listeFi(p);
        Iterateur it = liste.iterateur();
        int f;
        int somme = 0;
        while(it.aProchain()){
            f = (int) it.prochain();
            somme  += f;
        }

        return somme;
    }

    private Sequence<Integer> listeFi(PlateauDeJeu p) {
        int f1 = 0;// nombre de tour ayant plus de 1 pion
        int f2 = 0; // // diff entre nobre de tour de joueurIA et joueur ad.
        Sequence<Integer> fi = Configuration.instance().nouvelleSequence();
        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                Tour t = p.tour(i, j);
                if (!t.estVide() && !t.estInnocupable() && t.nbPion() > 1) {
                    if (num == t.sommetTour()) {
                        //System.out.println("avant resultat = " + resultat);
                        f1++;
                        //System.out.println("apres resultat = " + resultat);
                    }
                    else if (num != t.sommetTour()){
                        f2++;
                    }
                }
            }
        }
        fi.insereTete(f1);
        fi.insereTete(f1-f2);

        return fi;
    }


    @Override
    boolean tempsEcoule() {
        //double[][] scores = new double[9][9];
        //double max = 0;
        //int ldep = 0, cdep = 0;
        Coup meilleurCoup = null;
        double meilleurScore = Double.NEGATIVE_INFINITY;
        int horizon = 0;

        Couple<Integer, Integer> v;
        Couple<Double, Tour> resultat = null;
        Tour tour = null;
        Couple<Tour,Tour> opt = null;

        if (nbCoups < 5) {
            opt = isBetter(jeu.plateau());
            if (opt !=null){
                jeu.jouer(opt.premier(), opt.second());
                jeu.miseAJour();
                nbCoups++;
                System.out.println("resulata apres jouer :"+ evaluationV2(jeu.plateau()));
                System.out.println("nbCoups :"+nbCoups);
            }
            else {
                couvrirAdversaire(jeu);
                nbCoups++;
                System.out.println("resulata apres jouer :"+ evaluationV2(jeu.plateau()));
                System.out.println("nbCoups :"+nbCoups);
            }
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
