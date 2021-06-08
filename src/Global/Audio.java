package Global;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class Audio {

    private static Audio instance = null;
    // to store current position
    static Long currentFrame;
    static Clip clip;

    // current status of clip
    static String status;

    AudioInputStream audioInputStream;
    static String filePath;


    public Audio(String sound_name) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        String home = System.getProperty("user.dir");
        URL url = getClass().getResource("/Audio/"+sound_name+".wav");
        // create AudioInputStream object
        audioInputStream = AudioSystem.getAudioInputStream(url);

        // create clip reference
        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);
    }

    public static Audio instance(String sound_name) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        return instance = new Audio(sound_name);
    }
    /*
    Permet de jouer l'audio en cours
    */
    public static void boucle(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public static void play()
    {
        //start the clip
        clip.start();

        status = "play";
    }

    /*
    Permet d'arrêter l'audio en cours
    */
    public void pause()
    {
        if (status.equals("paused"))
        {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame =
                this.clip.getMicrosecondPosition();
        clip.stop();
        status = "paused";
    }

    /*
    Reprend l'audio
    */
    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        if (status.equals("play"))
        {
            System.out.println("Audio is already "+
                    "being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    /*
    Recommence l'audio depuis le début
    */
    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException
    {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }

    /*
    Arrête l'audio actuel
    */
    public static void stop() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    /*
    Permet de lancer l'audio à un moment donné
    */
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        if (c > 0 && c < clip.getMicrosecondLength())
        {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }

    /*
    Permet de reset le stream de l'audio
    */
    public static void change_volume(float db){
        FloatControl gainControl =
                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(db);
    }
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(filePath).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /*
    Joue un son dans res/Audio selon sound_name ( nom du fichier )
    */
    public static void play_sound(String sound_name){
        Audio sound = null;
        try {
            //sound = new Audio(sound_name);
            sound = instance(sound_name);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        sound.play();
    }

}