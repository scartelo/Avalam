package Controleur;

import Moteur.Jeu;
import Moteur.Tour;

public abstract class Joueur {
        Jeu jeu;
        int num;//
        boolean aSelectionneTour;
        boolean aDeplaceTour;
        Tour tourSelectionnee = null;
        // Le joueur connait son numéro, cela lui permet d'inspecter le plateau en
        // sachant
        // repérer ses pions et évaluer où il en est
        Joueur(int n, Jeu j) {
            num = n;
            jeu = j;
            aSelectionneTour = false;
            aDeplaceTour = false;
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
        boolean joue(int l , int c ) {
            return false;
        }

    }
