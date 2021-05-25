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
    private int decompte;
    private int lenteurAttente = 100;

    public ControleurMediateur(Jeu j){
        jeu=j;
        joueurs = new Joueur[2][2];
        typeJoueur = new int[2];
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i][0] = new JoueurHumain(i, jeu);
            joueurs[i][1] = new JoueurIAAleatoire(i, jeu);
            //typeJoueur[i] = 0;
        }
        typeJoueur[0]=jeu.IA1;
        typeJoueur[1]=jeu.IA2;
    }
    public void update_buttons(){
        iu.griser_annuler(jeu.plateau.peutAnnuler());
        iu.griser_refaire(jeu.plateau.peutRefaire());
    }
    @Override
    public void clicSouris(int l, int c) {
        // Lors d'un clic, on le transmet au joueur courant.
        // Si un coup a effectivement été joué (humain, coup valide), on change de joueur.

        if (l>=0 && c>=0 && l<jeu.plateau.lignes() && c<jeu.plateau.colonnes()&&joueurs[joueurCourant][typeJoueur[joueurCourant]].joue(l, c)) {
            jeu.miseAJour();
            changeJoueur();
        }
        //jeu.clic(l,c);
        update_buttons();
    }

    void changeJoueur() {
        joueurCourant = (joueurCourant + 1) % joueurs.length;
        //decompte = lenteurAttente;
    }

    public void annuler(){
        boolean b=jeu.annuler();
        if(b){
            changeJoueur();
            if(jeu.isIA(joueurCourant)){
                jeu.change_ia_state(joueurCourant,1);
            }
        }
        update_buttons();
    }

    public void refaire(){
        boolean b=jeu.refaire();
        if(b){
            changeJoueur();
            if(jeu.isIA(joueurCourant)){
                jeu.change_ia_state(joueurCourant,1);
            }
        }
        update_buttons(); }

    @Override
    public void fixerInterfaceUtilisateur(InterfaceUtilisateur i) {
        iu = i;
        //update_buttons();
    }
    @Override
    public void basculeJoueurIA(int j, int t) {
        Configuration.instance().logger().info("Nouveau type " + t + " pour le joueur " + j);
        typeJoueur[j] = t;
        if(j==0){
            jeu.IA1=t;
            if(t==1){
                jeu.IA1_ref=1;
            }
        }else{
            jeu.IA2=t;
            if(t==1){
                jeu.IA2_ref=1;
            }
        }
    }

    public void tictac() {
        if (!jeu.estTermine()) {
            if (decompte == 0) {
                int type = typeJoueur[joueurCourant];
                int courant = joueurs[joueurCourant][type].num() + 1;
                // Lorsque le temps est écoulé on le transmet au joueur courant.
                // Si un coup a été joué (IA) on change de joueur.
                if (joueurs[joueurCourant][type].tempsEcoule()) {
                    jeu.change_ia_state(joueurCourant,0);
                    changeJoueur();
                    if(jeu.isIA(joueurCourant)){
                        jeu.change_ia_state(joueurCourant,1);
                    }
                    update_buttons();
                }
                else {
                    // Sinon on indique au joueur qui ne réagit pas au temps (humain) qu'on l'attend.
                    System.out.println("On vous attend, joueur " + courant);
                    decompte = lenteurAttente;
                }
            } else {
                decompte--;
            }
        }
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
                iu.load(input);
                break;
            default:

        }


    }

}
