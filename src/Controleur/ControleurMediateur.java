package Controleur;


import Moteur.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

import javax.swing.*;

public class ControleurMediateur implements CollecteurEvenements {
    Jeu jeu;
    InterfaceUtilisateur iu;

    public ControleurMediateur(Jeu j){
        jeu=j;
    }

    public void clicSouris(int l, int c){
        jeu.clic(l,c);
    }

    public void annuler(){
        jeu.annuler();
    }

    public void refaire(){
        jeu.refaire();
    }

    void load(String c){
        int res = JOptionPane.showConfirmDialog(null,"Êtes vous sur de vouloir charger la sauvegarde ? ","Charger",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            jeu.load(Integer.parseInt(c),0);
        }
    }

    @Override
    public void fixerInterfaceUtilisateur(InterfaceUtilisateur i) {
        iu = i;
    }

    @Override
    public boolean commande(String c){
        switch(c){
            case "retour_menu":
                iu.retour_menu();
                break;
            case "quitter":
                iu.quitter();
                break;
            case "nouvellePartie":
                iu.nouvellePartie();
                break;
            case "annuler":
                annuler();;
                break;
            case "refaire":
                refaire();
                break;
            case "sauvegarder":
                iu.sauvegarder();
                break;
            case "pleinecran":
                iu.basculePleinEcran();
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
