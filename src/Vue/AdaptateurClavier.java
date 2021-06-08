package Vue;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AdaptateurClavier extends KeyAdapter {
	CollecteurEvenements controle;

	AdaptateurClavier(CollecteurEvenements c) {
		controle = c;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_Q:
				controle.commande("quitter");
				break;
			case KeyEvent.VK_S:
				controle.commande("sauvegarder");
				break;
			case KeyEvent.VK_N:
				controle.commande("nouvellePartie");
				break;
			case KeyEvent.VK_P:
				controle.commande("pause");
				break;
			case KeyEvent.VK_U:
				controle.commande("annuler");
				break;
			case KeyEvent.VK_R:
				controle.commande("refaire");
				break;
			case KeyEvent.VK_I:
				controle.commande("ia");
				break;
			case KeyEvent.VK_ESCAPE:
				controle.commande("pleinecran");
				break;
		}
	}
}
