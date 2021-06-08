package Controleur;

import Global.Audio;
import Global.Configuration;
import Moteur.Coup;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;

import java.util.Random;

public class JoueurIAAleatoire extends JoueurIA{
    //Random r;
    int nbCoups = 0;
    JoueurIAAleatoire(int n, Jeu jeu){
        super(n,jeu);
        //r = new Random();
    }
    //TODO ajouter les stra
    Tour selectionAleatoire(PlateauDeJeu plateau){
        int departL = r.nextInt(plateau.lignes());
        int departC = r.nextInt(plateau.colonnes());
        Tour departTour = plateau.tour(departL,departC);
        while (!departTour.estJouable() || plateau.pasDeplacable(departTour)){
            departL = r.nextInt(plateau.lignes());
            departC = r.nextInt(plateau.colonnes());
            departTour = plateau.tour(departL,departC);
        }
        return departTour;
    }

    public boolean optimal(PlateauDeJeu p ){
        Coup opt = null;
        opt = isBetter(p);
        if (opt != null) {
            return true;
        }
        else return false;

    }

    @Override
    boolean tempsEcoule() {
        Coup opt = null;
        System.out.println("debut IA");
        boolean t = optimal(jeu.plateau());
        //while(!jeu.estTermine()) {
            opt = isBetter(jeu.plateau());
            if (opt != null) {
                jeu.jouer(opt.src(), opt.dest());
                jeu.plateau().deselection_ia();
                jeu.plateau().selection_ia(opt.src(), opt.dest());
                jeu.miseAJour();
                nbCoups++;
                //System.out.println("resultat apres jouer :" + evaluationV2(jeu.plateau()));
                System.out.println("nbCoups :" + nbCoups);
                return true;
            } else {
                Coup c = couvrirAdversaire(jeu.plateau());
                if (c == null){
                    c = coupAleatoire(jeu.plateau());
                }
                jeu.jouer(c.src(), c.dest());
                jeu.plateau().deselection_ia();
                jeu.plateau().selection_ia(c.src(), c.dest());
                jeu.miseAJour();
                nbCoups++;
                //System.out.println("resultat apres jouer :" + evaluationV2(jeu.plateau()));
                System.out.println("nbCoups :" + nbCoups);
                return true;
            }




    }

    private Coup coupAleatoire(PlateauDeJeu p) {
        Tour departTour = selectionAleatoire(p);
        Coup c = null;
        //Audio.play_sound("Pick");
        Configuration.instance().logger().info(
                "La tour (" + departTour.ligne() + ", " + departTour.colonne() + ") a été selectionnée");
        Sequence<Tour> voisinsDepart = p.voisins(departTour.ligne(), departTour.colonne());
        int position = r.nextInt(voisinsDepart.taille()); // choix aléatoire parmi les 8 voisins maximum
        int i = 0;
        Iterateur<Tour> it = voisinsDepart.iterateur();
        while (it.aProchain()){
            Tour destTour = it.prochain();
            if (i == position){
                if (destTour.estDeplacable(departTour) && destTour.estJouable()){

                    c = new  Coup(departTour, destTour);
                    return c;
                }
            }else {
                i++;
            }

        }
        return c;
    }
}
