package Controleur;


import Moteur.Jeu;
import Vue.CollecteurEvenements;

public class ControleurMediateur implements CollecteurEvenements {
    Jeu jeu;

    public ControleurMediateur(Jeu j){
        jeu=j;
    }
    void restart(){
    }
    public void clicSouris(int l, int c){
        jeu.clic(l,c);
    }
        void refaire(){
        jeu.Refaire();
    }
    void annule(){
        jeu.Partie(0);
        jeu.Annule();
    }

    void save() {
        jeu.plateau.sauvegarder();
    }

    void load(String c){
        jeu.load(Integer.parseInt(c));
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