package Moteur;
import Global.Configuration;
import Structures.Iterateur;
import Structures.Sequence;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Saves{
    public int nb_saves;
    public List<String> l_saves;
    String path;
    int taille_futur;
    Jeu jeu;
    public Saves(Jeu j) {
        jeu=j;
        final String dir = System.getProperty("user.dir");
        String home = System.getProperty("user.dir");
        path = home + File.separator + "res" + File.separator + "Saves";
        File directory = new File(path);
        String[] tmp_l_saves = directory.list();
        l_saves=new ArrayList<>();
        l_saves.addAll(Arrays.asList(tmp_l_saves));
        nb_saves = l_saves.size();
        taille_futur=0;
    }
    /*
    Ecrit dans 'res/Saves' la sauvegarde, celle-ci correspond à l'historique de la partie en cours.
    La sauvegarde contiendra les positions sur la grille (i,j) des coups de l'historique ( passé et futur )
    */
    public void write_save(Sequence<Coup> passe, Sequence<Coup> futur) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yy__HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();

        String save_path = path + File.separator + dtf.format(now)+".txt";
        File f = new File(save_path);

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(save_path);
            myWriter.write(jeu.nom_j1+"\n");
            myWriter.write(jeu.nom_j2+"\n");
            myWriter.write(jeu.IA1+"\n");
            myWriter.write(jeu.IA2+"\n");
            myWriter.write(jeu.niveauIA1+"\n");
            myWriter.write(jeu.niveauIA2+"\n");
            Iterateur<Coup> it_p = passe.iterateur();
            Iterateur<Coup> it_f = futur.iterateur();

            while (it_p.aProchain()){
                Coup c = it_p.prochain();
                myWriter.write(c.src.ligne+" "+c.src.colonne+" "+c.dst.ligne+" "+c.dst.colonne+"\n");
            }
            myWriter.write("-1\n");
            while (it_f.aProchain()){
                Coup c = it_f.prochain();
                myWriter.write(c.src.ligne+" "+c.src.colonne+" "+c.dst.ligne+" "+c.dst.colonne+"\n");
            }
            myWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    Lis l'historique contenu dans la sauvegarde numero 'n_save' et renvoie une séquence correspondant à l'historique complet ( futur + passe )
    La fonction incrémente aussi la taille du futur, permettant de charger la sauvegarde en jouant tout les coups, puis en faisant annuler_coup*taille_futur
    */
    public Sequence<Coup> read_save(int n_save) {
        String save_path = path + File.separator + l_saves.get(n_save - 1);
        File save = new File(save_path);
        try (Scanner myReader = new Scanner(save)) {
            int i_1,j_1,i_2,j_2;
            Sequence<Coup> seq = Configuration.instance().nouvelleSequence();
            // Passe
            int next =0;
            String s=myReader.nextLine();
            jeu.nom_j1=s;
            s=myReader.nextLine();
            jeu.nom_j2=s;
            next = myReader.nextInt();
            jeu.IA1=next;
            next = myReader.nextInt();
            jeu.IA2=next;
            next = myReader.nextInt();
            jeu.niveauIA1=next;
            next = myReader.nextInt();
            jeu.niveauIA2=next;
            next =0;
            while (myReader.hasNextInt() && next !=-1) {
                next = myReader.nextInt();
                if (next != -1) {
                    i_1 = next;
                    j_1 = myReader.nextInt();
                    i_2 = myReader.nextInt();
                    j_2 = myReader.nextInt();
                    Tour src = new Tour(0,i_1,j_1);
                    Tour dst = new Tour(0,i_2,j_2);
                    Coup c = new Coup(src,dst);
                    seq.insereTete(c);
                }
            }

            //Futur
            while (myReader.hasNextInt()) {
                next = myReader.nextInt();
                if (next != -1) {
                    taille_futur++;
                    i_1 = next;
                    j_1 = myReader.nextInt();
                    i_2 = myReader.nextInt();
                    j_2 = myReader.nextInt();
                    Tour src = new Tour(0,i_1,j_1);
                    Tour dst = new Tour(0,i_2,j_2);
                    Coup c = new Coup(src,dst);
                    seq.insereQueue(c);
                }
            }
            myReader.close();
            return seq;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveExists(int n_save){
        return (n_save > 0 && n_save <= nb_saves);
    }
}

