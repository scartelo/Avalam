
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
    final int NBCDEBUT = 7; // nombre de coups max avant minmax
    int horizon = 2;

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

        Coup opt = null;

        if (nbCoups < NBCDEBUT) {
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
            PlateauDeJeu p = jeu.clonerPlateau();
            int[] meilleurCoup = new int[4];
            meilleurCoup = meilleurDeplacement(p);


            System.out.println("apres meilleur deplacement");
            //meilleurCoup.afficheCoup();
            //System.out.println("meilleur score = " + meilleurScore);
            Tour t = jeu.plateau().tour(meilleurCoup[0], meilleurCoup[1]);
            Tour t2 = jeu.plateau().tour(meilleurCoup[2], meilleurCoup[3]);
            jeu.jouer(t, t2);
            Configuration.instance().logger().info(
                    "Déplacement de la tour (" + t.ligne() + "," + t.colonne() + ") vers la tour (" + t2.ligne() + "," + t2.colonne() + ")");
            jeu.plateau().deselection_ia();
            jeu.plateau().selection_ia(t, t2);
            jeu.miseAJour();

        }
        return true;
    }

    private int[] meilleurDeplacement(PlateauDeJeu p){
        int [] coordonnes = new int[]{-1,-1,-1,-1};
        /*for (int i=0; i<bestMove.length; i++){
            bestMove[i] = -1;
        }*/
        double meilleurScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                Tour selectionnee = p.tour(i, j);
                Tour simuleDepart = new Tour(selectionnee.contenu(), selectionnee.ligne(), selectionnee.colonne());
                if (selectionnee.estJouable() ) {
                    Sequence<Tour> voisines = p.voisins(selectionnee.ligne(), selectionnee.colonne());
                    Iterateur<Tour> jouables = p.voisinsJouables(voisines).iterateur();
                    while (jouables.aProchain()) {
                        Tour dest = jouables.prochain();
                        Tour simuleDest = new Tour(dest.contenu(), dest.ligne(), dest.colonne());
                        if (dest.estJouable() && selectionnee.estDeplacable(dest)) {
                            //CoupIA coupMinMax = new CoupIA(selectionnee, dest);
                            //PlateauDeJeu plateau = simulationCoup(clone.plateau(), coupMinMax);
                            System.out.println("Clone plateau avant ajout");
                            p.afficherGrille();

                            System.out.println("Clone plateau apres ajout");
                            dest.ajouteTour(selectionnee);
                            p.afficherGrille();


                            //jeu.plateau().jouer(selectionnee,dest);
                            //PlateauDeJeu p = jeu.clonerPlateau();
                            //jeu.plateau().annuler();

                            double score = miniMax(p, horizon, true);
                            System.out.println("vider");
                            p.tour(selectionnee.ligne(), selectionnee.colonne()).viderTour();
                            p.tour(dest.ligne(), dest.colonne()).viderTour();
                            p.afficherGrille();

                            System.out.println("simulation");
                            p.placerTour(simuleDepart.contenu(), simuleDepart.ligne(), simuleDepart.colonne());
                            p.placerTour(simuleDest.contenu(), simuleDest.ligne(), simuleDest.colonne());

                            System.out.println("Remise à l'état apres min max");
                            p.afficherGrille();

                            //annuleCoupIA(p, coupMinMax);
                            if (score > meilleurScore) {
                                meilleurScore = score;
                                coordonnes[0] = simuleDepart.ligne();
                                coordonnes[1] = simuleDepart.colonne();
                                coordonnes[2] = simuleDest.ligne();
                                coordonnes[3] = simuleDest.colonne();
                            }
                        }

                    }

                }

            }

        }
        return coordonnes;
    }
    private double evaluation(PlateauDeJeu copy) {
        return copy.score(num);
    }

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
                                    return res;
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
        if (horizon == 0 || p.estFini()) {

            return evaluationV2(p);

        } else {

            double maxEval = joueurMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            CoupIA meilleurCoup =null;
            for (int i = 0; i < p.lignes(); i++) {
                for (int j = 0; j < p.colonnes(); j++) {
                    Tour depart = p.tour(i, j);
                    Tour simuleDepart = new Tour(depart.contenu(), depart.ligne(), depart.colonne());
                    if (depart.estJouable()){
                        Sequence<Tour> toursVoisines = p.voisins(depart.ligne(), depart.colonne());
                        Iterateur<Tour> jouables = p.voisinsJouables(toursVoisines).iterateur();
                        while (jouables.aProchain()) {
                            Tour dest = jouables.prochain();
                            Tour simuleDest = new Tour(dest.contenu(), dest.ligne(), dest.colonne());
                            if (depart.estDeplacable(dest)) {
                                //CoupIA coup = (CoupIA) new Coup(depart, dest);

                                //p.jouer(depart,dest);
                                dest.ajouteTour(depart);

                                //PlateauDeJeu pc = p.clonerPlateau();

                                //p.annuler();
                                //PlateauDeJeu plateau = simulationCoup(copy.plateau(), coup);
                                double scoreMinMAx = miniMax(p, horizon - 1, !joueurMax);
                                //annuleCoupIA(plateau, coup);
                                System.out.println("vider dans min max");
                                p.tour(depart.ligne(), depart.colonne()).viderTour();
                                p.tour(dest.ligne(), dest.colonne()).viderTour();
                                p.afficherGrille();

                                System.out.println("simulation dans min max");
                                p.placerTour(simuleDepart.contenu(), simuleDepart.ligne(), simuleDepart.colonne());
                                p.placerTour(simuleDest.contenu(), simuleDest.ligne(), simuleDest.colonne());

                                System.out.println("Remise à l'état apres min max");
                                p.afficherGrille();

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
            }
            return maxEval;
        }
    }



}
