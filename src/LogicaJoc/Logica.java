
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

/**
 *
 * @author marks
 */
public class Logica extends JPanel {
    public Pilota p1 = new Pilota(this);
    public Raqueta r1 = new Raqueta(this);
    public final int VELOCITAT_INICIAL = 1;
    public long ultimTemps = System.currentTimeMillis();
    public int nivell = 1;
    public double velocitat = VELOCITAT_INICIAL;
    public int puntuacio = 0;
    public long ultimTempsPuntuacio = System.currentTimeMillis();
    public String playerName;
    private String idioma;
    //Percentatge de nivell d'aument de velocitat
    double incrementVelocitat = 0.5;
    //Para anñadir el fondo 
    private Image pista;
    private Image pista2;
    private Image pista3;
    private Conexio conexio;

    public Logica(String nomIntroduit, String idiomaIntroduit) {
        //mas para añadir fondo
        conexio=new Conexio(this);
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

        this.playerName=nomIntroduit;
        this.idioma=idiomaIntroduit;
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
    
    public int getPuntuacio(){
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
        g2d.drawString(conexio.obtenirTraduccio("SCORE_TEXT", idioma)+ " "+getPuntuacio(), 10, 40);
        //g2d.drawString("Puntuació: " + getPuntuacio(), 10, 40);
        g2d.drawString(conexio.obtenirTraduccio("LEVEL_TEXT", idioma)+ " "+nivell, 10, 60);
        //g2d.drawString("Nivell: " + nivell, 10, 60);
        g2d.drawString("Velocitat: " + velocitat, 10, 80);
         
    }
    
    // Mostra el misatge de Game Overs
    public void gameOver(){

        Sonido.Sound.gameOver();
        
        conexio.conectar();
        conexio.insertarPuntuacion(this.playerName, this.puntuacio);
        String[][] datos = conexio.obtenerPuntuaciones();
        conexio.desconectar();

        // Mostrar diálogo personalizado
        DialogoGameOver dialogo = new DialogoGameOver((JFrame) SwingUtilities.getWindowAncestor(this), datos);
        dialogo.setVisible(true);

        if (dialogo.quiereReiniciar()) {
            //reiniciarJuego();
        } else {
            System.exit(0);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Sonido.Sound.reproducirMusicaMenu();
        
        //Seleccio de idioma mitjançant boto
        String[] opcions = {"Català", "English"};
        int idiomaSeleccionat = JOptionPane.showOptionDialog(
        null,
        "Selecciona l'idioma:",
        "Idioma",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        opcions,
        opcions[0]
        );

        if (idiomaSeleccionat == -1) {
        // Usuario cerró el diálogo sin seleccionar
        JOptionPane.showMessageDialog(null, "No s'ha seleccionat cap idioma.");
        System.exit(0);
        }

        String idioma = (idiomaSeleccionat == 0) ? "Catala" : "Angles";
        
        //Inicio del juego
        String name = null;
        do {
        name = JOptionPane.showInputDialog("Introdueix el teu nom: ");
            if (name == null) {
                // Controlem si l'usuari no introdueix nom
                JOptionPane.showMessageDialog(null, "No es pot continuar sense nom! ");
                System.exit(0); 
            }
        } while (name.trim().isEmpty()); // También elimina espacios en blanco
        //String name = JOptionPane.showInputDialog("Introduce tu nombre");
        JOptionPane.showMessageDialog(null, "Benvingut " + name);

        //Normas del Joc
        JOptionPane.showMessageDialog(null, "<html>" +
                "• Hauràs de mantenir la pilota en joc el <font color='red'><u>màxim</u></font> temps possible.<br><br>" +
                "• La <font color='red'> puntuació </font> és adequada al temps transcorregut en <font color='red'> mil·lisegons.</font><br><br>" +
                "• Cada <font color='red'>20 segons: </font><br><br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;• La velocitat de la pilota anirà en <b><font color='red'>augment.</font></b><br><br>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;• Perill <b><font color='red'> d'obstacles!!!</font></b>" +
                "</html>");

        JFrame frame = new JFrame("Mini Tennis");
        Logica l1 = new Logica(name, idioma);
        frame.add(l1);
        frame.setSize(350, 400);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        l1.requestFocusInWindow();
        
        try {
            String nivellInicial = JOptionPane.showInputDialog("Selecciona el nivell inicial");
            if (nivellInicial != null) {
                int nivellUsuari = Integer.parseInt(nivellInicial);
                l1.nivell = nivellUsuari;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Nivell no vàlid. S'establirà el nivell 1.");
        }
        Sonido.Sound.reproducirMusicaFondo();
        l1.velocitat = l1.VELOCITAT_INICIAL * (1 + l1.incrementVelocitat * l1.nivell);
        
        while (true) {
            l1.moviment();
            l1.repaint();
            Thread.sleep(10);
        }
    }
}
