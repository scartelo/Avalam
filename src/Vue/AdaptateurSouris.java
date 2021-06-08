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
        int l,c;
        int []coord = plateau_graphique.to_grid(e.getX(),e.getY());
        c=coord[1];
        l=coord[0];
        if(c<0||l<0){
            c=-1;
            l=-1;
        }
        controle.clicSouris(l,c);
    }

}
