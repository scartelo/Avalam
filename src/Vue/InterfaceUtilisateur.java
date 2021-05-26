package Vue;

public interface InterfaceUtilisateur {
    void update_waiting();

    void reset_waiting();

    void basculePleinEcran();

    void sauvegarder();

    void quitter();

    void nouvellePartie();

    void retour_menu();

    void griser_annuler(boolean b);

    void griser_refaire(boolean b);

    void load(String input);
}
