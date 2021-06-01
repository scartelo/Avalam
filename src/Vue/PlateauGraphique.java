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
import Structures.Couple;
import Structures.Sequence;

/*
Classe permettant de dessiner la grille et le score
*/
public class PlateauGraphique extends JComponent implements Observateur {
    int largeur, hauteur, largeurCase, hauteurCase, margin_x, margin_y, margin_x_score, margin_y_score, largeurScore, hauteurScore, largeurNom, hauteurNom, hauteur_texte1, hauteur_texte2;
    Graphics2D drawable;
    Jeu jeu;
    PlateauDeJeu plateau;
    ImageCharge couronne;
    public String Col_J1;
    public String Col_J2;
    static String waiting = "ATTENTE_IA";
    static int cnt_waiting = -1;
    public boolean aff_voisins;
    public int t_x,t_y,offsetX,offsetY,cellX,cellY,ori_x,ori_y;

    public PlateauGraphique(Jeu j) {
        jeu = j;
        plateau = jeu.plateau;
        aff_voisins = false;
        couronne = new ImageCharge(ImageCharge.charge("Images/couronne.png"));
        if (jeu.couleur) { // true si dans l'ordre "normal" ; false si inversé
            Col_J1 = "CouleurJ1";
            Col_J2 = "CouleurJ2";
        } else {
            Col_J2 = "CouleurJ1";
            Col_J1 = "CouleurJ2";
        }
        t_x = 0;
        t_y = 0;
        offsetX = 0;
        offsetY = 0;

    }
    @Override
    public void paintComponent(Graphics graphics) {
        drawable = (Graphics2D) graphics;
        largeur = getSize().width;
        hauteur = getSize().height;
        largeurScore = largeur / 2;
        hauteurScore = hauteur / 5;
        //hauteur = hauteur - hauteurScore;
        largeurCase = largeur / plateau.colonnes();
        hauteurCase = hauteur / plateau.lignes();
        //largeurCase = hauteurCase = Math.min(largeurCase, hauteurCase);
        margin_x = largeur/2; // A calculer
        margin_y = (hauteur - (hauteurCase * plateau.lignes())) / 2;
        hauteurNom = 0;
        largeurNom = 0;
        drawable.clearRect(0, 0, largeur, hauteur);
        hauteur_texte1 = hauteur + (hauteurScore / 6) + 10;
        hauteur_texte2 = hauteur + hauteurScore / 6 * 4;

        ori_x = margin_x / largeurCase;
        ori_y = margin_y / hauteurCase;

        //tracerGrille();
        tracerGrille_iso();
        //tracerScore();
        //tracer_Surbri_ia();
        //tracer_Surbri();
        tracerString(new Couleur("CouleurNbPion"),0,20,"x :"+t_x+"   y :"+t_y);
        tracerString(new Couleur("CouleurNbPion"),0,40,"OffX :"+offsetX+"   OffY :"+offsetY);
        tracerString(new Couleur("CouleurNbPion"),0,60,"SCREEN_W :"+largeur+"   SCREEN_H :"+hauteur);


    }
    void affichage(int x, int y){
        t_x=x;
        t_y=y;
    }
    void tracerImage(ImageCharge img, int x, int y, int lC, int hC) {
        drawable.drawImage(img.image(), x, y, lC, hC, null);
    }

    void tracerScore() {
        tracerCarre(new Couleur("CouleurScore"), 0, hauteur, largeur, hauteurScore,true);
        String j1 = jeu.nom_j1;
        String j2 = jeu.nom_j2;

        int w1 = tracerString(new Couleur("CouleurNbPion"), 5, hauteur_texte1, j1);


        int w2 = tracerString(new Couleur("CouleurNbPion"), 5, hauteur_texte2, j2);
        if (jeu.plateau.tourJoueur == 0) {
            tracerFleche(new Couleur(Col_J1), 5, hauteur_texte1 - hauteurScore / 12, w1, hauteurScore / 8);
            if (jeu.IA1_ref == 1) {
                tracerString(new Couleur("CouleurNbPion"), w1 + hauteurScore / 4, hauteur_texte1, waiting);
            }
        } else {
            tracerFleche(new Couleur(Col_J2), 5, hauteur_texte2 - hauteurScore / 12, w2, hauteurScore / 8);
            if (jeu.IA2_ref == 1) {
                tracerString(new Couleur("CouleurNbPion"), w2 + hauteurScore / 4, hauteur_texte2, waiting);
            }
        }

        int margin_score_j1 = 0;
        if (hauteur > 500) {
            margin_score_j1 = -1 * (hauteur / 100);
        }

        for (int i = 0; i < jeu.plateau.scoreJ1(); i++) {

            tracerCercle(new Couleur(Col_J1), i * ((hauteurScore / 6) + 2) + 10, hauteur + ((hauteurScore / 6) * 2) + margin_score_j1, (hauteurScore / 6), (hauteurScore / 6));
        }
        for (int i = 0; i < jeu.plateau.scoreJ2(); i++) {
            tracerCercle(new Couleur(Col_J2), i * ((hauteurScore / 6) + 2) + 10, (int) (hauteur + ((hauteurScore / 6) * 4.5)), hauteurScore / 6, hauteurScore / 6);
        }
    }

    void tracer_Surbri_ia() {
        for (int i = 0; i < plateau.lignes(); i++) {
            for (int j = 0; j < plateau.colonnes(); j++) {
                if (plateau.tour(i, j).est_select_ia()) {
                    int x = (j * hauteurCase) + margin_x;
                    int y = (i * largeurCase) + margin_y;
                    tracerSurbri(new Couleur("CouleurSubrillanceIA"), x + 4, y + 4, largeurCase - 6, hauteurCase - 6);
                }
            }
        }
    }

    private void tracerFleche(Couleur c, int x, int y, int w, int h) {

        Polygon arrowHead = new Polygon();

        arrowHead.addPoint(x + w, y);
        arrowHead.addPoint(x + w + h, y + h / 2);
        arrowHead.addPoint(x + w + h, y - h / 2);

        drawable.setColor(c.couleur());

        drawable.fill(arrowHead);
        drawable.setStroke(new BasicStroke(1));
        drawable.setColor(Color.BLACK);
        drawable.draw(arrowHead);
    }

    void tracerDiamond(int x, int y, int l, int h,boolean fill) {
        int x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = x;
        y1 = y + h / 2;
        x2 = x1 + l / 2;
        y2 = y;
        x3 = x + l;
        y3 = y1;
        x4 = x2;
        y4 = y + h;
        drawable.drawLine(x1, y1, x2, y2);
        drawable.drawLine(x2, y2, x3, y3);
        drawable.drawLine(x3, y3, x4, y4);
        drawable.drawLine(x4, y4, x1, y1);
        int px[] = {x1, x2, x3, x4};
        int py[] = {y1, y2, y3, y4};
        if(fill)
            drawable.fillPolygon(px,py,4);
    }

    public boolean in_Triangle( int x1, int y1, int x2, int y2, int x3, int y3, int x, int y){
        int denominator = ((y2 - y3)*(x1 - x3) + (x3 - x2)*(y1 - y3));
        int a =((y2 - y3)*(x - x3) + (x3 - x2)*(y - y3)) / denominator;
        int b=((y3 - y1)*(x - x3) + (x1 - x3)*(y - y3)) / denominator;
        int c=1 - a - b;
        return 0 <= a && a <= 1 && 0 <= b && b <= 1 && 0 <= c && c <= 1;
    }
    public int[] to_grid(int x,int y){
        int i,j;
        cellX=x/largeurCase;
        cellY=y/hauteurCase;
        i=(cellY-ori_y)+(cellX-ori_x);
        j=(cellY-ori_y)-(cellX-ori_x);
        offsetX=x%largeurCase;
        offsetY=y%hauteurCase;
        int px[] = {0, largeurCase/2, largeurCase, largeurCase/2};
        int py[] = {hauteurCase/2, 0, hauteurCase/2, hauteurCase};
        Polygon diamond= new Polygon(px,py,4);
        if(!diamond.contains(offsetX,offsetY)){              /// Fonction qui permet de regarder si le point selec est dans le losange compris dans le rectangle de traçage
            System.out.println("NOT IN POLY");
            if(offsetX<largeurCase/2 && offsetY<hauteurCase/2){ /// Regarde dans quel coin du rectangle le point est situé
                i-=1;
                System.out.println("IN TOP LEFT");
            }else if(offsetX<largeurCase/2 && offsetY>hauteurCase/2){ // vert
                j+=1;
                System.out.println("BOTTOM LEFT");

            }else if(offsetX>largeurCase/2 && offsetY<hauteurCase/2){ //bleu
                j-=1;
                System.out.println("BOTTOM RIGHT");

            }else if(offsetX>largeurCase/2 && offsetY > hauteurCase/2){
                i+=1;
                System.out.println("TOP RIGHT");

            }
        }
        return new int[]{i,j};
    }


    public int[] to_iso(int x,int y){
        int i=(ori_x*largeurCase)+(x-y)*(largeurCase/2);
        int j=(ori_y*hauteurCase)+(x+y)*(hauteurCase/2);
        return new int[]{i,j};
    }
    void tracerGrille_iso(){
        tracerCarre(new Couleur("CouleurFond"), 0, 0, largeur, hauteur,true);
        //bord gauche / droite
        for (int j = 0; j< plateau.colonnes();  j++){
            for (int i = 0; i < plateau.lignes(); i++) {
                if(!plateau.grille()[i][j].estInnocupable()){
                    int[] coord= to_iso(i,j);
                    int x=coord[0];
                    int y=coord[1];
                    boolean fill=false;
                    fill=true;
                    if(plateau.grille()[i][j].estJouable()){
                        if(plateau.grille()[i][j].sommetTour()==0){
                            drawable.setColor(Color.RED);
                        }else{
                            drawable.setColor(Color.YELLOW);
                        }
                    }else{
                        drawable.setColor(Color.BLACK);
                    }
                tracerDiamond(x,y,largeurCase,hauteurCase,fill);
                drawable.setColor(Color.BLACK);
                tracerNbPion(new Couleur("CouleurNbPion"), x+(largeurCase/14), y+(hauteurCase/5), largeurCase, hauteurCase,plateau.grille()[i][j].nbPion());

                }
            }
        }
    }
    void tracerGrille(){
        tracerCarre(new Couleur("CouleurFond"), 0, 0, largeur, hauteur,true);
        //bord gauche / droite
        tracerCarre(new Couleur("CouleurBord"), 0, 0, margin_x, hauteur,true);
        tracerCarre(new Couleur("CouleurBord"), margin_x+(plateau.lignes()*hauteurCase)+1, 0, largeur, hauteur,true);
        //bord haut / bas
        tracerCarre(new Couleur("CouleurBord"), 0, 0, largeur, margin_y,true);
        tracerCarre(new Couleur("CouleurBord"), 0, margin_y+(plateau.colonnes()*largeurCase), largeur, margin_y,true);

        for (int i = 0; i< plateau.lignes(); i++){
            for (int j = 0; j < plateau.colonnes(); j++){
                int x = (j * hauteurCase)+margin_x;
                int y = (i * largeurCase)+margin_y;
                Tour T = plateau.tour(i,j);
                int s=T.sommetTour();
                int n=T.nbPion();


                if(!T.estInnocupable()) {
                    tracerCarre(new Couleur("CouleurPlateau"), x+2, y+2, largeurCase-2, hauteurCase-2,true);
                    if(T.estVide()){
                        tracerCercle(new Couleur("CouleurVide"), x+4, y+4, largeurCase-6, hauteurCase-6);
                    }
                    else if (s == 0) {
                        //tracerImage(PionJaune, x, y, largeurCase, hauteurCase);
                        tracerCercle(new Couleur(Col_J1), x+4, y+4, largeurCase-6, hauteurCase-6);

                    } else if (s == 1) {
                        tracerCercle(new Couleur(Col_J2), x+4, y+4, largeurCase-6, hauteurCase-6);
                    }
                        if(!T.estVide()&&!plateau.pasDeplacable(plateau.tour(i,j))){
                            tracerNbPion(new Couleur("CouleurNbPion"), x+(largeurCase/14), y+(hauteurCase/5), largeurCase, hauteurCase,n);
                        }else if(!T.estInnocupable() && ! T.estVide()){

                        tracerImage(couronne, x+10, y+5, largeurCase-15, hauteurCase-15);
                        }
                    /*if (T.setEstSelectionee()){

                    }*/
                }
            }
        }
    }
    int tracerString(Couleur c, int x, int y,String s){
        Font font = new Font(Configuration.instance().lis("FontScore"),Font.BOLD,hauteurScore/6);
        drawable.setFont(font);
        drawable.setColor(c.couleur());

        drawable.drawString(s,x,y);
        return drawable.getFontMetrics().stringWidth(s);
    }
    void tracerSurbri(Couleur c, int x, int y, int lc, int hc){
        drawable.setStroke(new BasicStroke(3));
        drawable.setColor(c.couleur());
        drawable.drawOval(x,y,lc, hc);
    }
    void tracerNbPion(Couleur c, int x, int y, int lc, int hc,int nbPion){
        Font font = new Font(Configuration.instance().lis("FontPion"),Font.PLAIN,hc/2);
        drawable.setFont(font);
        drawable.setColor(c.couleur());
        drawable.drawString(String.valueOf(nbPion),x+(lc/3),y+(hc/2));
    }
    void tracerCarre(Couleur c, int x, int y, int lc, int hc,boolean fill){
        drawable.setStroke(new BasicStroke(1));
        drawable.setColor(c.couleur());
        if(fill) {
            drawable.fillRect(x, y, lc, hc);
        }
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
