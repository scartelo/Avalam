package Vue;

import Moteur.PlateauDeJeu;
import Moteur.Tour;
import javax.swing.*;
import java.awt.*;


import Patterns.Observateur;
import Moteur.Jeu;

public class PlateauGraphique extends JComponent implements Observateur{
    int largeur, hauteur, largeurCase, hauteurCase;
    Graphics2D drawable;
    Jeu jeu;
    PlateauDeJeu plateau;


    public PlateauGraphique(Jeu j){
        jeu=j;
        plateau = jeu.plateau;
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
                int x = j * hauteurCase;
                int y = i * largeurCase;
                Tour T = plateau.tour(i,j);
                int s=T.sommetTour();
                int n=T.nbPion();


                tracerCarre(new Color(0x090900), x, y, largeurCase, hauteurCase);
                if(n!=0) {
                    if (s == 0) {
                        //tracerImage(PionJaune, x, y, largeurCase, hauteurCase);
                        tracerCercle(new Color(0xEFBC05), x, y, largeurCase, hauteurCase);
                        tracerNbPion(new Color(0xFCFCFC), x, y, largeurCase, hauteurCase,n);

                    } else if (s == 1) {
                        tracerCercle(new Color(0xEF0521), x, y, largeurCase, hauteurCase);
                        tracerNbPion(new Color(0xFCFCFC), x, y, largeurCase, hauteurCase,n);
                    }
                    if (plateau.x1()!=-1){
                        tracerSurbri(new Color(0xFCFCFC),plateau.y1()*largeurCase , plateau.x1()*hauteurCase, largeurCase, hauteurCase);

                    }
                }
            }
        }
    }

    void tracerSurbri(Color c, int x, int y, int lc, int hc){
        drawable.setStroke(new BasicStroke(3));
        drawable.setColor(c);
        drawable.drawOval(x,y,lc, hc);
    }
    void tracerNbPion(Color c, int x, int y, int lc, int hc,int nbPion){
        Font font = new Font("Comic Sans MS",Font.BOLD,hc/2);
        drawable.setFont(font);
        drawable.setColor(c);
        drawable.drawString(String.valueOf(nbPion),x+(lc/3),y+(hc/2));
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

    @Override
    public void metAJour() {
        repaint();
    }

}
