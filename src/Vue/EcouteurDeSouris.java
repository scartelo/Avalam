package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EcouteurDeSouris extends MouseAdapter {
    CollecteurEvenements controle;
    PlateauGraphique plateau_graphique;

    EcouteurDeSouris( PlateauGraphique pg, CollecteurEvenements c){
        controle = c;
        plateau_graphique = pg;
    }

    @Override
    public void mousePressed(MouseEvent e){
        int c = e.getX() / plateau_graphique.largeurCase();
        int l = e.getY() / plateau_graphique.hauteurCase();
        controle.clicSouris(l,c);
    }

}