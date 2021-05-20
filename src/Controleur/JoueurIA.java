package Controleur;

import Moteur.Jeu;

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
}
