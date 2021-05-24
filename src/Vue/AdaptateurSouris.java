package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {
    CollecteurEvenements controle;
    PlateauGraphique plateau_graphique;

    AdaptateurSouris(PlateauGraphique pg, CollecteurEvenements c){
        controle = c;
        plateau_graphique = pg;
    }

    @Override
    public void mousePressed(MouseEvent e){
        int tmp_c=e.getX()- plateau_graphique.margin_x;
        int tmp_l=e.getY()- plateau_graphique.margin_y;
        int c = tmp_c / plateau_graphique.largeurCase();
        int l = tmp_l / plateau_graphique.hauteurCase();
        if(tmp_c<0||tmp_l<0){
            c=-1;
            l=-1;
        }
        controle.clicSouris(l,c);
    }

}
