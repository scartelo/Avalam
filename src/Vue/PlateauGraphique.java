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
import Structures.Iterateur;
import Structures.Sequence;

/*
Classe permettant de dessiner la grille et le score
*/
public class PlateauGraphique extends JComponent implements Observateur {
    public boolean aff_tourFinie;
    int largeur, hauteur, largeurCase, hauteurCase, margin_x, margin_y, margin_x_score, margin_y_score, largeurScore, hauteurScore, largeurNom, hauteurNom, hauteur_texte1, hauteur_texte2;
    Graphics2D drawable;
    Jeu jeu;
    PlateauDeJeu plateau;
    ImageCharge border;
    public String Col_J1;
    public String Col_J2;
    static String waiting = "ATTENTE_IA";
    static int cnt_waiting = -1;
    public boolean aff_voisins,transparency;
    public int t_x,t_y,offsetX,offsetY,cellX,cellY,ori_x,ori_y;
    private float alpha;

    public PlateauGraphique(Jeu j) {
        jeu = j;
        plateau = jeu.plateau;
        aff_voisins = false;
        transparency=false;
        aff_tourFinie=true;
        //border = new ImageCharge(ImageCharge.charge("Images/border.png"));
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
        largeurScore = largeur / 3;
        hauteurScore = (int)(hauteurCase*1.5)-4;
        //hauteur = hauteur - hauteurScore;
        largeurCase = largeur / plateau.colonnes();
        hauteurCase = hauteur / plateau.lignes();
        //largeurCase = hauteurCase = Math.min(largeurCase, hauteurCase);
        margin_x = largeur/2; // A calculer
        margin_y = (hauteur - (hauteurCase * plateau.lignes())) / 2;
        hauteurNom = 0;
        largeurNom = 0;
        drawable.clearRect(0, 0, largeur, hauteur);
        hauteur_texte1 = (hauteurScore / 6) + 10;
        hauteur_texte2 = hauteurScore / 6 * 4;

        ori_x = margin_x / largeurCase;
        ori_y = margin_y / hauteurCase;
        //tracerGrille();
        //tracer_Surbri_ia();
        //tracer_Surbri();
        //Fond
        tracerCarre(new Couleur("CouleurFond"), 0, 0, largeur, hauteur,true,false);
        drawable.setColor(new Couleur("CouleurJeu").couleur());
        // bord de l'image
        //tracerImage(border,0,0,largeur,hauteur);

        //Plateau de jeu
        tracerDiamond( ori_x-(largeurCase/2), ori_y-(hauteurCase/2), largeurCase*(plateau.lignes()+1), hauteurCase*(plateau.colonnes()+1),20,true);
        tracerScore();
        tracerGrille_iso();
        tracer_Turn();
        /*tracerString(new Couleur("CouleurNbPion"),0,20,"x :"+t_x+"   y :"+t_y);
        tracerString(new Couleur("CouleurNbPion"),0,40,"OffX :"+offsetX+"   OffY :"+offsetY);
        */
        //tracerString(new Couleur("CouleurNbPion"),0,60,"SCREEN_W :"+largeur+"   SCREEN_H :"+hauteur,40);

    }
    void tracer_Turn(){
        String text ="";
        String j1 = jeu.nom_j1;
        String j2 = jeu.nom_j2;
        Couleur c;
        if(plateau.tourJoueur==0){
            text+=j1;
            c= new Couleur(Col_J1);
        }else{
            text+=j2;
            c= new Couleur(Col_J2);
        }
        tracerCarre(c, -10, -10, largeur/6, 40+15+30+((hauteur/2)/10),true,true);
        Font font = new Font(Configuration.instance().lis("FontScore"),Font.BOLD,(hauteur/2)/10);
        drawable.setFont(font);
        int w =drawable.getFontMetrics().stringWidth("Au tour de");
        tracerString(new Couleur("CouleurNbPion"),((largeur/6)-w-10)/2,(hauteur/2)/10,"Au tour de",(hauteur/2)/10);
        font = new Font(Configuration.instance().lis("FontScore"),Font.BOLD,(hauteur/2)/8);
        drawable.setFont(font);
        w =drawable.getFontMetrics().stringWidth(text);
        tracerString(new Couleur("CouleurNbPion"),((largeur/6)-w-10)/2,40+15+((hauteur/2)/10),text,(hauteur/2)/8);

    }
    void affichage(int x, int y){
        t_x=x;
        t_y=y;
    }
    void tracerImage(ImageCharge img, int x, int y, int lC, int hC) {
        drawable.drawImage(img.image(), x, y, lC, hC, null);
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
            if(offsetX<largeurCase/2 && offsetY<hauteurCase/2){ /// Regarde dans quel coin du rectangle le point est situé
                i-=1;
            }else if(offsetX<largeurCase/2 && offsetY>hauteurCase/2){ // vert
                j+=1;

            }else if(offsetX>largeurCase/2 && offsetY<hauteurCase/2){ //bleu
                j-=1;

            }else if(offsetX>largeurCase/2 && offsetY > hauteurCase/2){
                i+=1;

            }
        }
        return new int[]{i,j};
    }
    public void marquer_voisins(){
            int i_sel=-1;
            int j_sel=-1;
            for(int i=0;i<plateau.lignes();i++){
                for(int j=0;j<plateau.colonnes();j++){
                    plateau.grille()[i][j].voisin = false;
                    if(plateau.grille()[i][j].estSelectionee()){
                        i_sel=i;
                        j_sel=j;
                    }
                }
            }
            if(i_sel!=-1 && j_sel!=-1) {
                Tour T = plateau.tour(i_sel, j_sel);
                Sequence toursVoisines = plateau.voisins(T.ligne(), T.colonne());
                Iterateur it = toursVoisines.iterateur();
                while(it.aProchain()){
                    Tour voisine = (Tour) it.prochain();
                    int i_v = voisine.ligne();
                    int j_v = voisine.colonne();
                    if (T.estDeplacable(plateau.tour(i_v, j_v))) {
                        plateau.grille()[i_v][j_v].voisin = true;
                    }
                }
            }
    }
    public int[] to_iso(int x,int y){
        int i=(ori_x*largeurCase)+(x-y)*(largeurCase/2);
        int j=(ori_y*hauteurCase)+(x+y)*(hauteurCase/2);
        return new int[]{i,j};
    }
    void tracerScore(){
        //Score
        tracerCarre(new Couleur("CouleurScore"), largeur/3, 0, largeurScore, hauteurScore,true,true);
        String j1 = jeu.nom_j1;
        String j2 = jeu.nom_j2;

        int w1=tracerString(new Couleur("CouleurNbPion"),3+largeur/3, 3+hauteur_texte1/2,j1,(hauteurScore/6));


        int w2=tracerString(new Couleur("CouleurNbPion"),3+largeur/3, hauteur_texte2,j2,(hauteurScore/6));
        if(jeu.plateau.tourJoueur==0){
            tracerFleche(new Couleur(Col_J1),10+(largeur/3), 3+(hauteur_texte1/3),w1,hauteurScore/8);
            if(jeu.IA1_ref==1) {
                tracerString(new Couleur("CouleurNbPion"),w1+(largeur/3)+10+hauteurScore/8,3+ hauteur_texte1/2,waiting,(hauteurScore/6));
            }
        }
        else{
            tracerFleche(new Couleur(Col_J2),10+(largeur/3), hauteur_texte2-hauteurScore/12,w2,hauteurScore/8);
            if(jeu.IA2_ref==1) {
                tracerString(new Couleur("CouleurNbPion"),w2+(largeur/3)+10+hauteurScore/8, hauteur_texte2,waiting,(hauteurScore/6));
            }
        }

        int margin_score_j1=0;
        if(hauteur>500){
            margin_score_j1=-1*(hauteur/100);
        }

        for(int i=0;i<jeu.scoreJ1();i++){

            tracerCercle(new Couleur(Col_J1), (largeur/3)+i*((hauteurScore/6)+2)+10,((hauteurScore/6)*2)+margin_score_j1 , (hauteurScore/6), (hauteurScore/6));
        }
        for(int i=0;i<jeu.scoreJ2();i++){
            tracerCercle(new Couleur(Col_J2), (largeur/3)+i*(((hauteurScore/6))+2)+10,(int)(((hauteurScore/6)*4.5)) , hauteurScore/6, hauteurScore/6);
        }
    }
    void tracerGrille_iso(){
        marquer_voisins();
        for (int j = 0; j< plateau.colonnes();  j++){
            for (int i = 0; i < plateau.lignes(); i++) {
                if(!plateau.grille()[i][j].estInnocupable()){
                    int[] coord= to_iso(i,j);
                    int x=coord[0];
                    int y=coord[1];
                    boolean fill=false;
                    fill=true;
                    boolean finie=false;
                    if(plateau.grille()[i][j].estJouable() && ! plateau.estIsole(i,j)){
                        drawable.setColor(new Couleur("CouleurPlateau").couleur());
                    }else if(!plateau.grille()[i][j].estVide() ||plateau.estIsole(i,j) ){
                        if(plateau.grille()[i][j].sommetTour()==0){
                            drawable.setColor(new Couleur(Col_J1).couleur());
                        }else{
                            drawable.setColor(new Couleur(Col_J2).couleur());
                        }
                        finie=true;
                    }
                    if(plateau.grille()[i][j].est_select_ia()){
                        drawable.setColor(new Couleur("CouleurSubrillanceIA").couleur());
                    }
                    if( aff_voisins&&plateau.grille()[i][j].marqueVoisin()){
                        drawable.setColor(new Couleur("CouleurSurbriVoisin").couleur());
                    }
                    if(plateau.grille()[i][j].estSelectionee()){
                        drawable.setColor(Color.BLUE);
                    }
                    if(!plateau.grille()[i][j].estVide()||plateau.grille()[i][j].est_select_ia()){
                        tracerDiamond(x,y,largeurCase,hauteurCase,10,fill);
                        drawable.setColor(Color.BLACK);
                        if(!finie || (finie && aff_tourFinie)){
                        tracerTour(x,y,10,10,plateau.grille()[i][j]);}
                    }
                }
            }
        }
    }
    void tracerPion(Couleur c, int x, int y, int lc, int hc){
        int hauteurPion =hc/3;


        drawable.setColor(c.couleur());
        drawable.fillOval(x, y+hauteurPion, lc, hc);
        drawable.setColor(Color.black);
        drawable.drawOval(x, y+hauteurPion, lc, hc);

        drawable.setColor(c.couleur());

        if(!transparency){
            drawable.fillRect(x,y+hc/2,lc,hauteurPion);
        }

        drawable.setColor(Color.black);
        drawable.drawLine(x, y+hc/2, x, y+(hc/2)+hauteurPion);
        drawable.drawLine(x+lc, y+hc/2, x + lc, y+(hc/2)+hauteurPion);

        drawable.setColor(c.couleur());
        drawable.fillOval(x,y, lc, hc);
        drawable.setColor(Color.black);
        drawable.drawOval(x,y, lc, hc);
    }
    void tracerTour(int x, int y, int lc, int hc,Tour T){
        if(transparency){
            drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)0.2));
        }
        for(int i=1;i<=T.nbPion();i++){
            if (T.niemePion(i) == 0) {
                tracerPion(new Couleur(Col_J1), x + largeurCase/4, (y+4 )-((hauteurCase)/6)*(i-1), largeurCase/2, hauteurCase/2);
            } else if (T.niemePion(i) == 1) {
                tracerPion(new Couleur(Col_J2), x+largeurCase/4, (y+4)-((hauteurCase)/6)*(i-1), largeurCase/2, hauteurCase/2);
            }
        }
        drawable.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)1));
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

    void tracerDiamond(int x, int y, int l, int h,int bord_h,boolean fill) {
        int x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = x;
        y1 = y + h / 2;
        x2 = x1 + l / 2;
        y2 = y;
        x3 = x + l;
        y3 = y1;
        x4 = x2;
        y4 = y + h;

        int px[] = {x1, x2, x3, x4};
        int py[] = {y1, y2, y3, y4};
        if(fill)
            drawable.fillPolygon(px,py,4);
            drawable.fillPolygon(new int[]{x4,x3,x3,x4},new int[]{y4,y3,y3+bord_h,y4+bord_h},4);
            drawable.fillPolygon(new int[]{x1,x4,x4,x1},new int[]{y1,y4,y4+bord_h,y1+bord_h},4);
            //face supérieur de la tuile
        drawable.setColor(Color.BLACK);
        drawable.drawLine(x1, y1, x2, y2);
        drawable.drawLine(x2, y2, x3, y3);
        drawable.drawLine(x3, y3, x4, y4);
        drawable.drawLine(x4, y4, x1, y1);
        //3D côté gauche
        drawable.drawLine(x1,y1,x1,y1+bord_h);
        drawable.drawLine(x4,y4,x4,y4+bord_h);
        drawable.drawLine(x1,y1+bord_h,x4,y4+bord_h);
        //3D côté droit
        drawable.drawLine(x4,y4,x4,y4+bord_h);
        drawable.drawLine(x3,y3,x3,y3+bord_h);
        drawable.drawLine(x3,y3+bord_h,x4,y4+bord_h);
    }

    int tracerString(Couleur c, int x, int y,String s,int hauteur_texte){
        Font font = new Font(Configuration.instance().lis("FontScore"),Font.BOLD,hauteur_texte);
        drawable.setFont(font);
        drawable.setColor(c.couleur());

        drawable.drawString(s,x,y);
        return drawable.getFontMetrics().stringWidth(s);
    }

    void tracerNbPion(Couleur c, int x, int y, int lc, int hc,int nbPion){
        Font font = new Font(Configuration.instance().lis("FontPion"),Font.PLAIN,hc/2);
        drawable.setFont(font);
        drawable.setColor(c.couleur());
        drawable.drawString(String.valueOf(nbPion),x+(lc/3),y+(hc/2));
    }
    void tracerCarre(Couleur c, int x, int y, int lc, int hc,boolean fill,boolean rounded){
        drawable.setStroke(new BasicStroke(1));
        drawable.setColor(c.couleur());
        if(!rounded){
            if(fill) {
                drawable.fillRect(x, y, lc, hc);
            }
            drawable.setColor(new Couleur("CouleurOutline").couleur());
            drawable.drawRect(x,y,lc, hc);
        }else{
            int arc_W=15;
            int arc_H=15;
            if(fill) {
                drawable.fillRoundRect(x, y, lc, hc,arc_W,arc_H);

            }
            drawable.setColor(new Couleur("CouleurOutline").couleur());
            drawable.drawRoundRect(x,y,lc, hc,arc_W,arc_H);
        }
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
