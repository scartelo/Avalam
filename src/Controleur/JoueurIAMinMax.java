
package Controleur;

import Global.Configuration;
import Moteur.*;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;

import java.util.Random;


public class JoueurIAMinMax extends JoueurIA {
    Random r;
    //PlateauDeJeu plateauDeJeu;
    //Jeu clone;
    int nbCoups = 0; // Coups à l'avance en debut de partie

    JoueurIAMinMax(int n, Jeu jj) {
        super(n, jj);
        r = new Random();
    }


    
    //Nouvelle version d'evaluation
    //estimation de la chance de p
    public double evaluationV2(PlateauDeJeu p) {
        int[] liste = listeFi(p);
        int[] W = {1,2,4};
        double somme =0;
        int f ,w;
        for (int i = 0;i< liste.length;i++){
            //System.out.println("score fi :"+liste[i]+W[i]);
            somme = somme +(liste[i]*W[i]);
        }
        return somme;
    }

    // fi(plateauDeJeu) avec W l'importace de score
    // nombre de tour ayant plus de 1 pion sur le plateau
    // diff entre nombre de tour de joueur1 et joueur2
    // compter des tour à 5 pion ou isolé
    private int[] listeFi(PlateauDeJeu p) {
        int f1 = 0;// nombre de tour ayant plus de 1 pion . w1 = 1
        int f2 = 0; // diff entre nombre de tour de joueurIA et joueur ad. w2 = 2
        int f3 = 0;// compter des tours à 5 pion ou isolé . w3 = 4
        int f4 = 0;
        int[] fi ;

        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                Tour t = p.tour(i, j);
                //tour isolés à verifier
                if (!t.estVide() && !t.estInnocupable() && t.nbPion() > 1) {
                    if (num == t.sommetTour()) {
                        //System.out.println("avant resultat = " + resultat);
                        f1++;
                        if(t.nbPion() == 5 || p.estIsole(i,j)){
                            f3++;
                        }
                        //System.out.println("apres resultat = " + resultat);
                    } else if (num != t.sommetTour()) {
                        f2++;
                    }
                    /*else if(t.nbPion() == 5 && num == t.sommetTour()){
                        f3++;
                    }
                    else if( p.estIsole(i,j) && num == t.sommetTour()){
                        f4++;
                    }*/
                }
            }
        }
        fi = new int[]{f1, (f1 - f2), (f3 + f4)};
        return fi;
    }


    @Override
    boolean tempsEcoule() {
        //double[][] scores = new double[9][9];
        //double max = 0;
        //int ldep = 0, cdep = 0;
        CoupIA meilleurCoup = new CoupIA(null, null);
        double meilleurScore = Double.NEGATIVE_INFINITY;
        int horizon = 3;

        Couple<Integer, Integer> v;
        //Couple<Double, Tour> resultat = null;
        Tour mTourdep = null;
        Tour mTourdest = null;
        Coup opt = null;

        if (nbCoups < 7) {
            opt = isBetter(jeu.plateau());
            if (opt != null) {
                jeu.jouer(opt.src(), opt.dest());
                jeu.plateau().deselection_ia();
                jeu.plateau().selection_ia(opt.src(), opt.dest());
                jeu.miseAJour();
                nbCoups++;
                System.out.println("resultat apres jouer :" + evaluationV2(jeu.plateau()));
                System.out.println("nbCoups :" + nbCoups);
            } else {
                Coup c = couvrirAdversaire(jeu.plateau());
                jeu.jouer(c.src(), c.dest());
                jeu.plateau().deselection_ia();
                jeu.plateau().selection_ia(c.src(), c.dest());
                jeu.miseAJour();
                nbCoups++;
                System.out.println("resultat apres jouer :" + evaluationV2(jeu.plateau()));
                System.out.println("nbCoups :" + nbCoups);
            }
        } else {

            for (int i = 0; i < jeu.plateau().lignes(); i++) {
                for (int j = 0; j < jeu.plateau().colonnes(); j++) {
                    Tour selectionnee = jeu.plateau().tour(i, j);
                    if (selectionnee.estJouable() && !jeu.plateau().pasDeplacable(selectionnee)) {
                        Sequence<Tour> voisines = jeu.plateau().voisins(selectionnee.ligne(), selectionnee.colonne());
                        Iterateur<Tour> jouables = jeu.plateau().voisinsJouables(voisines).iterateur();
                        while (jouables.aProchain()) {
                            Tour dest = jouables.prochain();
                            if (dest.estJouable() && selectionnee.estDeplacable(dest)) {
                                CoupIA coupMinMax = new CoupIA(selectionnee, dest);
                                //PlateauDeJeu plateau = simulationCoup(clone.plateau(), coupMinMax);

                                jeu.plateau().jouer(selectionnee,dest);
                                PlateauDeJeu p = jeu.plateau();
                                jeu.plateau().annuler();
                                double score = miniMax(p, horizon, true);
                                //annuleCoupIA(p, coupMinMax);
                                if (score > meilleurScore) {
                                    meilleurScore = score;
                                    mTourdep = selectionnee;
                                    mTourdest = dest;
                                }
                            }

                        }

                    }

                }

            }


            System.out.println("apres min max");
            //meilleurCoup.afficheCoup();
            System.out.println("meilleur score = " + meilleurScore);
            jeu.plateau().jouer(mTourdep, mTourdest);
            jeu.plateau().deselection_ia();
            jeu.plateau().selection_ia(mTourdep, mTourdest);
            Tour tour1 = jeu.plateau().tour(3, 4);
            Tour tour2 = jeu.plateau().tour(2, 4);
            //jeu.jouer(tour1, tour2);
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

    public PlateauDeJeu simulationCoup(PlateauDeJeu p, Coup coup) {
        PlateauDeJeu plateau = p.clone();
        System.out.println("Nouveau plateau");
        if (coup.dest().ajouteTour(coup.src())) {
            System.out.println("Coup IA joué");
            coup.afficheCoup();
            p.placerTour(coup.dest().contenu(), coup.dest().ligne(), coup.dest().colonne());
            p.afficherGrille();
            plateau = p.clone();
        }
        return plateau;
    }

    private void annuleCoupIA(PlateauDeJeu p, Coup coup) {
        Coup tmp = coup;
        if (coup != null){
            System.out.println("Nouveau plateau");
            p.placerTour(coup.src().contenu(), coup.src().ligne(), coup.src().colonne());
            p.placerTour(coup.dest().contenu(), coup.dest().ligne(), coup.dest().colonne());
            System.out.println("IA a annulé");
            coup.afficheCoup();
            p.afficherGrille();
        }
    }


    private double evaluation(Jeu copy) {
        double diff = 0.0;
        if (num == 0) {
            diff = copy.score(num) - copy.score((num + 1) % 2);
        } else {
            diff = copy.score((num + 1) % 2) - copy.score(num);
        }
        return diff;
    }

    /*double miniMax(Jeu copy, Tour source, int horizon, boolean joueurMax) {
        System.out.println("joueurMax =  " + (joueurMax ? "max" : "min") + " / horizon=" + horizon);
        if (horizon == 0 || copy.estTermine()) {
            //CoupIA c = new CoupIA(null, null);
            //c.fixerValeur(evaluation(copy));
            return evaluation(copy);
        } else {
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
    }*/

    public boolean estfini(PlateauDeJeu plateau){
        boolean res=true;
        for(int i=0;i<plateau.lignes();i++){
            for(int j=0;j<plateau.colonnes();j++){
                if(plateau.grille()[i][j].estJouable()){
                    for(int x=-1;x<2;x++){
                        for(int y=-1;y<2;y++){
                            if((i+x>=0 && x+i<plateau.lignes())&&(j+y>=0 && j+y<plateau.colonnes()))  {
                                if(plateau.grille()[i][j].estDeplacable(plateau.grille()[i+x][y+j])){
                                    res = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        //plateau.update_score();
        //MAJScore();
        return res;
    }

    double miniMax(PlateauDeJeu p, int horizon, boolean joueurMax) {
        if (horizon == 0 || estfini(p)) {

            return evaluationV2(p);

        } else {
            double maxEval = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            CoupIA meilleurCoup =null;
            for (int i = 0; i < p.lignes(); i++) {
                for (int j = 0; j < p.colonnes(); j++) {
                    Tour depart = p.tour(i, j);
                    Sequence<Tour> toursVoisines = p.voisins(depart.ligne(), depart.colonne());
                    Iterateur<Tour> jouables = p.voisinsJouables(toursVoisines).iterateur();
                    while (jouables.aProchain()) {
                        Tour dest = jouables.prochain();
                        if (dest.estJouable() && depart.estDeplacable(dest)) {
                            //CoupIA coup = (CoupIA) new Coup(depart, dest);

                            p.jouer(depart,dest);
                            PlateauDeJeu pc = p;
                            p.annuler();
                            //PlateauDeJeu plateau = simulationCoup(copy.plateau(), coup);
                            double scoreMinMAx = miniMax(pc, horizon - 1, !joueurMax);
                            //annuleCoupIA(plateau, coup);
                            if (joueurMax) {
                                maxEval = (maxEval > scoreMinMAx) ? maxEval : scoreMinMAx;
                                //meilleurCoup = coup;
                            } else {
                                maxEval = (maxEval < scoreMinMAx) ? maxEval : scoreMinMAx;
                                //meilleurCoup = coup;
                            }

                        }
                    }
                }
            }
            return maxEval;
        }
    }



}
