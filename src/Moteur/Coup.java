package Moteur;
/*
Un coup est définit par une tour se rajoutant à une autre tour
Cette classe est utilisée pour la gestion d'historique
*/
public class Coup {
    Tour src;
    Tour dst;

    public Coup(Tour source,Tour dest){
        src=source;
        dst=dest;
    }
}
