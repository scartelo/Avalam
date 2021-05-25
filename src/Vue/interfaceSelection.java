package Vue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

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
    private static JFrame frame;

    private int IA1,IA2,niveau1,niveau2; //IA = 1 si active  ou 0 si inactive    // niveau1 = 0 : facile / 1:moyen / 2:difficile
    private String J1,J2;   //Contient le nom des joueurs ( par défaut "Joueur 1" et "Joueur 2" )
    private boolean enabled1,enabled2;
    public interfaceSelection() {
        J1="Joueur 1";
        J2="Joueur 2";
        IA1=0;
        IA2=0;
        niveau1=0;
        niveau2=0;
        enabled1=false;
        enabled2=false;
        niveauIA1.setEnabled(enabled1);
        niveauIA2.setEnabled(enabled2);

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
                    J1=nomJ1.getText();}
                if(!nomJ2.getText().isEmpty()){
                    J2=nomJ2.getText();
                }
                InterfaceMenu m = new InterfaceMenu();
                m.Nouvelle_Partie(J1,J2,IA1,IA2,niveau1,niveau2);
                frame.dispose();
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
