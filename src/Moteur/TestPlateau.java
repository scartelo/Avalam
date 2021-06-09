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

        System.out.println("Test ajout");
        p.afficherGrille();
        Tour t = p.tour(8,2);
        Tour t2 = p.tour(7,2);
        Tour t8 = new Tour(t.contenu(), t.ligne(), t.colonne());
        Tour t9 = new Tour(t2.contenu(), t2.ligne(), t2.colonne());
        System.out.println("avant");
        System.out.println("T1");
        t.afficher();
        System.out.println("T2");
        t2.afficher();

        t.ajouteTour(t2);

        System.out.println("apres");
        System.out.println("T1");
        t.afficher();
        System.out.println("T2");
        t2.afficher();
        p.afficherGrille();


        System.out.println("encore apres");
        System.out.println("T8");
        t8.afficher();
        System.out.println("T9");
        t9.afficher();

        System.out.println("vider");
        p.tour(t2.ligne(), t2.colonne()).viderTour();
        p.tour(t.ligne(), t.colonne()).viderTour();
        p.afficherGrille();

        System.out.println("simulation");
        p.placerTour(t8.contenu(), t8.ligne(), t8.colonne());
        p.placerTour(t9.contenu(), t9.ligne(), t9.colonne());

        p.afficherGrille();
        /*Tour t = p.tour(8,2);
        Tour t2 = p.tour(7,2);
        Tour t8 = new Tour(t.contenu(), t.ligne(), t.colonne());
        Tour t9 = new Tour(t2.contenu(), t2.ligne(), t2.colonne());
        System.out.println("avant");
        System.out.println("T1");
        t.afficher();
        System.out.println("T2");
        t2.afficher();

        t.ajouteTour(t2);

        System.out.println("apres");
        System.out.println("T1");
        t.afficher();
        System.out.println("T2");
        t2.afficher();


        System.out.println("encore apres");
        System.out.println("T8");
        t8.afficher();
        System.out.println("T9");
        t9.afficher();*/

        Tour t3 = p.tour(7,2);
        Tour t4 = t;
        Coup coup = new Coup(t, t2);

        System.out.println("Test simulation et annulation");
        //p.simulationCoup(coup);
        //p.afficherGrille();
        /*System.out.println("vider");
        p.tour(t2.ligne(), t2.colonne()).viderTour();
        p.tour(t.ligne(), t.colonne()).viderTour();
        p.afficherGrille();

        System.out.println("simulation");
        p.placerTour(t8.contenu(), t8.ligne(), t8.colonne());
        p.placerTour(t9.contenu(), t9.ligne(), t9.colonne());*/
        //p.tour(t2.ligne(), t2.colonne()).placerTour(t3.contenu(), t2.ligne(), t2.colonne());
        //p.afficherGrille();

        System.out.println("annulation");
        //p.annuleCoupIA2(coup);
        p.afficherGrille();




        System.out.println(p.tour(3,5).estDeplacable(p.tour(4,6)));
    }
}
