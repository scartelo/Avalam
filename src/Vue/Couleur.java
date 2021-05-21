package Vue;

import Global.Configuration;

import java.awt.*;

public class Couleur {
    Color couleur;

    Couleur(String c){
        couleur = new Color(Integer.parseInt(Configuration.instance().lis(c), 16));
    }

     Color couleur(){
        return couleur;
    }
}
