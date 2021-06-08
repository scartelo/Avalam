package Vue;

import Controleur.ControleurMediateur;
import Moteur.Jeu;
import Moteur.Saves;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/*
Classe permettant d'afficher la grille, le score, et un JMenuBar contenant les fonctionnalités du jeu ( refaire,annuler,sauvegarder...)
*/
public class InterfaceGraphique implements Runnable, InterfaceUtilisateur, Observateur {
    private static JFrame frame;
    PlateauGraphique plateauGraphique;
    CollecteurEvenements controle;
    JButton annuler;
    JButton refaire;
    JButton restart;
    JButton sauvegarde;
    JButton loadbut;
    JButton quitter;
    JButton menu;
    JButton transparency,full_scr;
    JToggleButton tourFinie;
    JToggleButton iaJ1, iaJ2,voisins;
    JPanel game_panel;
    private boolean maximized;
    private boolean full_s;
    JMenu l_partie, l_sauvegardes;
    JTextField jt;
    Jeu jeu;
    ImageIcon logo_fenetre,logo_home,logo_partie;
    public InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        jeu=j;
        controle = c;
        plateauGraphique = new PlateauGraphique(j);
        controle = new ControleurMediateur(jeu);
        //Boite dialogue load
        j.ajouteObservateur(this);
        URL url = getClass().getResource("/Images/logo_fenetre.png");
        logo_fenetre = new ImageIcon(url);
        logo_home= new ImageIcon(getClass().getResource("/Images/home.png"));
        logo_partie= new ImageIcon(getClass().getResource("/Images/logo_partie.png"));
        Image tmp_home = logo_home.getImage() ;
        Image tmp_home2 = tmp_home.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ;
        logo_home = new ImageIcon( tmp_home2 );
        full_s=false;
    }
    public void run(){
        JLabel test= new JLabel();

        frame = new JFrame("Avalam");
        frame.setIconImage(logo_fenetre.getImage());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we)
            {
                quitter();
            }
        });
        plateauGraphique.addMouseListener(new AdaptateurSouris(plateauGraphique, controle));
        frame.addKeyListener(new AdaptateurClavier(controle));
        controle.fixerInterfaceUtilisateur(this);
        frame.setLocationRelativeTo(null);
        frame.add(plateauGraphique);
        sauvegarde = createButton("Enregistrer","sauvegarder");
        // Annuler / Refaire
        JPanel annulRef = new JPanel();
        annuler = createButton("<", "annuler");
        annuler.setToolTipText("Annule le dernier coup joué");
        griser_annuler(false);
        refaire = createButton(">", "refaire");
        refaire.setToolTipText("Rejoue le dernier coup annulé");
        griser_refaire(false);
        transparency=createButton("Transparence","transparency");
        transparency.setToolTipText("Affiche toutes les tours en transparent");
        tourFinie = createToggleButton("Tours finies");
        tourFinie.setToolTipText("Enlève l'affichage des tours finies");
        tourFinie.addActionListener(new AdaptateurCommande(controle,"tourFinie"));
        restart = createButton ("Nouvelle partie", "nouvellePartie");
        menu = createButton ("Menu", "retour_menu");
        menu.setBackground(new Couleur("CouleurScore").couleur());
        menu.setBorderPainted(false);
        menu.setToolTipText("Retour au menu");
        quitter = createButton ("Quitter", "quitter");
        iaJ1 = createToggleButton("IAJ1");
        iaJ1.addActionListener(new AdaptateurJoueur(controle, iaJ1, 0));
        if(jeu.IA1==1){
            iaJ1.setSelected(true);
        }
        iaJ2 = createToggleButton("IAJ2");
        iaJ2.addActionListener(new AdaptateurJoueur(controle, iaJ2, 1));
        if(jeu.IA2==1){
            iaJ2.setSelected(true);
        }
        voisins = createToggleButton("Voisins");
        voisins.setToolTipText("Affiche les voisins de la tour selectionnée");
        voisins.addActionListener(new AdaptateurCommande(controle,"aff_voisins"));

        l_partie=menu_partie();
        l_partie.setToolTipText("Menu de la partie");
        l_partie .setBackground(new Couleur("CouleurScore").couleur());
        Box box_panel= Box.createHorizontalBox();
        box_panel.add(annulRef,BorderLayout.CENTER);
        annulRef.setBackground(new Couleur("CouleurScore").couleur());
        box_panel.setBackground(new Couleur("CouleurScore").couleur());
        box_panel.setBorder(BorderFactory.createBevelBorder(0));
        JMenuBar m_bar= new JMenuBar();
        m_bar.add(l_partie);
        m_bar.setBackground(new Couleur("CouleurScore").couleur());
        annulRef.add(transparency);
        annulRef.add(tourFinie);
        annulRef.add(voisins);
        annulRef.add(annuler);
        annulRef.add(refaire);
        annulRef.add(iaJ1);
        annulRef.add(iaJ2);
        annulRef.add(m_bar);
        annulRef.add(menu);
        annulRef.setBorder(BorderFactory.createBevelBorder(0));
        frame.add(annulRef,BorderLayout.SOUTH);

        menu.setIcon(logo_home);
        l_partie.setIcon(logo_partie);

        controle.update_buttons();
        Timer time = new Timer(16, new AdaptateurTemps(controle));
        time.start();
        frame.pack();
        basculePleinEcran();
        frame.setVisible(true);

    }

    public JMenu menu_sauvegarde(){
        //liste des sauvegardes sous forme de menu
        JMenu m = new JMenu("Charger");
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
                        controle.update_buttons();
                    }
                });
                m.add(item);
            }
        }
        return m;
    }
    public JMenu menu_partie(){
        //liste des sauvegardes sous forme de menu
        JMenu m = new JMenu();

        JMenuItem newgame = new JMenuItem("Nouvelle Partie");
        newgame.setToolTipText("Partie rapide avec les mêmes options");
        newgame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                controle.commande("nouvellePartie");
                controle.update_buttons();
            }
        });
        m.add(newgame);
        JMenuItem save = new JMenuItem("Sauvegarder");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                controle.commande("sauvegarder");
                controle.update_buttons();
            }
        });
        m.add(save);

        l_sauvegardes=menu_sauvegarde();
        m.add(l_sauvegardes);

        return m;
    }
    public void aff_transparence(){
        plateauGraphique.transparency=!plateauGraphique.transparency;
        metAJour();
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

    private JToggleButton createToggleButton(String s) {
        JToggleButton but = new JToggleButton(s);
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
        metAJour();
    }
public void fullscreen(){
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    full_s=!full_s;
    frame.setUndecorated(full_s);
    frame.setVisible(false);
    frame.setVisible(true);
    metAJour();
}

    @Override
    public void Win_message(){
        String message = jeu.Win_message();
        JOptionPane.showMessageDialog(null,message,"Partie terminée",JOptionPane.PLAIN_MESSAGE);
    }

    public void update_waiting(){
        PlateauGraphique.update_attente();
        metAJour();
    }
    public void reset_waiting(){
        PlateauGraphique.reset_attente();
        metAJour();
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
    public void load(String c){
        int res = JOptionPane.showConfirmDialog(null,"Êtes vous sur de vouloir charger la sauvegarde ? ","Charger",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            sauvegarder();
            jeu.load(Integer.parseInt(c),0);
            controle.update_buttons();
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
    public void aff_voisin() {
        plateauGraphique.aff_voisins = !plateauGraphique.aff_voisins;
        metAJour();   }
    public void aff_tourFinie() {
        plateauGraphique.aff_tourFinie = !plateauGraphique.aff_tourFinie;metAJour();
    }


    @Override
    public void nouvellePartie(){
        int res = JOptionPane.showConfirmDialog(null,"Voulez vous recommencer la partie ? ","Nouvelle partie",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(res==JOptionPane.YES_OPTION){
            sauvegarder();
            jeu.nouvellePartie();
            plateauGraphique = new PlateauGraphique(jeu);
            controle.init_joueurs();
            frame.dispose();
            Dimension dim = frame.getSize();
            Point location = frame.getLocation();
            run();
            frame.setSize(dim);
            frame.setLocation(location);
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
            controle.change_play_state();
        }
    }
    public void griser_annuler(boolean b){
        annuler.setEnabled(b);
    }
    public void griser_refaire(boolean b){
        refaire.setEnabled(b);
    }
}
