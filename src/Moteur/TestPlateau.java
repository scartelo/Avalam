package Moteur;

public class TestPlateau {
    public static void main(String[] args) {

        PlateauDeJeu p  = new PlateauDeJeu();
        p.afficher_grille();

        System.out.println("Test annuler un coup 0 ( echec ) ");
        p.annulerCoup();
        p.afficher_grille();

        System.out.println("Test coup 1");
        p.Jouer(p.tour(3,4),p.tour(3,5));
        p.afficher_grille();

        System.out.println("Test coup 2");
        p.Jouer(p.tour(2,5),p.tour(3,5));
        p.afficher_grille();

        System.out.println("Test coup 3");
        p.Jouer(p.tour(3,6),p.tour(3,5));
        p.afficher_grille();


        System.out.println("Test coup 4");
        p.Jouer(p.tour(4,5),p.tour(3,5));
        p.afficher_grille();


        System.out.println("Test refaire un coup 0 ( echec ) ");
        p.refaireCoup();
        p.afficher_grille();

        System.out.println("Test annuler un coup 1");
        p.annulerCoup();
        p.afficher_grille();

        System.out.println("Test annuler un coup 2");
        p.annulerCoup();
        p.afficher_grille();

        System.out.println("Test annuler un coup 3");
        p.annulerCoup();
        p.afficher_grille();

        System.out.println("Test annuler un coup 4");
        p.annulerCoup();
        p.afficher_grille();

        System.out.println("Test annuler un coup 5");
        p.annulerCoup();
        p.afficher_grille();

        System.out.println("Test refaire un coup 1");
        p.refaireCoup();
        p.afficher_grille();

        System.out.println("Test refaire un coup 2");
        p.refaireCoup();
        p.afficher_grille();
        System.out.println("Test refaire un coup 3");
        p.refaireCoup();
        p.afficher_grille();

        System.out.println("Test refaire un coup 4");
        p.refaireCoup();
        p.afficher_grille();

        System.out.println("Test refaire un coup 5 ( echec ) ");
        p.refaireCoup();
        p.afficher_grille();


        System.out.println(p.tour(3,5).estDeplacable(p.tour(4,6)));
    }
}
