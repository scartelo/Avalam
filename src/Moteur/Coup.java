package Moteur;

public class Coup {
    Tour src;
    Tour dst;

    public Coup(Tour source,Tour dest){
        src=source;
        dst=dest;
    }
    public Tour get_Dest(){
        return dst;
    }
    public Tour get_Src(){
        return src;
    }
}
