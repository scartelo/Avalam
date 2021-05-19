package Moteur;
import Global.Configuration;
import Structures.*;

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
}

