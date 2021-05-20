package Controleur;

import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Tour;

public class Joueur {
        Jeu jp;
        int num;//
        // Le joueur connait son numéro, cela lui permet d'inspecter le plateau en
        // sachant
        // repérer ses pions et évaluer où il en est
        Joueur(int n, Jeu jj) {
            num = n;
            jp = jj;
        }

        int num() {
            return num;
        }

        // Méthode appelée pour tous les joueurs une fois le temps écoulé
        // Si un joueur n'est pas concerné, il lui suffit de l'ignorer
        boolean tempsEcoule() {
            return false;
        }

        // Méthode appelée pour tous les joueurs lors d'un clic sur le plateau
        // Si un joueur n'est pas concerné, il lui suffit de l'ignorer
        boolean jeu(int l , int c ) {
            return false;
        }
    }
