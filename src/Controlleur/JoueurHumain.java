package Moteur;

public class JoueurHumain extends Joueur{
    JoueurHumain(int n, PlateauDeJeu pj){
        super(n,pj);
    }
    @Override
    boolean jeu (Tour dep, Tour dest ) {
        /*if (pjeu.estAccessible(ldest,cdest)){
            pjeu.jouer(ldep,cdep,ldest,cdest);
            return true;
        }
        else*/
            return false;

    }
}
