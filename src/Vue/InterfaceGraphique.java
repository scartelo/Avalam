package Vue;

import Moteur.PlateauDeJeu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceGraphique implements Runnable{
    private JFrame frame;
    PlateauGraphique plateauGraphique;
    public InterfaceGraphique(PlateauDeJeu p){
        plateauGraphique = new PlateauGraphique(p);
    }

    @Override
    public void run() {
        frame = new JFrame("Avalam");

        //plateauGraphique.addMouseListener(new EcouteurDeSouris(jeuGraphique, controle));


        Box boxJeu= Box.createVerticalBox();
        boxJeu.add(plateauGraphique);
        boxJeu.setAlignmentY(Component.CENTER_ALIGNMENT);
        boxJeu.setPreferredSize(new Dimension(500,400));
        frame.add( boxJeu);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setVisible(true);
    }
    public void demarrer(PlateauDeJeu p){
        SwingUtilities.invokeLater(new InterfaceGraphique(p));
    }
}
