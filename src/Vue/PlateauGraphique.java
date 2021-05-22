package Vue;

import Global.Configuration;
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
        tracerCarre(new Couleur("CouleurFond"), 0, 0, largeur, hauteur);
        for (int i = 0; i< plateau.lignes(); i++){
            for (int j = 0; j < plateau.colonnes(); j++){
                int x = j * hauteurCase;
                int y = i * largeurCase;
                Tour T = plateau.tour(i,j);
                int s=T.sommetTour();
                int n=T.nbPion();


                if(!T.estInnocupable()) {
                    tracerCarre(new Couleur("CouleurOutline"), x, y, largeurCase, hauteurCase);
                    tracerCarre(new Couleur("CouleurPlateau"), x+2, y+2, largeurCase-2, hauteurCase-2);
                    if(T.estVide()){
                        tracerCercle(new Couleur("CouleurVide"), x+2, y+2, largeurCase-2, hauteurCase-2);
                    }
                    else if (s == 0) {
                        //tracerImage(PionJaune, x, y, largeurCase, hauteurCase);
                        tracerCercle(new Couleur("CouleurJ1"), x+2, y+2, largeurCase-2, hauteurCase-2);
                        tracerNbPion(new Couleur("CouleurNbPion"), x, y, largeurCase, hauteurCase,n);

                    } else if (s == 1) {
                        tracerCercle(new Couleur("CouleurJ2"), x+2, y+2, largeurCase-2, hauteurCase-2);
                        tracerNbPion(new Couleur("CouleurNbPion"), x, y, largeurCase, hauteurCase,n);
                    }
                    if (plateau.x1()!=-1){
                        tracerSurbri(new Couleur("CouleurSubrillance"),plateau.y1()*largeurCase , plateau.x1()*hauteurCase, largeurCase-1, hauteurCase-1);

                    }
                }
            }
        }
    }

    void tracerSurbri(Couleur c, int x, int y, int lc, int hc){
        drawable.setStroke(new BasicStroke(3));
        drawable.setColor(c.couleur());
        drawable.drawOval(x,y,lc, hc);
    }
    void tracerNbPion(Couleur c, int x, int y, int lc, int hc,int nbPion){
        Font font = new Font("Comic Sans MS",Font.BOLD,hc/2);
        drawable.setFont(font);
        drawable.setColor(c.couleur());
        drawable.drawString(String.valueOf(nbPion),x+(lc/3),y+(hc/2));
    }
    void tracerCarre(Couleur c, int x, int y, int lc, int hc){
        drawable.setStroke(new BasicStroke(10));
        drawable.setColor(c.couleur());
        drawable.fillRect(x,y,lc, hc);
    }
    void tracerCercle(Couleur c, int x, int y, int lc, int hc){
        drawable.setStroke(new BasicStroke(10));
        drawable.setColor(c.couleur());
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
