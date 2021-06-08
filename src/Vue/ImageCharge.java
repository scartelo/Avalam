package Vue;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class ImageCharge {
    Image image;

    ImageCharge(InputStream in){
        try {
            image = ImageIO.read(in);
        } catch (IOException e) {
            System.err.println("Impossible de charger l'image " + in );
            System.exit(1);
        }
    }

    Image image(){
        return image;
    }

    public static InputStream charge(String nom) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(nom);
    }

}