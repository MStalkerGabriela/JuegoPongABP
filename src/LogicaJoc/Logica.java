
package LogicaJoc;

import javax.swing.*;

import BBDD.Conexio;
import Objectes.Pilota;
import Objectes.Raqueta;
import Sonido.Sound;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Objectes.DialogoGameOver;

public class Logica extends JPanel {
    public Pilota p1 = new Pilota(this);
    public Raqueta r1 = new Raqueta(this);
    private final int VELOCITAT_INICIAL = 1;
    private long ultimTemps = System.currentTimeMillis();
    private int nivell = 1;
    private double velocitat = VELOCITAT_INICIAL;

    private int puntuacio = 0;
    private long ultimTempsPuntuacio = System.currentTimeMillis();
    private String playerName;
    private String idioma;
    //Percentatge de nivell d'aument de velocitat
    private double incrementVelocitat = 0.5;
    //Para anñadir el fondo 
    private Image pista;
    private Image pista2;
    private Image pista3;
    private Conexio conexio;

    public Logica(String nomIntroduit, String idiomaIntroduit) {
        this.playerName=nomIntroduit;
        this.idioma=idiomaIntroduit;
        conexio=new Conexio(this);
        //seleccionem la ruta de la imatge de fons
        pista = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis.png")).getImage();
        pista2 = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis2.png")).getImage();
        pista3 = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis3.png")).getImage();
        
        //Captacio tecla presionada 
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
    public void moviment(){
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
        //mostar per pantalla nivell puntuacio i velocitat acual
        g2d.setColor(Color.magenta);
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString(conexio.obtenirTraduccio("SCORE_TEXT", idioma)+ " "+getPuntuacio(), 10, 40);
        g2d.drawString(conexio.obtenirTraduccio("LEVEL_TEXT", idioma)+ " "+nivell, 10, 60);
        g2d.drawString(conexio.obtenirTraduccio("SPEED_TEXT", idioma)+ " "+velocitat, 10, 80 );    
    }
    
    // Mostra el misatge de Game Overs
    public void gameOver(){

        Sonido.Sound.gameOver();
        
        conexio.conectar();
        conexio.insertarPuntuacion(this.playerName, this.puntuacio);
        String[][] datos = conexio.obtenerPuntuaciones();
        conexio.desconectar();

        // Mostrar diálogo personalizado
        DialogoGameOver dialogo = new DialogoGameOver((JFrame) SwingUtilities.getWindowAncestor(this), datos, idioma);
        dialogo.setVisible(true);
    }
    
    //Getters
    public double getIncrementVelocitat() {
    return incrementVelocitat;
    }
        
    public double getVelocitat() {
    return velocitat;
    }
        
    public int getNivell() {
    return nivell;
    }
    
    public int getVELOCITAT_INICIAL() {
    return VELOCITAT_INICIAL;
    }
    
    public int getPuntuacio(){
    return puntuacio;
    }
    //Setters
    public void setNivell(int nivell) {
    this.nivell = nivell;
    }

    public void setVelocitat(double velocitat) {
    this.velocitat = velocitat;
    }
}