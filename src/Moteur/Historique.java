package Moteur;
import Global.Configuration;
import Structures.*;
/*
L'historique contient les coups qui ont été joué sur le plateau ( passé et futur si ils ont été annulés )
 */
public class Historique<Coup> {
    Sequence<Coup> passe, futur;

    Historique() {
        passe = Configuration.instance().nouvelleSequence();
        futur = Configuration.instance().nouvelleSequence();
    }

    void nouveau(Coup c) {
        passe.insereTete(c);
        while (!futur.estVide())
            futur.extraitTete();
    }

    public boolean peutAnnuler() {
        return !passe.estVide();
    }

    public Coup annuler() {
        if (peutAnnuler()) {
            Coup c = passe.extraitTete();
            futur.insereTete(c);
            return c;
        } else {
            return null;
        }
    }
    public boolean peutRefaire() {
        return !futur.estVide();
    }

    public Coup refaire() {
        if (peutRefaire()) {
            Coup c = futur.extraitTete();
            passe.insereTete(c);
            return c;
        } else {
            return null;
        }
    }
    public void Vider_historique(){
        while(!passe.estVide()){
            passe.extraitTete();
        }
        while(!futur.estVide()){
            futur.extraitTete();
        }
    }
}

