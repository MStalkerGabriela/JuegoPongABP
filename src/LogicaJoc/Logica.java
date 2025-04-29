/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicaJoc;

import javax.swing.JPanel;
import Objectes.Pilota;
import Objectes.Raqueta;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Logica extends JPanel {
    public Pilota p1 = new Pilota(this);
    public Raqueta r1 = new Raqueta(this);
    public final int VELOCITAT_INICIAL = 1;
    public long ultimTemps = System.currentTimeMillis();
    public int nivell = 1;
    public double velocitat = VELOCITAT_INICIAL;
    public int puntuacio = 0; 
    public long ultimTempsPuntuacio = System.currentTimeMillis(); 
    //Percentatge de nivell d'aument de velocitat
    double incrementVelocitat = 0.5;
    //Para anñadir el fondo 
    private Image pista;
    private Image pista2;
    private Image pista3;

    public Logica() {
        //mas para añadir fondo
        pista = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis.png")).getImage();
        pista2 = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis2.png")).getImage();
        pista3 = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis3.png")).getImage();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                    r1.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                    r1.keyPressed(e);
            }
        });
        setFocusable(true);
    }
    
    //Logica de puntuacio 
    private void comprobarTemps() {
    long tempsActual = System.currentTimeMillis();
    //Cada milesima puja la puntuacio
    if (tempsActual - ultimTempsPuntuacio >= 1) { 
        puntuacio++;
        ultimTempsPuntuacio = tempsActual;
    }
    
    // Cada 20 segons aumenta el nivell y velocitat
    if (tempsActual - ultimTemps >= 20000) {
        nivell++;
        velocitat = VELOCITAT_INICIAL * (1 + incrementVelocitat * nivell);
        ultimTemps = tempsActual;
        }
    }
    
    private int getPuntuacio(){
        return puntuacio;
    }


    private void moviment(){
        p1.movimentPilota();
        r1.movimentRaqueta();
        comprobarTemps();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
        //Els fons canvia cada 5 nivells
        g2d.drawImage(pista, 0, 0, getWidth(), getHeight(),null);
        if (nivell >= 5){
            g2d.drawImage(pista2, 0, 0, getWidth(), getHeight(),null);
        }if (nivell >= 10){
            g2d.drawImage(pista3, 0, 0, getWidth(), getHeight(),null);
        }

        p1.paint(g2d);
        r1.paint(g2d);
        //mostar per pantalla nivell actual 
        g2d.setColor(Color.magenta);
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString("Puntuació: " + getPuntuacio(), 10, 40);
        g2d.drawString("Nivell: " + nivell, 10, 60);
         
    }
    
    // Mostra el misatge de Game Overs
    public void gameOver(){
        JOptionPane.showMessageDialog(this, "Game Over \nLa teva puntuació es: "+getPuntuacio(), "Game Over", JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }
    
    public static void main(String[] args) throws InterruptedException {
        //Inicio del juego
        String name = JOptionPane.showInputDialog("Introduce tu nombre");
        JOptionPane.showMessageDialog(null, "Bienvenido " + name);
        //Todo IMPLEMENTAR EN EL CODGIO DE GIT HUB

        //Normas del Juego
        JOptionPane.showMessageDialog(null, "<html>" +
                "• Hauràs de mantenir la pilota en joc el <font color='red'><u>màxim</u></font> temps possible.<br><br>" +
                "• La <font color='red'> puntuació </font> és adequada al temps transcorregut en <font color='red'> mil·lisegons.</font><br><br>" +
                "• Cada <font color='red'>20 segons: </font><br><br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;• La velocitat de la pilota anirà en <b><font color='red'>augment.</font></b><br><br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;• Perill <b><font color='red'> d'obstacles!!!</font></b>" +
                "</html>");
        
        JFrame frame = new JFrame("Mini Tennis");
        Logica l1 = new Logica();
        frame.add(l1);
        try {
            String nivellInicial = JOptionPane.showInputDialog("Selecciona el nivell inicial");
            if (nivellInicial != null) {
                int nivellUsuari = Integer.parseInt(nivellInicial);
                l1.nivell = nivellUsuari;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nivell no vàlid. S'establirà el nivell 1.");
        }
        l1.velocitat = l1.VELOCITAT_INICIAL * (1 + l1.incrementVelocitat * l1.nivell);

        frame.setSize(350, 400);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
            l1.moviment();
            l1.repaint();
            Thread.sleep(10);
        }
    }
}
