package Vue;

import Controleur.ControleurMediateur;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceMenu {
    private JButton StartGame;
    private JPanel MainPanel;

    public InterfaceMenu() {
        StartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Jeu jeu = new Jeu(new PlateauDeJeu());
                ControleurMediateur controleur = new ControleurMediateur(jeu);
                InterfaceGraphique.demarrer(jeu, controleur);



            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Menu");
        frame.setContentPane(new InterfaceMenu().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}