package Moteur;
/*
Un coup est définit par une tour se rajoutant à une autre tour
Cette classe est utilisée pour la gestion d'historique
*/
public class Coup {
    Tour src;
    Tour dest;

    public Coup(Tour source,Tour dst){
        src=source;
        dest =dst;
    }

    public Tour src(){
        return src;
    }

    public Tour dest(){
        return dest;
    }
}
