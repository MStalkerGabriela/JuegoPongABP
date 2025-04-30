package Objectes;

import javax.swing.*;
import java.awt.*;

public class DialogoGameOver extends JDialog{

    private boolean reiniciar=false;

    public DialogoGameOver(JFrame parent, String[][] datosPuntuaciones) {
        super(parent, "Fin del Juego", true);
        setLayout(new BorderLayout(10, 10));

        // Tabla de puntuaciones
        JTable tabla = new JTable(datosPuntuaciones, new String[]{"Jugador", "Puntos", "Fecha"});
        tabla.setEnabled(false);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabla.setRowHeight(25);
        tabla.setGridColor(Color.LIGHT_GRAY);

        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    
        JButton btnSalir = new JButton("Cerrar Juego");
        btnSalir.addActionListener(e -> System.exit(0));

        //panelBotones.add(btnReiniciar);
        panelBotones.add(btnSalir);

        add(panelBotones, BorderLayout.SOUTH);

        // Configuración del diálogo
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public boolean quiereReiniciar() {
        return reiniciar;
    }
}
