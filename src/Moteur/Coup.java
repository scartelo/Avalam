package Moteur;
/*
Un coup est définit par une tour se rajoutant à une autre tour
Cette classe est utilisée pour la gestion d'historique
*/
public class Coup {
    int num; // numéro du joeur jouant le coup
    Tour src;
    Tour dest;

    public Coup(){

    }
    public Coup(int n, Tour source,Tour dst){
        num = n;
        src=source;
        dest =dst;
    }
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

    public int num(){
        return num;
    }
}
