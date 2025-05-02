package Objectes;

import java.awt.Graphics2D;
import javax.swing.JPanel;
import LogicaJoc.Logica;
import Sonido.Sound;

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
       //Ubicacio de la imatge per la pala
       imatgePilota = new ImageIcon(getClass().getResource("/resources/imagenes/pelotaTenis.png")).getImage();
    }
    
    //Metode amb la logica de moviment de la pilota
    public void movimentPilota() {
        Boolean cambiarDireccion = true;
        if (x + xa * logica.getIncrementVelocitat()< 0)
            xa = 1;
        else if (x + xa * logica.getVelocitat() > logica.getWidth() - DIAMETRE_PILOTA)
            xa = -1;
        else if (y + ya * logica.getVelocitat() < 0)
            ya = 1;
        //Colisio nomes cuan la pilota esta descendent per evitar doble rebot
        else if (collision() && ya > 0) {
            Rectangle raqueta = logica.r1.getBounds();
            int raquetaCenter = raqueta.x + raqueta.width / 2;
            int pilotaCenter = x + DIAMETRE_PILOTA / 2;
            // Rebot vertical cap a adal
            ya = -1;
            // Calculem offset (punt al moment de la coliso)entre el centre de la raqueta i el centre de la pilota
            int offset = pilotaCenter - raquetaCenter;
            // Escalem el offset para que no sigui massa gran
            xa = offset / (raqueta.width / 10);
            //Evitem que la pilota reboti recte 
            if (xa == 0) xa = (rand.nextBoolean()) ? 1 : -1;
            // Reposiciona la pilota sobre la raqueta
            y = raqueta.y - DIAMETRE_PILOTA;
            //
        } else {
            cambiarDireccion = false;
        }
        if (cambiarDireccion)
            Sonido.Sound.reproducirRebotarPilota();
        // Game over si la pilota toca la part inferior 
        if (y + ya * logica.getVelocitat() > logica.getHeight() - DIAMETRE_PILOTA)
            logica.gameOver();
        // Actualitza la posicio
        x += xa * logica.getVelocitat();
        y += ya * logica.getVelocitat();
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
