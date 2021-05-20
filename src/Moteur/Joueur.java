package Moteur;

public class Joueur {
    public int score;
    public Joueur(){
        score=0;
    }
    public void inc_score(){
        score++;
    }
    public void set_score(int s){
        score=s;
    }
}
