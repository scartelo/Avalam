package Vue;

import Controleur.ControleurMediateur;
import Moteur.Jeu;
import Moteur.Saves;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
Classe permettant d'afficher la grille, le score, et un JMenuBar contenant les fonctionnalités du jeu ( refaire,annuler,sauvegarder...)
*/
public class InterfaceGraphique implements Runnable, InterfaceUtilisateur, Observateur {
    private static JFrame frame;
    PlateauGraphique plateauGraphique;
    CollecteurEvenements controle;
    JButton annuler, refaire,restart,sauvegarde,loadbut,quitter,menu;
    private boolean maximized;
    JMenu l_sauvegardes;
    JTextField jt;
    Jeu jeu;

    public InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        jeu=j;
        controle = c;
        plateauGraphique = new PlateauGraphique(j);
        controle = new ControleurMediateur(jeu);
        //Boite dialogue load
        j.ajouteObservateur(this);
    }

    @Override
    public void run() {
        frame = new JFrame("Avalam");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        plateauGraphique.addMouseListener(new AdaptateurSouris(plateauGraphique, controle));
        frame.addKeyListener(new AdaptateurClavier(controle));

        Box principal=Box.createHorizontalBox();

        Box boxJeu = Box.createVerticalBox();
        boxJeu.add(plateauGraphique);
        boxJeu.setAlignmentY(Component.CENTER_ALIGNMENT);
        boxJeu.setPreferredSize(new Dimension(500, 400));
        principal.add(boxJeu);

        frame.add(principal);
        JMenuBar m_bar= new JMenuBar();
        sauvegarde = createButton("Save","sauvegarder");
        // Annuler / Refaire
        Box annulRef = Box.createHorizontalBox();
        annuler = createButton("<", "annuler");
        griser_annuler(false);
        refaire = createButton(">", "refaire");
        griser_refaire(false);
        restart = createButton ("Nouvelle partie", "nouvellePartie");
        menu = createButton ("Menu", "retour_menu");
        quitter = createButton ("Quitter", "quitter");
        annulRef.add(annuler);
        annulRef.add(refaire);
        l_sauvegardes=menu_sauvegarde();
        m_bar.add(quitter);
        m_bar.add(menu);
        m_bar.add(restart);
        m_bar.add(annulRef);
        m_bar.add(sauvegarde);
        m_bar.add(l_sauvegardes);
        frame.setJMenuBar(m_bar);

        controle.fixerInterfaceUtilisateur(this);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public JMenu menu_sauvegarde(){
        //liste des sauvegardes sous forme de menu
        JMenu m = new JMenu("Sauvegardes");
        Saves save=new Saves(jeu);
        if(save.nb_saves==0){
            m.setEnabled(false);
        }
        else{
            for(int i=0;i<save.nb_saves;i++){
                JMenuItem item = new JMenuItem(save.l_saves.get(i).split("\\.")[0]);
                String s =String.valueOf(i+1);
                item.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        controle.commandeInput("load",s);
                    }
                });
                m.add(item);
            }
        }
        return m;
    }
    public static void showFrame(boolean b){
        frame.setVisible(b);
    }
    public void partie_finie(){
        if(jeu.partieTerminee()) {
            JDialog d = new JDialog(frame, "Partie finie !");
            // create a label
            String finie = jeu.get_winner();
            JLabel l = new JLabel(finie);
            d.add(l);
            // setsize of dialog
            d.setSize(200, 100);
            // set visibility of dialog
            d.setVisible(true);
        }
    }
    public static void demarrer(Jeu j, CollecteurEvenements controle) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j , controle));
    }

    private JLabel createLabel(String s) {
        JLabel lab = new JLabel(s);
        lab.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lab;
    }

    private JButton createButton(String s, String c) {
        JButton but = new JButton(s);
        but.addActionListener(new AdaptateurCommande(controle, c));
        but.setAlignmentX(Component.CENTER_ALIGNMENT);
        but.setFocusable(false);
        return but;
    }
    @Override
    public void metAJour() {
        plateauGraphique.repaint();
    }

    @Override
    public void basculePleinEcran() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        if (maximized) {
            device.setFullScreenWindow(null);
            maximized = false;
        } else {
            device.setFullScreenWindow(frame);
            maximized = true;
        }

    }
    @Override
    public void sauvegarder() {
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous sauvegarder ? ","Sauvegarder",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            jeu.sauvegarder();
            metAJour();
        }
    }

    @Override
    public void quitter() {
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous vraiment quitter ? ","Quitter le jeu",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            jeu.quitter();
            metAJour();
        }
    }



    @Override
    public void nouvellePartie(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous recommencer la partie ? ","Nouvelle partie",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            sauvegarder();
            jeu.nouvellePartie();
            metAJour();
        }
    }


    public void retour_menu(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous revenir au menu ? ","Menu",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res== JOptionPane.YES_OPTION){
            InterfaceMenu m = new InterfaceMenu();
            m.showMenu(true);
            frame.dispose();
            metAJour();
        }
    }
    public void griser_annuler(boolean b){
        annuler.setEnabled(b);
    }
    public void griser_refaire(boolean b){
        refaire.setEnabled(b);
    }
}
