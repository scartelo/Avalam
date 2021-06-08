package Vue;

import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;

public class EtatPartie extends JComponent implements Observateur {
    int scoreJ1, scoreJ2;
    int tourJoueur;
    JLabel score;
    JLabel joueur1;
    JLabel joueur2;
    JLabel tourJ1;
    JLabel tourJ2;
    JToggleButton iaJ1, iaJ2;
    int xposDebutScoreJ1, xposDebutScoreJ2, yposDebutScoreJ1, yposDebutScoreJ2;

    private Graphics2D graphics2D;

    EtatPartie(){
        scoreJ1 = 5;
        scoreJ2 = 2;
        tourJoueur = 0;
        score = createLabel("Score");
        joueur1 = createLabel("Joueur 1");
        joueur2 = createLabel("Joueur 2");
        //tourJ1 = createLabel("X");
        //tourJ2 = createLabel("");
    }

    @Override
    public void paintComponent(Graphics g){
        graphics2D = (Graphics2D) g;
        graphics2D.clearRect(0,0, getWidth(), getHeight());

        miseAJourScore();
        miseAJourTour();

    }

    private void miseAJourTour() {
    }

    private void miseAJourScore() {
        int lc, hc;
        lc = hc = getWidth()/90;
        for (int i=0; i<scoreJ1; i++){
            tracerCercle(new Couleur("CouleurJ1"), getX()*i,getY()+5, lc, hc );
        }
        for (int i=0; i<scoreJ2; i++){
            tracerCercle(new Couleur("CouleurJ2"), getX()*i, getY()+20, lc, hc);
        }

    }

    void tracerCercle(Couleur c, int x, int y, int lc, int hc){
        graphics2D.setStroke(new BasicStroke(10));
        graphics2D.setColor(c.couleur());
        graphics2D.fillOval(x,y,lc, hc);
    }
    private JLabel createLabel(String s) {
        JLabel lab = new JLabel(s);
        lab.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lab;
    }
        @Override
    public void metAJour() {

    }
}
