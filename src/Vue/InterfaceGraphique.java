package Vue;

import Moteur.PlateauDeJeu;
import Moteur.Jeu;
import Moteur.Saves;
import Patterns.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InterfaceGraphique implements Runnable, InterfaceUtilisateur, Observateur {
    private JFrame frame;
    PlateauGraphique plateauGraphique;
    CollecteurEvenements controle;
    JButton annuler, refaire,restart,sauvegarde,loadbut,quitter;
    private boolean maximized;
    JTextField jt;
    Jeu jeu;

    public InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        jeu=j;
        plateauGraphique = new PlateauGraphique(j);
        controle = c;
        //Boite dialogue load
        j.ajouteObservateur(this);
    }

    @Override
    public void run() {
        frame = new JFrame("Avalam");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        plateauGraphique.addMouseListener(new EcouteurDeSouris(plateauGraphique, controle));

        Box principal=Box.createHorizontalBox();

        Box boxJeu = Box.createVerticalBox();
        boxJeu.add(plateauGraphique);
        boxJeu.setAlignmentY(Component.CENTER_ALIGNMENT);
        boxJeu.setPreferredSize(new Dimension(500, 400));
        principal.add(boxJeu);

        frame.add(principal);

        //liste des sauvegardes sous forme de menu
        JMenuBar m_bar= new JMenuBar();
        JMenu m = new JMenu("Sauvegardes");
        Saves save=new Saves();
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
        m_bar.add(m);
        sauvegarde = createButton("Save","save");
        m_bar.add(sauvegarde);
        // Annuler / Refaire
        Box annulRef = Box.createHorizontalBox();
        annuler = createButton("<", "annule");
        refaire = createButton(">", "refaire");
        restart = createButton ("Nouvelle partie", "restart");
        quitter = createButton ("quitter", "quitter");
        annulRef.add(annuler);
        annulRef.add(refaire);
        m_bar.add(annulRef);
        m_bar.add(restart);
        m_bar.add(quitter);
        frame.setJMenuBar(m_bar);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
    public void partie_finie(){
        if(jeu.partie_terminee()) {
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
    public static void demarrer(Jeu j, CollecteurEvenements c) {
        SwingUtilities.invokeLater(new InterfaceGraphique(j, c));
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
}
