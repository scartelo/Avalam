package Vue;

import Moteur.PlateauDeJeu;
import Moteur.Tour;
import javax.swing.*;
import java.awt.*;

public class PlateauGraphique extends JComponent{
    int largeur, hauteur, largeurCase, hauteurCase;
    Graphics2D drawable;
    PlateauDeJeu plateau;


    public PlateauGraphique(PlateauDeJeu p){
        plateau = p;

    }

    @Override
    public void paintComponent(Graphics graphics){
        drawable = (Graphics2D) graphics;
        largeur = getSize().width;
        hauteur = getSize().height;
        largeurCase = largeur / plateau.colonnes();
        hauteurCase = hauteur / plateau.lignes();

        largeurCase = hauteurCase = Math.min(largeurCase, hauteurCase);

        drawable.clearRect(0,0, largeur, hauteur);

        tracerGrille();
    }

    void tracerGrille(){
        for (int i = 0; i< plateau.lignes(); i++){
            for (int j = 0; j < plateau.colonnes(); j++){
                int x = j * largeurCase;
                int y = i * hauteurCase;
                Tour T = plateau.tour(i,j);
                int s=T.sommetTour();
                int n=T.nbPion();
                tracerCarre(new Color(0x090700), x, y, largeurCase, hauteurCase);
                if(n!=0) {
                    if (s == 0) {
                        //tracerImage(PionJaune, x, y, largeurCase, hauteurCase);
                        tracerCercle(new Color(0xEFBC05), x, y, largeurCase, hauteurCase);
                    } else if (s == 1) {
                        tracerCercle(new Color(0xEF0521), x, y, largeurCase, hauteurCase);
                    }
                }
            }
        }
    }
    void tracerCarre(Color c, int x, int y, int lc, int hc){
        drawable.setStroke(new BasicStroke(10));
        drawable.setColor(c);
        drawable.fillRect(x,y,lc, hc);
    }
    void tracerCercle(Color c, int x, int y, int lc, int hc){
        drawable.setStroke(new BasicStroke(10));
        drawable.setColor(c);
        drawable.fillOval(x,y,lc, hc);
    }
    public int largeurCase(){
        return largeurCase;
    }

    public int hauteurCase(){
        return hauteurCase;
    }

    //@Override
    public void metAJour() {
        repaint();
    }

}
