package Controleur;

import Global.Configuration;
import Moteur.Coup;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;
import Structures.SequenceTableau;

import java.util.Random;


public class JoueurIA extends Joueur {
    Random r;
    int infBorneL = 0, supBorneL = 0, infBorneC = 0, supBorneC = 0;

    JoueurIA(int n, Jeu j) {
        super(n, j);
        r = new Random(100);
        System.out.println();
    }

    public int renvoieZone() {
        int z = r.nextInt(4); // on divise le plateau en 4 zones
        System.out.println("Zone : " + z);
        return z;
    }

    public void fixerBornes() {
        int zone = renvoieZone();
        switch (zone) {
            case 0 -> {
                infBorneL = 0;
                supBorneL = 6;
                infBorneC = 0;
                supBorneC = 6;
            }
            case 1 -> {
                infBorneL = 0;
                supBorneL = 5;
                infBorneC = 6;
                supBorneC = 9;
            }
            case 2 -> {
                infBorneL = 5;
                supBorneL = 9;
                infBorneC = 0;
                supBorneC = 6;
            }
            case 3 -> {
                infBorneL = 5;
                supBorneL = 9;
                infBorneC = 5;
                supBorneC = 9;
            }
            default -> {
                Configuration.instance().logger().severe("Zone inconnue");
                System.exit(1);
            }
        }

    }

    /*Tour tour = selectionAleatoireAvecBorne(p,zone, infBorneL, infBorneC, supBorneL, supBorneC);
    while(tour == null){
        //return null;
        zone = renvoieZone();
        tour = selectionAleatoireAvecBorne(p,zone,infBorneL, infBorneC, supBorneL, supBorneC);
    }

    return tour;

}*/
    public Sequence<Tour> changerZone(PlateauDeJeu p) {
        fixerBornes();
        Sequence<Tour> mesTours = Configuration.instance().nouvelleSequence();
        for (int i = infBorneL; i < supBorneL; i++) {
            for (int j = infBorneC; j < supBorneC; j++) {
                Tour tour = p.tour(i, j);
                if (tour.estJouable() && !p.pasDeplacable(tour) && tour.sommetTour() == num) {
                    mesTours.insereQueue(tour);
                    tour.afficher();
                    System.out.println("taille mes tours " + mesTours.taille());

                }
            }
        }
        System.out.println("milieu changer zone");
        int compte = 0;
        while (!mesTours.iterateur().aProchain()) {
            fixerBornes();
            for (int i = infBorneL; i < supBorneL; i++) {
                for (int j = infBorneC; j < supBorneC; j++) {
                    Tour tour = p.tour(i, j);
                    if (tour.estJouable() && !p.pasDeplacable(tour) && tour.sommetTour() == num) {
                        mesTours.insereQueue(tour);
                    }
                }
            }
            compte++;
            if (compte >= 5) {
                System.out.println("dernier coup");
                return null;
            }
            System.out.println("milieu while changer zone");
        }
        System.out.println("sorti changer zone");
        return mesTours;
    }

    public Tour selectionAleatoireAvecBorne(PlateauDeJeu p) {
        /*int departL = biL + r.nextInt(bsL - biL);
        int departC = biC + r.nextInt(bsC - biC);
        Tour tour = p.tour(departL, departC);*/
        Sequence<Tour> mesTours = changerZone(p);
        //SequenceTableau<Tour> mesTours = (SequenceTableau<Tour>) copy;
        assert (mesTours == null);
        Tour resultat = null;

        if (mesTours == null) {
            resultat = selectionAleatoire(p);
        } else {
            System.out.println("taille " + mesTours.taille());
            int positionTourAjouer = r.nextInt(mesTours.taille());
            Iterateur<Tour> it = mesTours.iterateur();
            int k = 0;
            while (it.aProchain()) {
                Tour tourAJouer = it.prochain();
                if (k == positionTourAjouer) {
                    resultat = tourAJouer;
                } else {
                    k++;
                }
            }
        }

        return resultat;
    }

    public Coup couvrirAdversaire(PlateauDeJeu plateau) {
        //int zone = r.nextInt(4); // on divise le plateau en 4 zones
        System.out.println("debut couvrireAD");
        Tour tour = selectionAleatoireAvecBorne(plateau); // tour pris
        Sequence<Tour> voisins = plateau.voisins(tour.ligne(), tour.colonne());
        Sequence<Tour> vJouables = plateau.voisinsJouables(voisins);
        while (vJouables.estVide()) {
            tour = selectionAleatoireAvecBorne(plateau); // tour pris
            voisins = plateau.voisins(tour.ligne(), tour.colonne());
            vJouables = plateau.voisinsJouables(voisins);
        }
        Iterateur<Tour> itvJouables = vJouables.iterateur();
        Sequence<Tour> toursAdverses = Configuration.instance().nouvelleSequence();
        Sequence<Tour> toursJ = Configuration.instance().nouvelleSequence();
        Coup coup = null;
        while (itvJouables.aProchain()) {
            Tour tourVoisines = itvJouables.prochain();
            if (tourVoisines.sommetTour() != num) {
                if (tour.estDeplacable(tourVoisines)) {
                    toursAdverses.insereQueue(tourVoisines);
                }
            } else {  // si les voisins sont de meme couleur
                toursJ.insereQueue(tourVoisines);
            }
        }
        if (toursAdverses.estVide()) {
            if (toursJ.estVide()) {
                Configuration.instance().logger().severe("Coup non trouver couvrir adversaire");
            } else {
                coup = new Coup(tour, toursJ.extraitTete());
            }
        } else {
            coup = new Coup(tour, toursAdverses.extraitTete());
        }

        //copy.jouer(tour, tourAJouer);
        //copy.plateau().deselection_ia();
        //copy.plateau().selection_ia(tour, tourAJouer);
        System.out.println("Sorti couvrir adversaire");
        if (coup == null)
            Configuration.instance().logger().severe("Coup non trouver couvrir adversaire");
        return coup;
    }

    // regarde la liste des voisins et renvoie le tour qui contient nb pions , null sinon
    public Tour nbPionsMatch(PlateauDeJeu p, Sequence voisins, int nb) {
        Tour tRes = null;
        Iterateur voisinsJouables = p.voisinsJouables(voisins).iterateur();
        while (voisinsJouables.aProchain()) {
            Tour t = (Tour) voisinsJouables.prochain();
            if (t.nbPion() == nb) {
                tRes = t;
                return tRes;
            }
        }
        return tRes;
    }

    //verifier s'il y a un deplacement plus optimal
    public Coup isBetter(PlateauDeJeu p) {
        Coup result;
        Tour tDep;
        Tour tDest;
        for (int i = 0; i < p.lignes(); i++) {
            for (int j = 0; j < p.colonnes(); j++) {
                if (p.tour(i, j).estJouable() && p.tour(i, j).sommetTour() == num) {
                    Sequence voisins = p.voisins(i, j);
                    switch (p.tour(i, j).nbPion()) {
                        case 1:
                            tDest = nbPionsMatch(p, voisins, 4);
                            if (tDest != null) {
                                tDep = p.tour(i, j);
                                result = new Coup(tDep, tDest);
                                return result;
                            }
                            break;
                        case 2:
                            tDest = nbPionsMatch(p, voisins, 3);
                            if (tDest != null) {
                                tDep = p.tour(i, j);
                                result = new Coup(tDep, tDest);
                                return result;
                            }
                            break;
                        case 3:
                            tDest = nbPionsMatch(p, voisins, 2);
                            if (tDest != null) {
                                tDep = p.tour(i, j);
                                result = new Coup(tDep, tDest);
                                return result;
                            }
                            break;
                        case 4:
                            tDest = nbPionsMatch(p, voisins, 1);
                            if (tDest != null) {
                                tDep = p.tour(i, j);
                                result = new Coup(tDep, tDest);
                                return result;
                            }
                            break;
                        default:
                            Configuration.instance().logger().severe("Pas de tour ");
                            return null;

                    }
                }
            }
        }
        return null;
    }


    @Override
    boolean tempsEcoule() {
        return false;
    }

    Tour selectionAleatoire(PlateauDeJeu plateau) {
        int departL = r.nextInt(plateau.lignes());
        int departC = r.nextInt(plateau.colonnes());
        Tour departTour = plateau.tour(departL, departC);
        while (!departTour.estJouable() || !plateau.pasDeplacable(departTour)) {
            departL = r.nextInt(plateau.lignes());
            departC = r.nextInt(plateau.colonnes());
            departTour = plateau.tour(departL, departC);
        }
        return departTour;
    }
}
