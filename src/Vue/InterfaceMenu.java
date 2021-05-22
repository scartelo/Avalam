package Vue;

import Controleur.ControleurMediateur;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceMenu {
    private JButton LoadGame;
    private JPanel MainPanel;
    private JButton chargerUnePartieButton;
    private JButton quitterButton;
    private JButton startGame;
    private static JFrame frame;
    Jeu jeu;

    public InterfaceMenu() {
        jeu = new Jeu(new PlateauDeJeu());

        chargerUnePartieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Charger_Partie(2);
            }
        });
        quitterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.Quitter();
            }
        });
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Nouvelle_Partie();
            }
        });
    }
    public void Nouvelle_Partie(){
        ControleurMediateur controleur = new ControleurMediateur(jeu);
        InterfaceGraphique.demarrer(jeu, controleur);
        frame.setVisible(false);
    }

    public void Charger_Partie(int n_save){
        jeu.load(n_save,1);
        Nouvelle_Partie();

    }
    public static void main(String[] args) {
        frame = new JFrame("Menu");
        frame.setContentPane(new InterfaceMenu().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}