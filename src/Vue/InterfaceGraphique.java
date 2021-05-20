package Vue;

import Moteur.PlateauDeJeu;
import Moteur.Jeu;
import Moteur.Saves;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InterfaceGraphique implements Runnable {
    private JFrame frame;
    PlateauGraphique plateauGraphique;
    CollecteurEvenements controle;
    JButton annuler, refaire,restart,sauvegarde,loadbut;
    JTextField jt;

    public InterfaceGraphique(Jeu j, CollecteurEvenements c) {
        plateauGraphique = new PlateauGraphique(j);
        controle = c;
        //Boite dialogue load
    }

    @Override
    public void run() {
        frame = new JFrame("Avalam");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //plateauGraphique.addMouseListener(new EcouteurDeSouris(jeuGraphique, controle));

        Box principal=Box.createHorizontalBox();

        Box boxJeu = Box.createVerticalBox();
        boxJeu.add(plateauGraphique);
        boxJeu.setAlignmentY(Component.CENTER_ALIGNMENT);
        boxJeu.setPreferredSize(new Dimension(500, 400));
        principal.add(boxJeu);


        Box barreLaterale= Box.createVerticalBox();
        barreLaterale.add(createLabel("Avalam"));
        barreLaterale.add(Box.createGlue());
        // Annuler / Refaire
        Box annulRef = Box.createHorizontalBox();
        annuler = createButton("<", "annule");
        refaire = createButton(">", "refaire");
        annulRef.add(annuler);
        annulRef.add(refaire);
        barreLaterale.add(annulRef);

        sauvegarde = createButton("Save","save");
        barreLaterale.add(sauvegarde);
        barreLaterale.add(Box.createGlue());

        barreLaterale.add(createLabel("Load (Save n°1,2..)"));
        jt=new JTextField("");
        jt.setSize(new Dimension(20,3));
        jt.setMinimumSize(new Dimension(20,3));
        jt.setMaximumSize(new Dimension(20,3));
        jt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String input = jt.getText();
                controle.commandeInput("load",input);
            }
        });
        barreLaterale.add(jt);
        barreLaterale.add(Box.createGlue());

        principal.add(barreLaterale);
        frame.add(principal);

        //liste des sauvegardes sous forme de menu
        JMenuBar m_bar= new JMenuBar();
        JMenu m = new JMenu("Sauvegardes");
        Saves save=new Saves();
        for(int i=0;i<save.nb_saves;i++){
            JMenuItem item = new JMenuItem(save.l_saves.get(i).split("\\.")[0]);
            m.add(item);
        }
        m_bar.add(m);
        frame.setJMenuBar(m_bar);


        frame.setSize(500, 300);
        frame.setVisible(true);
    }

    public void demarrer(Jeu j, CollecteurEvenements c) {
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

}
