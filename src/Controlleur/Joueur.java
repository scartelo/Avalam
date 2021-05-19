package Moteur;

// Classe commune à tous les joueurs : IA ou humain

abstract class Joueur {
    PlateauDeJeu pjeu;
    int num; //
    // Le joueur connait son numéro, cela lui permet d'inspecter le plateau en
    // sachant
    // repérer ses pions et évaluer où il en est
    Joueur(int n, PlateauDeJeu p) {
        num = n;
        pjeu = p;
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
    boolean jeu(Tour dep, Tour dest ) {
        return false;
    }
}
