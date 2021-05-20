package Vue;


import Moteur.Coup;

public interface CollecteurEvenements {

    boolean commande(String commande);
    void commandeInput(String commande,String input);
}
