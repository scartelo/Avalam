package Patterns;

import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

public class Observable {
	Sequence<Observateur> observateurs;

	public Observable() {
		observateurs = Configuration.instance().nouvelleSequence();
	}

	public void ajouteObservateur(Observateur l) {
		observateurs.insereQueue(l);
	}

	public void miseAJour() {
		Iterateur<Observateur> it;
		it = observateurs.iterateur();
		while (it.aProchain()) {
			it.prochain().metAJour();
		}
	}
}
