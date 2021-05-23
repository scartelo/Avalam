package Vue;

import Controleur.ControleurMediateur;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/*
Classe permettant d'afficher le contenu du menu principal
*/
public class InterfaceMenu {
    private JButton LoadGame;
    private JPanel MainPanel;
    private JButton chargerUnePartieButton;
    private JButton quitterButton;
    private JButton startGame;
    private JButton tutorielButton;
    private static JFrame frame;
    Jeu jeu;

    public InterfaceMenu() {
        jeu = new Jeu(new PlateauDeJeu());

        chargerUnePartieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Charger_Partie(1);
            }
        });
        quitterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Quitter();
            }
        });
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interfaceSelection m = new interfaceSelection();
                m.selection();
            }
        });
        tutorielButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show_tutoriel();
            }
        });
    }
    public void show_tutoriel(){
        JFrame tutoriel = new JFrame();
        tutoriel = new JFrame("Tutoriel");
        tutoriel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tutoriel.setSize(775, 500);
        tutoriel.setResizable(false);
        tutoriel.setLocationRelativeTo(null);

        final String dir = System.getProperty("user.dir");
        String home = System.getProperty("user.dir");
        home = home + File.separator + "res" + File.separator + "Images"+File.separator+"tutoriel.png";
        ImageIcon icon = new ImageIcon(home);
        JLabel label_tuto = new JLabel(icon);
        tutoriel.add(label_tuto);

        tutoriel.setVisible(true);
    }
    public void showMenu(boolean b){
        frame.setVisible(b);
    }
    public void Nouvelle_Partie(String nom_j1,String nom_j2,int ia1, int ia2, int n_ia1,int n_ia2){
        jeu.nom_j1=nom_j1;
        jeu.nom_j2=nom_j2;
        jeu.IA1=ia1;
        jeu.IA2=ia2;
        jeu.niveauIA1=n_ia1;
        jeu.niveauIA2=n_ia2;
        ControleurMediateur controleur = new ControleurMediateur(jeu);
        InterfaceGraphique.demarrer(jeu, controleur);
        frame.setVisible(false);
    }

    public void Charger_Partie(int n_save){
        int res = JOptionPane.showConfirmDialog(null,"Êtes vous sur de vouloir charger la sauvegarde ? ","Charger",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            if(jeu.load(n_save,1)){
                Nouvelle_Partie(jeu.nom_j1,jeu.nom_j2,jeu.IA1,jeu.IA2,jeu.niveauIA1,jeu.niveauIA2);
            }
            else{
                JOptionPane.showMessageDialog(null, "La sauvegarde n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void Quitter(){
        int res = JOptionPane.showConfirmDialog(null,"Êtes vous sur de vouloir quitter ? ","Quitter",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            jeu.quitter();
        }
    }
    public static void main(String[] args) {
        frame = new JFrame("Menu");
        frame.setContentPane(new InterfaceMenu().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}