package Sonido;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {

    private static Clip musicaFondo;
    private static Clip clipRebotPilota;
    private static Clip clipGameOver;

    // Reproducir música de fondo en bucle
    public static void reproducirMusicaFondo() {
        try {
            URL musica = Sound.class.getResource("/resources/sonidos/tennisSong.wav");
            if (musica != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musica);
                musicaFondo = AudioSystem.getClip();
                musicaFondo.open(audioIn);
                musicaFondo.loop(Clip.LOOP_CONTINUOUSLY); // Música en bucle
                musicaFondo.start();
                System.out.println("Música de fondo reproduciéndose en bucle...");
            } else {
                System.err.println("No se encontró el archivo de música.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reproducir sonido de rebote de la pelota
    public static void reproducirRebotarPilota() {
        try {
            URL rebotPilota = Sound.class.getResource("/resources/sonidos/ball.wav");
            if (rebotPilota != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(rebotPilota);
                clipRebotPilota = AudioSystem.getClip();
                clipRebotPilota.open(audioIn);
                clipRebotPilota.start();
                System.out.println("Reproduciendo sonido de rebote...");
            } else {
                System.err.println("No se encontró el archivo de rebote de pelota.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bajarVolumenMusica(float decibelios) {
        if (musicaFondo != null && musicaFondo.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            FloatControl gainControl = (FloatControl) musicaFondo.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(decibelios);
        }
    }

    public static void reproducirGameOver() {
        try {
            URL gameOver = Sound.class.getResource("/resources/sonidos/gameOver.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(gameOver);
            Clip clipGameOver = AudioSystem.getClip();
            clipGameOver.open(audioIn);
            if (clipGameOver.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clipGameOver.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(+6.0f);
            }
            clipGameOver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void gameOver(){

        Sound.bajarVolumenMusica(-10.0f); // Bajar volumen de fondo
        Sound.reproducirGameOver(); // Sonido de Game Over más fuerte
        System.exit(0);
    }
}
