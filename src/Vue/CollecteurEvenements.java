package Vue;

public interface CollecteurEvenements {

    boolean commande(String commande);
    void commandeInput(String commande,String input);
}
