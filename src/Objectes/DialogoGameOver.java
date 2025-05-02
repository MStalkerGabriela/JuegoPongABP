package Objectes;

import javax.swing.*;
import java.awt.*;
import BBDD.Conexio;

public class DialogoGameOver extends JDialog {

    public DialogoGameOver(JFrame parent, String[][] datosPuntuaciones, String idioma) {
        super(parent, true);
        setLayout(new BorderLayout(10, 10));

        Conexio conexio = new Conexio(null);

        setTitle(conexio.obtenirTraduccio("FINAL_GAME_TEXT", idioma));

        // Traducción de encabezados de tabla
        String[] columnNames = {
            conexio.obtenirTraduccio("PLAYER_TEXT", idioma),
            conexio.obtenirTraduccio("POINT_TEXT", idioma),
            conexio.obtenirTraduccio("DATE_TEXT", idioma)
        };

        JTable tabla = new JTable(datosPuntuaciones, columnNames);
        tabla.setEnabled(false);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabla.setRowHeight(25);
        tabla.setGridColor(Color.LIGHT_GRAY);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnSalir = new JButton(conexio.obtenirTraduccio("EXIT_BUTTON", idioma));
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);

        // Configuración del diálogo
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
