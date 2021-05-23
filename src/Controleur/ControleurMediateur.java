package Controleur;


import Global.Configuration;
import Moteur.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

import javax.swing.*;

public class ControleurMediateur implements CollecteurEvenements {
    Jeu jeu;
    InterfaceUtilisateur iu;
    Joueur[][] joueurs;
    int[] typeJoueur;
    int joueurCourant;

    public ControleurMediateur(Jeu j){
        jeu=j;
        joueurs = new Joueur[2][2];
        typeJoueur = new int[2];
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i][0] = new JoueurHumain(i, jeu);
            joueurs[i][1] = new JoueurIA(i, jeu);
            typeJoueur[i] = 0;
        }
    }
    public void update_buttons(){
        iu.griser_annuler(jeu.plateau.peutAnnuler());
        iu.griser_refaire(jeu.plateau.peutRefaire());
    }
    @Override
    public void clicSouris(int l, int c) {
        // Lors d'un clic, on le transmet au joueur courant.
        // Si un coup a effectivement été joué (humain, coup valide), on change de joueur.
        /*
        Configuration.instance().logger().info("Joueur courant :" + joueurCourant);
        if (joueurs[joueurCourant][typeJoueur[joueurCourant]].joue(l, c)) {
            jeu.miseAJour();
            changeJoueur();
        }*/
        jeu.clic(l,c);
        update_buttons();
    }

    void changeJoueur() {
        joueurCourant = (joueurCourant + 1) % joueurs.length;
        //decompte = lenteurAttente;
    }

    public void annuler(){
        jeu.annuler();update_buttons();
    }

    public void refaire(){ jeu.refaire();update_buttons(); }

    void load(String c){
        int res = JOptionPane.showConfirmDialog(null,"Êtes vous sur de vouloir charger la sauvegarde ? ","Charger",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            iu.sauvegarder();
            jeu.load(Integer.parseInt(c),0);
        }
    }

    @Override
    public void fixerInterfaceUtilisateur(InterfaceUtilisateur i) {
        iu = i;
        update_buttons();
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
                load(input);
                break;
            default:

        }


    }

}
