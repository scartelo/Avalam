package Moteur;

public class TestTour {
    public static void main(String[] args) {
        Tour t = new Tour(0, 0,5);
        Tour t2 = new Tour(9, 0, 6);
        t.ajouteTour(t2);
        System.out.println("T1");
        t.afficher();
        System.out.println("T2");
        t2.afficher();
    }
}
