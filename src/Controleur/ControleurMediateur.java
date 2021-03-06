package Controleur;


import Global.Audio;
import Global.Configuration;
import Moteur.Jeu;
import Vue.CollecteurEvenements;
import Vue.InterfaceUtilisateur;

import java.util.Timer;
import java.util.TimerTask;

public class ControleurMediateur implements CollecteurEvenements {
    Jeu jeu;
    InterfaceUtilisateur iu;
    Joueur[][] joueurs;
    int[] typeJoueur;
    public int joueurCourant;
    private int decompte;
    private int lenteurAttente = 100;
    public boolean playing;
    public Timer time;
    public boolean timer;
    public ControleurMediateur(Jeu j){
        jeu=j;
        init_joueurs();
        playing=true;
        timer=false;
    }

    public JoueurIA initiJoueurIA(int niveau, int num, Jeu j){
        JoueurIA joueurIA = null;
        switch (niveau){
            case 0:
                joueurIA = new JoueurIAAleatoire(num, j);
                break;
            case 1:
                joueurIA = new JoueurIAMinMax(num, j);
                break;
            case 2:
                joueurIA = new JoueurIAAlphaBeta(num, j);
                break;
            default:
                Configuration.instance().logger().severe("Niveau IA inconnue");
                System.exit(1);
        }
        assert(joueurIA != null);
        return joueurIA;

    }
    public void init_joueurs(){
        joueurs = new Joueur[2][2];
        typeJoueur = new int[2];
        for (int i = 0; i < joueurs.length; i++) {
            joueurs[i][0] = new JoueurHumain(i, jeu);
            joueurs[i][1] = initiJoueurIA(jeu.niveau(i), i, jeu);
            //joueurs[i][1] = new JoueurIAMinMax(i, jeu);
            //joueurs[i][1] = new JoueurIAAlphaBeta(i, jeu);
            typeJoueur[i] = jeu.estIA(i);
        }
        //typeJoueur[0]=jeu.IA1;
        //typeJoueur[1]=jeu.IA2;
        joueurCourant=jeu.plateau.tourJoueur;
    }

    @Override
    public boolean playing() {
        return playing;
    }
    public void end_timer(){
        timer=false;
    }
    public void change_play_state(){
        playing=!playing;
    }
    public void update_buttons(){
        iu.griser_annuler(jeu.plateau.peutAnnuler());
        iu.griser_refaire(jeu.plateau.peutRefaire());
    }
    @Override
    public void clicSouris(int l, int c) {
        // Lors d'un clic, on le transmet au joueur courant.
        // Si un coup a effectivement ??t?? jou?? (humain, coup valide), on change de joueur.

        if (l>=0 && c>=0 && l<jeu.plateau.lignes() && c<jeu.plateau.colonnes()&&joueurs[joueurCourant][typeJoueur[joueurCourant]].joue(l, c)) {
            jeu.miseAJour();
            changeJoueur();
            update_buttons();
            if(jeu.estTermine()){
                Audio.play_sound("GaveOver");
                iu.Win_message();
                Configuration.instance().logger().info("Partie termin??e");
            }

        }
        //jeu.clic(l,c);

    }

    public void changeJoueur() {
        joueurCourant = (joueurCourant + 1) % joueurs.length;
        decompte = lenteurAttente;
        iu.reset_waiting();
        timer=false;
        jeu.plateau.deselect_propose();
        System.out.println("YO");//Deselectionne propose
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

    public void suggestion(){
        jeu.suggestion(joueurCourant);
    }

    public void deselect_propose(){
        jeu.deselect_propose();
    }

    public void refaire(){
        boolean b=jeu.refaire();
        if(b){
            changeJoueur();
            if(jeu.isIA(joueurCourant)){
                jeu.change_ia_state(joueurCourant,1);
            }
        }
        update_buttons();
        if(jeu.estTermine()){
            iu.Win_message();
            Configuration.instance().logger().info("Partie termin??e");
        }
    }

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
                //int courant = joueurs[joueurCourant][type].num() + 1;
                // Lorsque le temps est ??coul?? on le transmet au joueur courant.
                // Si un coup a ??t?? jou?? (IA) on change de joueur.
                if (joueurs[joueurCourant][type].tempsEcoule()) {
                    jeu.change_ia_state(joueurCourant,0);
                    changeJoueur();
                    if(jeu.isIA(joueurCourant)){
                        jeu.change_ia_state(joueurCourant,1);
                    }
                    update_buttons();
                    iu.reset_waiting();
                    if(jeu.estTermine()){
                        Audio.play_sound("GameOver");
                        iu.Win_message();
                        Configuration.instance().logger().info("Partie termin??e");
                    }
                }
                else {
                    // Sinon on indique au joueur qui ne r??agit pas au temps (humain) qu'on l'attend.
                    System.out.println("On vous attend, joueur " + joueurs[joueurCourant][type].num());
                    decompte = lenteurAttente;
                    iu.update_waiting();
                    if (!jeu.isIA(joueurCourant)&&!timer) {
                        timer=true;
                        iu.proposer_coup();
                    }
                }
            } else {
                decompte--;
                if(decompte%20==0){
                    iu.update_waiting();
                }
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
            case "aff_voisins":
                iu.aff_voisin();
                break;
            case "transparency":
                iu.aff_transparence();
                break;
            case "tourFinie":
                iu.aff_tourFinie();
                break;
            case "suggestion":
                iu.suggestion();
                break;
            case "fullScreen":
                iu.fullscreen();
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
                joueurCourant=jeu.plateau.tourJoueur;
                if(jeu.isIA(joueurCourant)){
                    jeu.change_ia_state(joueurCourant,1);
                }
                break;
            default:
                Configuration.instance().logger().severe("Commande inconnue");
                System.exit(1);
        }
    }

}
