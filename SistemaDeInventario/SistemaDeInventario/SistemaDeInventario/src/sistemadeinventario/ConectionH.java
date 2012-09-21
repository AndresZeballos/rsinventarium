package sistemadeinventario;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import javax.swing.JOptionPane;

public class ConectionH {

    private Statement stmt;
    private Connection con;
    private String driver, jdbc, host, puerto, base, usuario, contraseña;

    public Statement getStatement() {
        return this.stmt;
    }

    /*
     public Statement getStatement2() {
     try {
     stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
     ResultSet.CONCUR_READ_ONLY);
     } catch (SQLException e) {
     e.printStackTrace();
     stmt = null;
     }
     return stmt;
     }*/
    private void leer_ini() {
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("C:\\Sistema de RossiSport\\params.ini"));
            driver = p.getProperty("driver");
            jdbc = p.getProperty("jdbc");
            host = p.getProperty("host");
            puerto = p.getProperty("puerto");
            base = p.getProperty("base");
            usuario = p.getProperty("user", "usuario");;
            contraseña = p.getProperty("data");
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, "Ocurrió un problema", "Error al leer la configuración", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }

    public ConectionH() {
        leer_ini();
        try {
            Class.forName(driver);
            String url = jdbc + host + puerto + base;// "jdbc:mysql://localhost:3306/rossisport";
            con = DriverManager.getConnection(url, usuario, contraseña);
            stmt = con.createStatement();
            /*
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/rossisport";
            con = DriverManager.getConnection(url, "root", "");
            stmt = con.createStatement();
            */
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, "Ocurrió un problema al conectarse a la base de datos", "Error en la conección", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
        }
    }

    public void Close() {
        try {
            con.close();
        } catch (SQLException e) {
        }
    }
}
