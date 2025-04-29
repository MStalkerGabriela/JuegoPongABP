package Objectes;

import java.awt.Graphics2D;
import javax.swing.JPanel;
import LogicaJoc.Logica;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import java.util.Random;

public class Pilota extends JPanel {
    Random rand = new Random();
    private static final int DIAMETRE_PILOTA = 20;
    private int x = rand.nextInt(30, 320);
    private int y = 0;
    private int xa = 1;
    private int ya = 1;
    private Logica logica;
    private Image imatgePilota;

    public Pilota(Logica logica){
       this.logica=logica;
       imatgePilota = new ImageIcon(getClass().getResource("/resources/imagenes/pelotaTenis.png")).getImage();

    }

    public void movimentPilota() {
        // Rebote lateral
        if (x + xa * logica.velocitat < 0)
            xa = 1;
        if (x + xa * logica.velocitat > logica.getWidth() - DIAMETRE_PILOTA)
            xa = -1;

        // Rebote superior
        if (y + ya * logica.velocitat < 0)
            ya = 1;

        // Colisión con la raqueta
        if (collision()) {
            ya = -1;
            y = logica.r1.getTopY() - DIAMETRE_PILOTA;
        }

        // Game over si toca el suelo
        if (y + ya * logica.velocitat > logica.getHeight() - DIAMETRE_PILOTA)
            logica.gameOver();

        // Actualiza posición
        x += xa * logica.velocitat;
        y += ya * logica.velocitat;
    }


    private boolean collision() {
        return logica.r1.getBounds().intersects(getBounds());
	}

    public void paint(Graphics2D g2d) {
        g2d.fillOval(x, y, 20, 20);
        g2d.drawImage(imatgePilota, x, y, DIAMETRE_PILOTA, DIAMETRE_PILOTA, null);
    }

    public Rectangle getBounds() {
		return new Rectangle(x, y, DIAMETRE_PILOTA, DIAMETRE_PILOTA);
	}
}
