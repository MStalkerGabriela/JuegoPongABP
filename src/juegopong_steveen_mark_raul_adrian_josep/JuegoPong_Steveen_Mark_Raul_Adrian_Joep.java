
package juegopong_steveen_mark_raul_adrian_josep;

import BBDD.Conexio;
import LogicaJoc.Logica;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class JuegoPong_Steveen_Mark_Raul_Adrian_Joep {
    //Constants 
    public static final int AMPLE_FINESTRA = 350;
    public static final int ALT_FINESTRA = 400;

    public static void main(String[] args) throws InterruptedException {
        Sonido.Sound.reproducirMusicaMenu();
        Conexio conexio = new Conexio(null);
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
        // Si l'usuari no selecciona ningun idioma
        JOptionPane.showMessageDialog(null, "No s'ha seleccionat cap idioma.");
        System.exit(0);
        }

        String idioma = (idiomaSeleccionat == 0) ? "Catala" : "Angles";
        
         //Incici del joc
        String name = null;
        do {
            name = JOptionPane.showInputDialog(conexio.obtenirTraduccio("INSERT_NAME_TEXT", idioma));
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, conexio.obtenirTraduccio("WORNG_NAME_TEXT", idioma));
                System.exit(0);
            }
        } while (name.trim().isEmpty());

        JOptionPane.showMessageDialog(null, conexio.obtenirTraduccio("WELCOME_TEXT", idioma) + " " + name);

        //Normes del Joc
        JOptionPane.showMessageDialog(null,conexio.obtenirTraduccio("RULES_TEXT", idioma));

        JFrame frame = new JFrame("Mini Tennis");
        Logica l1 = new Logica(name, idioma);
        frame.add(l1);
        frame.setSize(AMPLE_FINESTRA, ALT_FINESTRA);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        l1.requestFocusInWindow();

        try {
            String nivellInicial = JOptionPane.showInputDialog(conexio.obtenirTraduccio("SELECT_LEVEL_TEXT", idioma));
            if (nivellInicial != null) {
                int nivellUsuari = Integer.parseInt(nivellInicial);
                l1.setNivell(nivellUsuari);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, conexio.obtenirTraduccio("WRONG_SELECT_LEVEL_TEXT", idioma));
        }

        Sonido.Sound.reproducirMusicaFondo();
        l1.setVelocitat(l1.getVELOCITAT_INICIAL() * (1 + l1.getIncrementVelocitat() * l1.getNivell()));

        while (true) {
            l1.moviment();
            l1.repaint();
            Thread.sleep(10);
        }
    }
    
}
