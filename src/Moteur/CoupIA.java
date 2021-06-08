package Moteur;

public class CoupIA extends Coup{
    public int valeur; // nombre de tour
    public CoupIA(){
        super();

    }
    public CoupIA(Tour source, Tour dst) {
        super(source, dst);
    }
    public int valeur(){
        return valeur;
    }
    public void fixerValeur(int v){
        valeur = v;
    }

    @Override
    public void afficheCoup(){
        System.out.println("Coup: (" + src().ligne() + "," + src().colonne() + ") -> ("
                + dest().ligne() + "," + dest().colonne() + ") valeur = " + valeur());
    }
}
