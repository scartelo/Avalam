package Moteur;
import Structures.Sequence;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Saves{
    int nb_saves;
    String[] l_saves;
    String path;

    public Saves() {
        final String dir = System.getProperty("user.dir");
        String home = System.getProperty("user.dir");
        path = home + File.separator + "res" + File.separator + "Saves";
        File directory = new File(path);
        l_saves = directory.list();
        nb_saves = l_saves.length;
    }

    public void write_save(Sequence<Coup> passe, Sequence<Coup> futur) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yy__HH_mm");
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
            myWriter.write("P\n");
            while (!passe.estVide()){
                Coup c = passe.extraitTete();
                myWriter.write(c.src.ligne+" "+c.src.colonne+" "+c.dst.ligne+" "+c.dst.colonne+"\n");
            }
            myWriter.write("F\n");
            while (!futur.estVide()){
                Coup c = futur.extraitTete();
                myWriter.write(c.src.ligne+" "+c.src.colonne+" "+c.dst.ligne+" "+c.dst.colonne+"\n");
            }
            myWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }/*
    public void read_save(int save) {
        String save_path = path + File.separator +l_saves[save-1]+".txt";
        File save=new File(save_path);
        try (Scanner myReader = new Scanner(save)) {
            while (myReader.hasNextLine()) {
                if(myReader.nextLine()=="P"){
                    System.out.println("P FOUND");
                }
            }
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }*/
}

