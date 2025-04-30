
package BBDD;

import java.awt.Frame;
//import java.awt.GridLayout;
import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableCellRenderer;
import LogicaJoc.Logica;

public class Conexio {
    private Connection con;
    private final Logica game;
    private static final String URL = "jdbc:mysql://localhost:3306/pong";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Conexio(Logica game) {
        this.game = game;
    }

    public void conectar() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la BD");
        } catch (SQLException e) {
            System.err.print("Error conectando a la BD: " + e.getMessage());
        }
    }

    public void insertarPuntuacion(String nombre, int score) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);  // Deshabilitar auto-commit

            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO puntuacion (nom, score, fecha) VALUES (?, ?, ?)")) {

                pstmt.setString(1, nombre);
                pstmt.setInt(2, score);
                pstmt.setString(3, LocalDateTime.now().format(DATE_FORMATTER));
                pstmt.executeUpdate();

                conn.commit();  // Confirmar cambios

            } catch (SQLException e) {
                conn.rollback();  // Revertir en caso de error
                throw e;
            }
        } catch (SQLException e) {
            System.err.print("Error guardando puntuación: " + e.getMessage());
            
        }
    }

    /*public void mostrarPuntuaciones() {
        conectar(); // Abrir conexión dentro del hilo
        String[][] datos = obtenerPuntuaciones();
        desconectar(); // Cerrar después de obtener datos
        mostrarVentanaPuntuaciones(datos);
    }*/

    public String[][] obtenerPuntuaciones() {
        String[][] datos = new String[10][4];
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = conn.createStatement()) {

            String sql
                    = "SELECT * FROM ("
                    + "   SELECT nom, score, "
                    + "          DATE_FORMAT(fecha, '%Y-%m-%d %H:%i') as fecha_formateada, "
                    + "          fecha "
                    + // Mantener la columna original para ordenar
                    "   FROM puntuacion "
                    + "   ORDER BY fecha DESC "
                    + "   LIMIT 20"
                    + ") AS recientes "
                    + "ORDER BY score DESC, fecha DESC "
                    + // Usar la columna original
                    "LIMIT 10";

            try (ResultSet rs = stmt.executeQuery(sql)) {
                int i = 0;
                while (rs.next() && i < 10) {
                    datos[i][0] = rs.getString("nom") != null ? rs.getString("nom") : "Anónimo";
                    datos[i][1] = String.valueOf(rs.getInt("score"));
                    datos[i][2] = rs.getString("fecha_formateada"); // Usar el alias para mostrar
                    i++;
                }
            }

        } catch (SQLException e) {
            return null;
            //NO S'HA DE FER UN OPTIONPANE, SI NO RETORNAR ERROR I A LA PART VISUAL TRACTAR-LO
            
            /*JOptionPane.showMessageDialog(null,
                    "Error actualizando puntuaciones: " + e.getMessage(),
                    "Error de BD",
                    JOptionPane.ERROR_MESSAGE);
            */
        }
        return datos;
    }

    //AQUEST MÈTODE NO VA AQUÍ, JA QUE AQUESTA CLASSE S'ENCARREGA D'ÚNICAMENT
    //LA CONNEXIO A BASE DE DADES
    /*
    private void mostrarVentanaPuntuaciones(String[][] datos) {
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = new JDialog((Frame) null, "Top 10 Puntuaciones", false); // Usar JDialog modal
            JTable tabla = new JTable(datos, new String[]{"Jugador", "Puntos", "Fecha"});

            // Configuración de la tabla
            tabla.setAutoCreateRowSorter(true);
            tabla.setFillsViewportHeight(true);

            // Centrar contenido
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < tabla.getColumnCount(); i++) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // Añadir scroll
            JScrollPane scrollPane = new JScrollPane(tabla);
            dialog.add(scrollPane);

            // Configuración del diálogo
            dialog.setSize(800, 300);
            dialog.setLocationRelativeTo(game);
            dialog.setAlwaysOnTop(true); // <--- Forzar estar al frente
            dialog.setModal(true); // Bloquear interacción con otras ventanas
            dialog.setVisible(true);
        });
    }
*/
    
    //obtenir traducció
    public String obtenirTraduccio(String clau, String idioma){
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = conn.createStatement()) {
            String sql
                    = "   SELECT Catala, Angles " +
                    "   FROM Traduccio "
                    + "   WHERE Traduccio_Key = '" + clau + "'"
                    ;

            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()){
                    if ("Catala".equals(idioma)){
                    //String traduccioCatala = rs.getString("Catala");
                        return rs.getString("Catala");
                    }else{
                    //String traduccioAngles = rs.getString("Angles");
                        return rs.getString("Angles");
                    }
                }else{
                    return "Traduccio no trobada";
                }
            } catch (SQLException e) {
                return null;
            }
        }catch (SQLException e){
            return null;
        }
    }
    
    
    public void desconectar() {
        try {
            if (con != null) {
                con.close();
            }
            System.out.println("Conexión cerrada");
        } catch (SQLException e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}
