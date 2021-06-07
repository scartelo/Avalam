package Controleur;

import Global.Configuration;
import Moteur.Jeu;
import Moteur.PlateauDeJeu;
import Moteur.Tour;
import Structures.Couple;
import Structures.Iterateur;
import Structures.Sequence;

import java.util.Random;


public class JoueurIA extends Joueur{
    Random r;

    JoueurIA(int n, Jeu j) {
        super(n, j);
        r = new Random();
    }
    public Tour selectionTourDansZone(PlateauDeJeu p, int zone) {
        int infBorneL = 0, supBorneL = 0, infBorneC = 0, supBorneC = 0;
        switch (zone) {
            case 0 -> {
                infBorneL = 0;
                supBorneL = 5;
                infBorneC = 0;
                supBorneC = 5;
            }
            case 1 -> {
                infBorneL = 0;
                supBorneL = 5;
                infBorneC = 5;
                supBorneC = 9;
            }
            case 2 -> {
                infBorneL = 5;
                supBorneL = 9;
                infBorneC = 0;
                supBorneC = 5;
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
        Tour tour = selectionAleatoireAvecBorne(p, infBorneL, infBorneC, supBorneL, supBorneC);
        return tour;

    }

    public Tour selectionAleatoireAvecBorne(PlateauDeJeu p, int biL, int biC, int bsL, int bsC) {
        /*int departL = biL + r.nextInt(bsL - biL);
        int departC = biC + r.nextInt(bsC - biC);
        Tour tour = p.tour(departL, departC);*/
        Sequence mesTours = Configuration.instance().nouvelleSequence();
        int nbMesTours = 0;
        Tour resultat = null;
        for (int i = biL; i < bsL; i++) {
            for (int j = biC; j < bsC; j++) {
                Tour tour = p.tour(i, j);
                if (tour.estJouable() && !p.pasDeplacable(tour) && tour.sommetTour() == num) {
                    mesTours.insereQueue(tour);
                    nbMesTours++;
                }
            }
        }
        Iterateur it = mesTours.iterateur();
        int positionTourAjouer = r.nextInt(nbMesTours);
        int k = 0;
        while (it.aProchain()) {
            Tour tourAJouer = (Tour) it.prochain();
            if (k == positionTourAjouer) {
                resultat = tourAJouer;
            } else {
                k++;
            }
        }

        return resultat;
    }

    public void couvrirAdversaire(Jeu copy) {
        int zone = r.nextInt(4); // on divise le plateau en 4 zones
        Tour tour = selectionTourDansZone(copy.plateau(), zone);
        Sequence<Tour> voisins = copy.plateau().voisins(tour.ligne(), tour.colonne());
        Iterateur<Tour> vJouables = copy.plateau().voisinsJouables(voisins).iterateur();
        Sequence<Tour> toursAdverses = Configuration.instance().nouvelleSequence();
        while(!vJouables.aProchain()){
            zone = r.nextInt(4); // on divise le plateau en 4 zones
            tour = selectionTourDansZone(copy.plateau(), zone);
            voisins = jeu.plateau().voisins(tour.ligne(), tour.colonne());
            vJouables = jeu.plateau().voisinsJouables(voisins).iterateur();
        }
        while (vJouables.aProchain()) {
            Tour tourVoisines = (Tour) vJouables.prochain();
            if (tourVoisines.sommetTour() != num) {
                if (tour.estDeplacable(tourVoisines)) {
                    toursAdverses.insereQueue(tourVoisines);
                }
            }
        }
        Tour tourAJouer = toursAdverses.extraitTete();

        //p.Jouer(tour, tourAJouer);
        copy.jouer(tour, tourAJouer);
        copy.plateau().deselection_ia();
        copy.plateau().selection_ia(tour, tourAJouer);
    }
    // regarde la liste des voisins et renvoie le tour qui contient nb pions , null sinon
    public Tour nbPionsMatch(PlateauDeJeu p , Sequence voisins, int nb){
        Tour tRes = null;
        Iterateur voisinsJouables = p.voisinsJouables(voisins).iterateur();
        while (voisinsJouables.aProchain()){
            Tour t = (Tour) voisinsJouables.prochain();
            if(t.nbPion() == nb){
                tRes = t;
                return  tRes;
            }
        }
        return tRes;
    }

    //verifier s'il y a un deplacement plus optimal
    public Couple<Tour,Tour> isBetter (PlateauDeJeu p){
        Couple<Tour,Tour> result;
        Tour tDep;
        Tour tDest;
        for(int i = 0 ; i < p.lignes();i++){
            for(int j = 0 ; j < p.colonnes();j++){
                if(p.tour(i,j).estJouable() &&  p.tour(i,j).sommetTour() == num){
                    Sequence voisins = p.voisins(i, j);
                    switch (p.tour(i,j).nbPion()){
                        case 1:
                            tDest = nbPionsMatch(p,voisins,4);
                            if(tDest != null){
                                tDep = p.tour(i,j);
                                result = new Couple<>(tDep,tDest);
                                return result;
                            }
                            break;
                        case 2:
                            tDest = nbPionsMatch(p,voisins,3);
                            if(tDest != null){
                                tDep = p.tour(i,j);
                                result = new Couple<>(tDep,tDest);
                                return result;
                            }
                            break;
                        case 3:
                            tDest = nbPionsMatch(p,voisins,2);
                            if(tDest != null){
                                tDep = p.tour(i,j);
                                result = new Couple<>(tDep,tDest);
                                return result;
                            }
                            break;
                        case 4:
                            tDest = nbPionsMatch(p,voisins,1);
                            if(tDest != null){
                                tDep = p.tour(i,j);
                                result = new Couple<>(tDep,tDest);
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
    boolean tempsEcoule(){
        return false;
    }

    Tour selectionAleatoire(){
        int departL = r.nextInt(jeu.plateau().lignes());
        int departC = r.nextInt(jeu.plateau().colonnes());
        Tour departTour = jeu.plateau().tour(departL,departC);
        while (!departTour.estJouable() || jeu.plateau().estIsole(departL, departC)){
            departL = r.nextInt(jeu.plateau().lignes());
            departC = r.nextInt(jeu.plateau().colonnes());
            departTour = jeu.plateau().tour(departL,departC);
        }
        return departTour;
    }
}
