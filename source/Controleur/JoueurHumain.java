package Controleur;

import Global.Audio;
import Global.Configuration;
import Moteur.Jeu;
import Moteur.Tour;

public class JoueurHumain extends Joueur {
    Tour tourSelectionnee = null;
    JoueurHumain(int n, Jeu j) {
        super(n, j);
    }

    @Override
    // Fonction appelée par un joueur humain pour jouer un coup
    public boolean joue(int l, int c){
        if (!jeu.estTermine()) {
            if (tourSelectionnee == null) {
                if (!jeu.plateau().tour(l, c).estJouable() || jeu.plateau().pasDeplacable(jeu.plateau().tour(l, c))) {
                    if (jeu.plateau().tour(l, c).estInnocupable())
                        Configuration.instance().logger().warning("Hors grille");
                    else
                        Configuration.instance().logger().warning("Cette tour n'est pas jouable");
                    Audio.play_sound("Error");
                    return false;
                }
                else if (jeu.plateau().estIsole(l, c)) {
                    Audio.play_sound("Error");
                    Configuration.instance().logger().warning("Cette tour est isolée, elle ne peut pas être sélectionner");
                    return false;
                } else {
                    tourSelectionnee = jeu.plateau().tour(l,c);
                    /*tourSelectionnee.marquer(
                            Integer.parseInt(Configuration.instance().lis("CouleurSubrillance"),16)
                    );*/
                    tourSelectionnee.marquer(true);
                    jeu.miseAJour();
                    Configuration.instance().logger().info(
                            "La tour (" + tourSelectionnee.ligne() + ", " + tourSelectionnee.colonne() + ") a été selectionnée");
                    Audio.play_sound("Pick");
                    return false;
                }
            } else {
                Tour dest = jeu.plateau().tour(l, c);
                if (dest.estJouable() && dest.estDeplacable(tourSelectionnee)) {
                    //jeu.plateau().Jouer(tourSelectionnee, dest);
                    jeu.jouer(tourSelectionnee, dest);
                    Audio.play_sound("Drop");
                    Configuration.instance().logger().info(
                            "Déplacement de la tour (" + tourSelectionnee.ligne() + "," + tourSelectionnee.colonne() + ") vers la tour (" + dest.ligne() + "," + dest.colonne() + ") effectué");
                    tourSelectionnee.marquer(false);

                    tourSelectionnee = null;
                    jeu.plateau().deselection_ia();
                    jeu.miseAJour();

                    int prochain_joueur=(num()+1)%2;
                    if(jeu.isIA(prochain_joueur)){
                        jeu.change_ia_state(prochain_joueur,1);
                    }
                    System.out.println("J'ai fini");
                    return true;
                } else {
                    Configuration.instance().logger().warning("Déplacement impossible");
                    if(!(dest.ligne()==tourSelectionnee.ligne() && dest.colonne()==tourSelectionnee.colonne())) {
                        Audio.play_sound("Error");
                    }
                    tourSelectionnee.marquer(false);

                    tourSelectionnee = null;
                    jeu.miseAJour();
                    return false;
                }

            }
        }else
            Configuration.instance().logger().info("Partie terminée");
        return false;
    }

}