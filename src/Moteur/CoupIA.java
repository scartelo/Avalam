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
}
