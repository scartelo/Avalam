package Moteur;

public class TestPlateau {
    public static void main(String[] args) {

        PlateauDeJeu p  = new PlateauDeJeu();
        p.afficher_grille();

        p.Jouer(p.tour(3,4),p.tour(3,5));
        p.afficher_grille();

        p.Jouer(p.tour(2,5),p.tour(3,5));
        p.afficher_grille();

        p.Jouer(p.tour(3,6),p.tour(3,5));
        p.afficher_grille();

        p.Jouer(p.tour(4,5),p.tour(3,5));
        p.afficher_grille();
        p.Annuler_coup();
        p.afficher_grille();
        p.Annuler_coup();
        p.afficher_grille();
        p.Annuler_coup();
        p.afficher_grille();
        p.Annuler_coup();
        p.afficher_grille();
        p.Annuler_coup();
        p.afficher_grille();
        p.sauvegarder();
        System.out.println("Test refaire un coup 1");
        p.Refaire_coup();
        p.afficher_grille();
        System.out.println("Test 2");
        p.Refaire_coup();
        p.afficher_grille();
        p.Refaire_coup();
        p.afficher_grille();
        p.Refaire_coup();
        p.afficher_grille();
        p.Refaire_coup();
        p.afficher_grille();
        p.Refaire_coup();
        p.afficher_grille();

        System.out.println(p.tour(3,5).estDeplacable(p.tour(2,5)));


    }
}
