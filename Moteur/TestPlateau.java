package Moteur;

public class TestPlateau {
    public static void main(String[] args) {
        PlateauDeJeu p  = new PlateauDeJeu();
        p.afficher_grille();
        p.tour(3,5).ajouteTour(p.tour(3,4));
        p.tour(3,5).ajouteTour(p.tour(2,5));
        p.afficher_grille();
        System.out.println(p.tour(3,5).estDeplacable(p.tour(2,5)));
    }
}
