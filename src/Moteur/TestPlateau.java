package Moteur;

import Structures.Iterateur;
import Structures.Sequence;

public class TestPlateau {
    public static void main(String[] args) {

        PlateauDeJeu p  = new PlateauDeJeu();
        p.afficherGrille();

        System.out.println("Test annuler un coup 0 ( echec ) ");
        p.annulerCoup();
        p.afficherGrille();

        System.out.println("Test coup 1");
        p.jouer(p.tour(3,4),p.tour(3,5));
        p.afficherGrille();

        System.out.println("Test marquage");
        System.out.println(" Contenu " + p.tour(3,3).contenu());
        //p.tour(3,3).marquer(0x1A);
        System.out.println(" Contenu apres marquage " + p.tour(3,3).contenu());



        System.out.println("Test coup 2");
        p.jouer(p.tour(2,5),p.tour(3,5));
        p.afficherGrille();

        System.out.println("Test coup 3");
        p.jouer(p.tour(3,6),p.tour(3,5));
        p.afficherGrille();


        System.out.println("Test coup 4");
        p.jouer(p.tour(4,5),p.tour(3,5));
        p.afficherGrille();


        System.out.println("Test refaire un coup 0 ( echec ) ");
        p.refaireCoup();
        p.afficherGrille();

        System.out.println("Test annuler un coup 1");
        p.annulerCoup();
        p.afficherGrille();

        System.out.println("Test annuler un coup 2");
        p.annulerCoup();
        p.afficherGrille();

        System.out.println("Test annuler un coup 3");
        p.annulerCoup();
        p.afficherGrille();

        System.out.println("Test annuler un coup 4");
        p.annulerCoup();
        p.afficherGrille();

        System.out.println("Test annuler un coup 5");
        p.annulerCoup();
        p.afficherGrille();

        System.out.println("Test refaire un coup 1");
        p.refaireCoup();
        p.afficherGrille();

        System.out.println("Test refaire un coup 2");
        p.refaireCoup();
        p.afficherGrille();
        System.out.println("Test refaire un coup 3");
        p.refaireCoup();
        p.afficherGrille();

        System.out.println("Test refaire un coup 4");
        p.refaireCoup();
        p.afficherGrille();

        System.out.println("Test refaire un coup 5 ( echec ) ");
        p.refaireCoup();
        p.afficherGrille();


        System.out.println("Test Voisins jouables ");
        Sequence v = p.voisins(0,5);
        Sequence j = p.voisinsJouables(v);
        Iterateur it = j.iterateur();
        while (it.aProchain()){
            Tour t = (Tour) it.prochain();
            t.afficher();

        }

        System.out.println("Test Voisins jouables (0,0) ");
        Sequence v2 = p.voisins(0,0);
        Sequence j2 = p.voisinsJouables(v2);
        Iterateur it2 = j2.iterateur();
        while (it2.aProchain()){
            Tour t = (Tour) it2.prochain();
            t.afficher();

        }

        System.out.println("Test Voisins jouables (5,2) ");
        Sequence v3 = p.voisins(5,2);
        Sequence j3 = p.voisinsJouables(v3);
        Iterateur it3 = j3.iterateur();
        while (it3.aProchain()){
            Tour t = (Tour) it3.prochain();
            t.afficher();

        }

        p.afficherGrille();



        System.out.println(p.tour(3,5).estDeplacable(p.tour(4,6)));
    }
}
