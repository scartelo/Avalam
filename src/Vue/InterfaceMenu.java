package Vue;

import Controleur.ControleurMediateur;
import Moteur.Audio;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Saves;

import javax.imageio.ImageIO;
import javax.naming.ldap.Control;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/*
Classe permettant d'afficher le contenu du menu principal
*/
public class InterfaceMenu {
    private static Audio sound;
    private JButton LoadGame;
    private JPanel MainPanel;
    private JButton chargerUnePartieButton;
    private JButton quitterButton;
    private JButton startGame;
    private JButton tutorielButton;
    private JComboBox BoxChargerSauvegarde;
    private static JFrame frame;
    Jeu jeu;

    public InterfaceMenu() {
        jeu = new Jeu(new PlateauDeJeu());
        Saves save = new Saves(jeu);
        for(int i=0;i<save.l_saves.size();i++){
            BoxChargerSauvegarde.addItem(save.l_saves.get(i));
        }
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
        URL url = getClass().getResource("/Images/tutoriel.png");
        ImageIcon image = new ImageIcon(url);
        JLabel label_tuto = new JLabel( image);
        tutoriel.add(label_tuto);
        tutoriel.setVisible(true);
    }
    public void showMenu(boolean b){
        frame.setVisible(b);
        if(b){
            sound.play();
        }
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
        stop_sound();
        frame.setVisible(false);
    }

    public void Charger_Partie(int n_save){
        int res = JOptionPane.showConfirmDialog(null,"Êtes vous sur de vouloir charger la sauvegarde ? ","Charger",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            if(jeu.load(BoxChargerSauvegarde.getSelectedIndex()+1,1)){
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
        menu_music();
        frame.setContentPane(new InterfaceMenu().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public void stop_sound(){
        try {
            sound.stop();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public static void menu_music(){
        sound= null;
        try {
            sound = new Audio("8bitMenu");
            sound.change_volume(-30);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        sound.boucle();
        sound.play();
    }

}