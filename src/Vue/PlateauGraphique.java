package Vue;

import Global.Configuration;
import Moteur.PlateauDeJeu;
import Moteur.Tour;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import Patterns.Observateur;
import Moteur.Jeu;
/*
Classe permettant de dessiner la grille et le score
*/
public class PlateauGraphique extends JComponent implements Observateur{
    int largeur, hauteur, largeurCase, hauteurCase,margin_x,margin_y,margin_x_score,margin_y_score,largeurScore,hauteurScore,largeurNom,hauteurNom,hauteur_texte1,hauteur_texte2;
    Graphics2D drawable;
    Jeu jeu;
    PlateauDeJeu plateau;
    ImageCharge couronne;
    static String waiting="ATTENTE_IA";
    static int cnt_waiting=-1;
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
        hauteur_texte1=hauteur+(hauteurScore/6)+10;
        hauteur_texte2=hauteur+ hauteurScore/6 *4;
        tracerGrille();
        tracerScore();
    }

    void tracerImage(ImageCharge img, int x, int y, int lC, int hC){
        drawable.drawImage(img.image(), x, y, lC, hC, null);
    }

    void tracerScore(){
        tracerCarre(new Couleur("CouleurScore"), 0, hauteur, largeur,hauteurScore);
        String j1 = jeu.nom_j1;
        String j2 = jeu.nom_j2;
        if(jeu.plateau.tourJoueur==0){
            j1+=" X";
            if(jeu.IA1_ref==1) {
                j1+=waiting;
            }
        }
        else{
            j2+=" X";
            if(jeu.IA2_ref==1) {
                j2+=waiting;
            }
        }
        tracerString(new Couleur("CouleurNbPion"),5, hauteur_texte1,j1);
        tracerString(new Couleur("CouleurNbPion"),5, hauteur_texte2,j2);

        int margin_score_j1=0;
        if(hauteur>500){
            margin_score_j1=-1*(hauteur/100);
        }
        for(int i=0;i<jeu.plateau.scoreJ1();i++){
            tracerCercle(new Couleur("CouleurJ1"), i*((hauteurScore/6)+2)+10,hauteur+((hauteurScore/6)*2)+margin_score_j1 , (hauteurScore/6), (hauteurScore/6));
        }
        for(int i=0;i<jeu.plateau.scoreJ2();i++){
            tracerCercle(new Couleur("CouleurJ2"), i*((hauteurScore/6)+2)+10,(int)(hauteur+((hauteurScore/6)*4.5)) , hauteurScore/6, hauteurScore/6);
        }
    }
    void tracerGrille(){
        tracerCarre(new Couleur("CouleurFond"), 0, 0, largeur, hauteur);
        //bord gauche / droite
        tracerCarre(new Couleur("CouleurBord"), 0, 0, margin_x, hauteur);
        tracerCarre(new Couleur("CouleurBord"), margin_x+(plateau.lignes()*hauteurCase)+1, 0, largeur, hauteur);
        //bord haut / bas
        tracerCarre(new Couleur("CouleurBord"), 0, 0, largeur, margin_y);
        tracerCarre(new Couleur("CouleurBord"), 0, margin_y+(plateau.colonnes()*largeurCase), largeur, margin_y);

        for (int i = 0; i< plateau.lignes(); i++){
            for (int j = 0; j < plateau.colonnes(); j++){
                int x = (j * hauteurCase)+margin_x;
                int y = (i * largeurCase)+margin_y;
                Tour T = plateau.tour(i,j);
                int s=T.sommetTour();
                int n=T.nbPion();


                if(!T.estInnocupable()) {
                    tracerCarre(new Couleur("CouleurPlateau"), x+2, y+2, largeurCase-2, hauteurCase-2);
                    if(T.estVide()){
                        tracerCercle(new Couleur("CouleurVide"), x+4, y+4, largeurCase-6, hauteurCase-6);
                    }
                    else if (s == 0) {
                        //tracerImage(PionJaune, x, y, largeurCase, hauteurCase);
                        tracerCercle(new Couleur("CouleurJ1"), x+4, y+4, largeurCase-6, hauteurCase-6);

                    } else if (s == 1) {
                        tracerCercle(new Couleur("CouleurJ2"), x+4, y+4, largeurCase-6, hauteurCase-6);
                    }
                        if(!T.estVide()&&!plateau.pasDeplacable(plateau.tour(i,j))){
                            tracerNbPion(new Couleur("CouleurNbPion"), x+(largeurCase/14), y+(hauteurCase/5), largeurCase, hauteurCase,n);
                        }else if(!T.estInnocupable() && ! T.estVide()){

                        tracerImage(couronne, x+10, y+5, largeurCase-15, hauteurCase-15);
                        }
                    if (T.setEstSelectionee()){
                        tracerSurbri(new Couleur("CouleurSubrillance"), x+4, y+4, largeurCase-6, hauteurCase-6);
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
        drawable.setStroke(new BasicStroke(1));
        drawable.setColor(c.couleur());
        drawable.fillRect(x,y,lc, hc);
        drawable.setColor(new Couleur("CouleurOutline").couleur());
        drawable.drawRect(x,y,lc, hc);
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
    public static void reset_attente(){
        waiting=" ATTENTE_IA";
        cnt_waiting=-1;
    }
    public static void update_attente(){
        cnt_waiting+=1;
        if(cnt_waiting>2){
            reset_attente();
        }else if(cnt_waiting>=0){
            waiting+=" .";
        }
    }
}
