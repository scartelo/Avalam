package Controleur;


import Moteur.Jeu;
import Vue.*;

import javax.swing.*;

public class ControleurMediateur implements CollecteurEvenements {
    Jeu jeu;

    public ControleurMediateur(Jeu j){
        jeu=j;
    }
    void restart(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous recommencer la partie ? ","Nouvelle partie",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            jeu.NouvellePartie();
        }
    }
    public void clicSouris(int l, int c){
        jeu.clic(l,c);
    }
        void refaire(){
        jeu.Refaire();
    }
    void annule(){
        jeu.Annule();
    }
    void quitter() {
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous vraiment quitter ? ","Quitter le jeu",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            jeu.Quitter();
        }
    }

    void retour_menu(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous revenir au menu ? ","Menu",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            InterfaceMenu m = new InterfaceMenu();
            m.showMenu(true);
            InterfaceGraphique.showFrame(false);
        }
    }
    void save() {
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous sauvegarder ? ","Sauvegarder",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            jeu.sauvegarder();
        }
    }

    void load(String c){
        jeu.load(Integer.parseInt(c),0);
    }

    @Override
    public boolean commande(String c){
        switch(c){
            case "retour_menu":
                retour_menu();
                break;
            case "quitter":
                quitter();
                break;
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
