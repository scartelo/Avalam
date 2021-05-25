package Controleur;

import Moteur.Jeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Sequence;

import java.util.Random;


public abstract class JoueurIAMinMax extends Joueur{
    Random r;
    JoueurIAMinMax(int n, Jeu jj) {
        super(n, jj);
    }
    @Override
    boolean tempsEcoule() {
        // Pour cette IA, on selectionne aléatoirement une case libre
        int ldep,cdep,ldest,cdest;
        for(int i = 0; i < jeu.plateau.lignes(); i++){
            for(int j = 0; j < jeu.plateau.colonnes(); j++){
                if (jeu.plateau.estAccessible(i,j)){
                    ldest = r.nextInt(((i+1) - (i-1)) + 1) + (i-1);
                    cdest = r.nextInt(((j+1) - (j-1)) + 1) + (j-1);
                    while(!jeu.plateau.tour(i,j).estDeplacable(jeu.plateau.tour(ldest,cdest))){
                        ldest = r.nextInt(((i+1) - (i-1)) + 1) + (i-1);
                        cdest = r.nextInt(((j+1) - (j-1)) + 1) + (j-1);
                    }
                    jeu.plateau.Jouer(jeu.plateau.tour(i,j), jeu.plateau.tour(ldest,cdest));
                    return true;
                    }
                }
            }
        return false;
    }


    double miniMax(Tour t , int horizon, boolean joueurMax){
        int vl,vc;
        Sequence<Couple<Integer,Integer>> voisins;
        if (horizon == 0 && jeu.estTermine()){
            //calculer score
            if (num == 0){
                return jeu.plateau.scoreJ1();
            }
            else
                return jeu.plateau.scoreJ2();


        }
       if(joueurMax) {
           double maxEval = Double.NEGATIVE_INFINITY;
           voisins  = jeu.plateau.voisins(t.ligne(),t.colonne());
           while(!voisins.estVide()){
              vl = voisins.extraitTete().premier();
              vc =voisins.extraitTete().second();
              double s = miniMax(jeu.plateau.tour(vl,vc),horizon-1,false);
              maxEval = Math.max(maxEval,s);
           }
           return maxEval;

        }
       else{
           double minEval = Double.POSITIVE_INFINITY;
           voisins  = jeu.plateau.voisins(t.ligne(),t.colonne());
           while(!voisins.estVide()){
               vl = voisins.extraitTete().premier();
               vc =voisins.extraitTete().second();
               double s = miniMax(jeu.plateau.tour(vl,vc),horizon-1,false);
               minEval = Math.min(minEval,s);
           }
           return minEval;
       }
    }


    /*public void bestmove(){
        double max = Double.NEGATIVE_INFINITY;

    }*/
}
