package Vue;


import Moteur.Coup;

public interface CollecteurEvenements {
    void joue(int l, int c);

    public void clicSouris(int l, int c);

    void jouerCoup(Coup coup);

    public void fixerIU(InterfaceUtilisateur iu);
}
