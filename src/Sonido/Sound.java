package Sonido;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {

    private static Clip musicaFondo;
    private static Clip musicaMenu;
    private static Clip clipRebotPilota;
    private static Clip clipGameOver;

    // Reprodueix la música del menú principal
    public static void reproducirMusicaMenu() {
        try {
            URL musica = Sound.class.getResource("/resources/sonidos/musicaMenu.wav");
            if (musica != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musica);
                musicaMenu = AudioSystem.getClip();
                musicaMenu.open(audioIn);
                musicaMenu.loop(Clip.LOOP_CONTINUOUSLY); // Reprodueix en bucle
                musicaMenu.start();
            } else {
                System.err.println("No s'ha trobat l'arxiu de música del menú.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reprodueix la música de fons durant el joc
    public static void reproducirMusicaFondo() {
        try {
            // Atura i tanca la música del menú si encara sona
            if (musicaMenu != null && musicaMenu.isRunning()) {
                musicaMenu.stop();
                musicaMenu.close();
            }

            URL musica = Sound.class.getResource("/resources/sonidos/tennisSong.wav");
            if (musica != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musica);
                musicaFondo = AudioSystem.getClip();
                musicaFondo.open(audioIn);
                musicaFondo.loop(Clip.LOOP_CONTINUOUSLY); // Reprodueix en bucle
                musicaFondo.start();
            } else {
                System.err.println("No s'ha trobat l'arxiu de música del joc.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reprodueix el so del rebot de la pilota
    public static void reproducirRebotarPilota() {
        try {
            URL rebotPilota = Sound.class.getResource("/resources/sonidos/ball.wav");
            if (rebotPilota != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(rebotPilota);
                clipRebotPilota = AudioSystem.getClip();
                clipRebotPilota.open(audioIn);
                clipRebotPilota.start();
            } else {
                System.err.println("No s'ha trobat l'arxiu de so de rebot de pilota.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reprodueix el so de Game Over
    public static void reproducirGameOver() {
        try {
            URL gameOver = Sound.class.getResource("/resources/sonidos/gameOver1.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(gameOver);
            clipGameOver = AudioSystem.getClip();
            clipGameOver.open(audioIn);

            // Ajusta el volum si és possible
            if (clipGameOver.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clipGameOver.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(+6.0f);
            }

            clipGameOver.start(); // Inicia el so de Game Over
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Executa la seqüència Game Over + música final
    public static void gameOver() {
        // Atura la música de fons si està sonant
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
            musicaFondo.close();
        }

        reproducirGameOver(); // So de Game Over

        try {
            Thread.sleep(2000); // Espera 2 segons
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reproducirMusicaFinal(); // Reprodueix música final en bucle
    }

    // Reprodueix la música final del joc
    public static void reproducirMusicaFinal() {
        try {
            URL musica = Sound.class.getResource("/resources/sonidos/musicaFinal.wav");
            if (musica != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musica);
                musicaFondo = AudioSystem.getClip();
                musicaFondo.open(audioIn);
                musicaFondo.loop(Clip.LOOP_CONTINUOUSLY); // En bucle fins que es tanqui el joc
                musicaFondo.start();
            } else {
                System.err.println("No s'ha trobat l'arxiu de música final.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
