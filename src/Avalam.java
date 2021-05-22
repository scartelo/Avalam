import Controleur.ControleurMediateur;
import Global.Configuration;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Vue.InterfaceGraphique;
import Vue.InterfaceTextuelle;

public class Avalam {
    public static void main(String[] args) {
        Jeu jeu = new Jeu(new PlateauDeJeu());
        ControleurMediateur controleur = new ControleurMediateur(jeu);
        switch (Configuration.instance().lis("Interface")){
            case "Graphique":
                InterfaceGraphique.demarrer(jeu, controleur);
                jeu.Partie(1);
                break;
            case "Textuelle":
                InterfaceTextuelle.demarrer(jeu, controleur);
                jeu.Partie(1);
            default:
                Configuration.instance().logger().severe("Interface invalide");
                System.exit(1);
        }

    }
}
