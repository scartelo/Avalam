package Vue;

import Global.Configuration;
import Moteur.PlateauDeJeu;
import Moteur.Tour;
import javax.swing.*;
import java.awt.*;


import Patterns.Observateur;
import Moteur.Jeu;
/*
Classe permettant de dessiner la grille et le score
*/
public class PlateauGraphique extends JComponent implements Observateur{
    int largeur, hauteur, largeurCase, hauteurCase,margin_x,margin_y,margin_x_score,margin_y_score,largeurScore,hauteurScore,largeurNom,hauteurNom;
    Graphics2D drawable;
    Jeu jeu;
    PlateauDeJeu plateau;
    ImageCharge couronne;

    public PlateauGraphique(Jeu j){
        jeu=j;
        plateau = jeu.plateau;
        couronne=new ImageCharge(ImageCharge.charge("Images/couronne.png"));
    }

    @Override
    public void paintComponent(Graphics graphics){
        drawable = (Graphics2D) graphics;
        largeur = getSize().width;
        hauteur = getSize().height;
        largeurScore=largeur/2;
        hauteurScore=hauteur/5;
        hauteur=hauteur-hauteurScore;
        largeurCase = largeur / plateau.colonnes();
        hauteurCase = hauteur / plateau.lignes();
        largeurCase = hauteurCase = Math.min(largeurCase, hauteurCase);
        margin_x=(largeur-(largeurCase*plateau.colonnes()))/2;
        margin_y=(hauteur-(hauteurCase*plateau.lignes()))/2;
        margin_x_score=200;
        margin_y_score=0;
        hauteurNom=0;
        largeurNom=0;
        drawable.clearRect(0,0, largeur, hauteur);
        tracerGrille();
        tracerScore();
    }
    void tracerImage(ImageCharge img, int x, int y, int lC, int hC){
        drawable.drawImage(img.image(), x, y, lC, hC, null);
    }

    void tracerScore(){
        tracerCarre(new Couleur("CouleurScore"), 0, hauteur, largeur,hauteurScore);
        tracerCarre(new Couleur("CouleurVide"), 0, hauteur, largeur,3 );
        String j1 = jeu.nom_j1;
        String j2 = jeu.nom_j2;
        if(jeu.plateau.tourJoueur==0){
            j1+=" X";
        }
        else{
            j2+=" X";
        }
        tracerString(new Couleur("CouleurNbPion"),0, hauteur+(hauteurScore/6)+10,j1);
        tracerString(new Couleur("CouleurNbPion"),0, hauteur+((hauteurScore/6)*4),j2);

        for(int i=0;i<jeu.plateau.scoreJ1();i++){
            tracerCercle(new Couleur("CouleurJ1"), i*(hauteurScore/6)+2,hauteur+((hauteurScore/6)*2) , (hauteurScore/6), (hauteurScore/6));
        }
        for(int i=0;i<jeu.plateau.scoreJ2();i++){
            tracerCercle(new Couleur("CouleurJ2"), i*((hauteurScore/6)+2),(hauteur+((hauteurScore/6)*5)) , hauteurScore/6, hauteurScore/6);
        }
    }
    void tracerGrille(){
        tracerCarre(new Couleur("CouleurFond"), 0, 0, largeur, hauteur);
        for (int i = 0; i< plateau.lignes(); i++){
            for (int j = 0; j < plateau.colonnes(); j++){
                int x = (j * hauteurCase)+margin_x;
                int y = (i * largeurCase)+margin_y;
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

                    } else if (s == 1) {
                        tracerCercle(new Couleur("CouleurJ2"), x+2, y+2, largeurCase-2, hauteurCase-2);
                    }
                        if(!T.estVide()&&!plateau.pasDeplacable(plateau.tour(i,j))){
                            tracerNbPion(new Couleur("CouleurNbPion"), x+(largeurCase/14), y+(hauteurCase/5), largeurCase, hauteurCase,n);
                        }else if(!T.estInnocupable() && ! T.estVide()){
                        int decal=5;
                        if(hauteur<350) {
                            decal = 7;
                        }
                        tracerImage(couronne, x+decal, y+5, largeurCase-10, hauteurCase-10);
                        }
                    if (T.marque() == Integer.parseInt(Configuration.instance().lis("CouleurSubrillance"),16)){
                        tracerSurbri(new Couleur("CouleurSubrillance"), x+2, y+2, largeurCase-2, hauteurCase-2);
                    }
                }
            }
        }
    }
    void tracerString(Couleur c, int x, int y,String s){
        Font font = new Font("Comic Sans MS",Font.BOLD,hauteurScore/6);
        drawable.setFont(font);
        drawable.setColor(c.couleur());
        drawable.drawString(s,x,y);
    }
    void tracerSurbri(Couleur c, int x, int y, int lc, int hc){
        drawable.setStroke(new BasicStroke(2));
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
        drawable.setColor(c.couleur());
        drawable.fillOval(x,y,lc, hc);
        drawable.setStroke(new BasicStroke(1));
        drawable.setColor(Color.BLACK);
        drawable.drawOval(x,y,lc,hc);
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
