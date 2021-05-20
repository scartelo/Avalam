package Controleur;


import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;
import java.util.*;

public class ControleurMediateur implements CollecteurEvenements {
    Jeu jeu;

    public ControleurMediateur(Jeu j){
        jeu=j;
    }
    void restart(){
    }

    void refaire(){
        jeu.Refaire();
    }
    void annule(){
        jeu.Annule();
    }
    void quitter() { jeu.Quitter(); }

    void save() {
        jeu.plateau.sauvegarder();
    }
    void load(String c){
        jeu.plateau.load_sauvegarde(Integer.parseInt(c));
    }

    @Override
    public boolean commande(String c){
        switch(c){
            case "restart":
                restart();
                break;
            case "annule":
                annule();
                break;
            case "refaire":
                refaire();
                break;
            case "save":
                save();
                break;
            case "quitter":
                quitter();
                break;
            default:
                return false;
        }
        return true;
    }


    public void commandeInput(String commande,String input) {
        switch(commande){
            case "load":
                System.out.println(input);
                load(input);
                break;
            default:

        }


    }

}
