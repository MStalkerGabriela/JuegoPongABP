package Objectes;

import java.awt.Graphics2D;
import javax.swing.JPanel;
import LogicaJoc.Logica;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;


public class Raqueta extends JPanel {
    private static final int Y = 330;
    private static final int AMPLE_RAQUETA = 60;
    private static final int ALT_RAQUETA = 10;
    private int x = 130;
    private int xa = 0;
    private Logica logica;
    private Image raqueta;
    
    public Raqueta(Logica logica){
        this.logica = logica;
        //Ubicacio de la imatge per la pala
        raqueta = new ImageIcon(getClass().getResource("/resources/imagenes/raquetaTenis.png")).getImage();
    }
    
    //Metode amb la logica de moviment de la raqueta
    public void movimentRaqueta(){
        if (x + xa > 0 && x + xa < logica.getWidth()-AMPLE_RAQUETA)
	x = x + xa;
    }
    public void paint(Graphics2D g2d) {
        //g2d.fillRect(x, Y, AMPLE_RAQUETA, ALT_RAQUETA);
        g2d.drawImage(raqueta, x, Y, AMPLE_RAQUETA, ALT_RAQUETA, null);
    }
    
    public void keyReleased(KeyEvent e) {
        xa = 0;
    }
    
    //*****Deteccio de tecla presionada(AMPLIACIO: TAMBE FUNCIONA WASD***
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT||e.getKeyCode() == KeyEvent.VK_A)
        xa = -(int)logica.getVelocitat();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT||e.getKeyCode() == KeyEvent.VK_D)
        xa = (int)logica.getVelocitat();
    }
    
    public Rectangle getBounds(){

        return new Rectangle (x,Y,AMPLE_RAQUETA,ALT_RAQUETA);
    }
    
    public int getTopY(){
        return Y - ALT_RAQUETA;
    }
}
