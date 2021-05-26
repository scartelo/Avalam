package Vue;

public interface CollecteurEvenements {
    void fixerInterfaceUtilisateur(InterfaceUtilisateur i);

    void basculeJoueurIA(int j, int t);

    boolean commande(String commande);
    void commandeInput(String commande,String input);

    void clicSouris(int l, int c);

    void update_buttons();

    void tictac();

    void init_joueurs();
}
