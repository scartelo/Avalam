package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URL;

public class interfaceSelection {
    private JPanel panelJ1;
    private JLabel Nom1;
    private JTextField nomJ1;
    private JComboBox niveauIA1;
    private JPanel MainPanel;
    private JCheckBox checkIA1;
    private JLabel Nom2;
    private JTextField nomJ2;
    private JCheckBox checkIA2;
    private JComboBox niveauIA2;
    private JButton Valider;
    private JPanel PanelJ2;
    private JLabel NiveauIA2;
    private JLabel NiveauIA1;
    private JCheckBox joueur2Start;
    private JCheckBox joueur1Start;
    private JLabel labelCommencer1;
    private JRadioButton couleur;
    private JPanel Panel_valider;
    private JLabel LabelRED;
    private JLabel LabelYELLOW;
    private JCheckBox checkBox1;
    private static JFrame frame;
    private boolean color;
    ImageIcon logo_fenetre,logo_YELLOW,logo_RED;
    private int IA1,IA2,niveau1,niveau2,tourDep,MAX_CHAR; //IA = 1 si active  ou 0 si inactive    // niveau1 = 0 : facile / 1:moyen / 2:difficile
    private String J1,J2;   //Contient le nom des joueurs ( par défaut "Joueur 1" et "Joueur 2" )
    private boolean enabled1,enabled2;
    public interfaceSelection() {
        J1="Joueur 1";
        J2="Joueur 2";
        IA1=0;
        IA2=0;
        niveau1=0;
        niveau2=0;
        tourDep=0;
        MAX_CHAR=10;
        enabled1=false;
        enabled2=false;
        niveauIA1.setEnabled(enabled1);
        niveauIA2.setEnabled(enabled2);
        joueur1Start.setEnabled(false);
        joueur2Start.setEnabled(true);
        color=false;
        URL url = getClass().getResource("/Images/logo_fenetre.png");
        logo_fenetre = new ImageIcon(url);
        logo_RED= new ImageIcon(getClass().getResource("/Images/red_square.png"));
        logo_YELLOW= new ImageIcon(getClass().getResource("/Images/yellow_square.png"));

        Image image = logo_RED.getImage(); // transform it
        Image newimg = image.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        logo_RED = new ImageIcon(newimg);  // transform it back

        Image image2 = logo_YELLOW.getImage(); // transform it
        Image newimg2 = image2.getScaledInstance(15, 15,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        logo_YELLOW = new ImageIcon(newimg2);  // transform it back
        LabelRED.setIcon(logo_RED);
        LabelYELLOW.setIcon(logo_YELLOW);


        checkIA1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IA1=(IA1+1)%2;
                enabled1=!enabled1;
                niveauIA1.setEnabled(enabled1);
            }
        });
        checkIA2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IA2=(IA2+1)%2;
                enabled2=!enabled2;
                niveauIA2.setEnabled(enabled2);
            }
        });
        Valider.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!nomJ1.getText().isEmpty()){
                    J1=nomJ1.getText();
                }
                if(!nomJ2.getText().isEmpty()){
                    J2=nomJ2.getText();
                }
                if(J1.length()>MAX_CHAR || J2.length()>MAX_CHAR) {
                    JOptionPane.showMessageDialog(null,"Le nom des joueurs ne doit contenir que "+MAX_CHAR+" caractères au maximum.","Erreur",JOptionPane.ERROR_MESSAGE);
                }else {
                    InterfaceMenu m = new InterfaceMenu();
                    m.Nouvelle_Partie(J1, J2, IA1, IA2, niveau1, niveau2, tourDep, tourDep, color);
                    frame.dispose();
                }
            }
        });
        niveauIA1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s= (String) niveauIA1.getSelectedItem();
                niveau1=difficultee(s);
            }
        });
        niveauIA2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s= (String) niveauIA2.getSelectedItem();
                niveau2=difficultee(s);

            }
        });

        joueur1Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tourDep=0;
                joueur2Start.setSelected(false);
                joueur1Start.setEnabled(false);
                joueur2Start.setEnabled(true);
            }
        });
        joueur2Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tourDep=1;
                joueur1Start.setSelected(false);
                joueur2Start.setEnabled(false);
                joueur1Start.setEnabled(true);
            }
        });

        checkBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                color=!color;
                Icon tmp;
                tmp=LabelYELLOW.getIcon();
                LabelYELLOW.setIcon(LabelRED.getIcon());
                LabelRED.setIcon(tmp);

            }
        });
    }
    public int difficultee(String diff){
        switch(diff){
            case "Facile":
                return 0;
            case "Moyen":
                return 1;
            case "Difficile":
                return 2;
            default:
                System.err.println("erreur dans la sélaction de la difficulté");
                return 0;
        }
    }
    public void selection() {
        frame = new JFrame("Séléction des joueurs");
        frame.setContentPane(new interfaceSelection().MainPanel);
        frame.setIconImage(logo_fenetre.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(440, 360);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
