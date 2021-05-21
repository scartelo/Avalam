package Controleur;

import Moteur.Jeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Sequence;

import java.util.Random;


public class JoueurIA extends Joueur{
    Random r;
    JoueurIA(int n, Jeu jj) {
        super(n, jj);
    }
    @Override
    boolean tempsEcoule() {
        // Pour cette IA, on selectionne aléatoirement une case libre
        int ldep,cdep,ldest,cdest;
        for(int i = 0 ; i < jp.plateau.lignes();i++){
            for(int j = 0; j < jp.plateau.colonnes();j++){
                if (jp.plateau.estAccessible(i,j)){
                    ldest = r.nextInt(((i+1) - (i-1)) + 1) + (i-1);
                    cdest = r.nextInt(((j+1) - (j-1)) + 1) + (j-1);
                    while(!jp.plateau.tour(i,j).estDeplacable(jp.plateau.tour(ldest,cdest))){
                        ldest = r.nextInt(((i+1) - (i-1)) + 1) + (i-1);
                        cdest = r.nextInt(((j+1) - (j-1)) + 1) + (j-1);
                    }
                    jp.plateau.Jouer(jp.plateau.tour(i,j),jp.plateau.tour(ldest,cdest));
                    return true;
                    }
                }
            }
        return false;
    }


    double miniMax(Tour t , int horizon, boolean joueurMax){
        int vl,vc;
        Sequence<Couple<Integer,Integer>> voisins;
        if (horizon == 0 && jp.plateau.estTermine()){
            //calculer score
            if (num == 0){
                return jp.scoreJ1();
            }
            else
                return jp.scoreJ2();


        }
       if(joueurMax) {
           double maxEval = Double.NEGATIVE_INFINITY;
           voisins  = jp.plateau.voisins(t.ligne(),t.colonne());
           while(!voisins.estVide()){
              vl = voisins.extraitTete().get_premier();
              vc =voisins.extraitTete().get_second();
              double s = miniMax(jp.plateau.tour(vl,vc),horizon-1,false);
              maxEval = Math.max(maxEval,s);
           }
           return maxEval;

        }
       else{
           double minEval = Double.POSITIVE_INFINITY;
           voisins  = jp.plateau.voisins(t.ligne(),t.colonne());
           while(!voisins.estVide()){
               vl = voisins.extraitTete().get_premier();
               vc =voisins.extraitTete().get_second();
               double s = miniMax(jp.plateau.tour(vl,vc),horizon-1,false);
               minEval = Math.min(minEval,s);
           }
           return minEval;
       }
    }
    /*public void bestmove(){
        double max = Double.NEGATIVE_INFINITY;

    }*/
}
