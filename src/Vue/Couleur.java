package Vue;

import Global.Configuration;

import java.awt.*;
/*
Renvoie une couleur selon une string donnée, les couleurs sont définies dans le fichier de configurations
*/
public class Couleur {
    Color couleur;

    Couleur(String c){
        couleur = new Color(Integer.parseInt(Configuration.instance().lis(c), 16));
    }

     Color couleur(){
        return couleur;
    }
}
