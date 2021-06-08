package Vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdaptateurCommande implements ActionListener {
    CollecteurEvenements control;
    String commande;

    AdaptateurCommande(CollecteurEvenements c, String com) {
        control = c;
        commande = com;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        control.commande(commande);
    }
}
