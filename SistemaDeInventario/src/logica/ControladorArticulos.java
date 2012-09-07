/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sistemadeinventario.ConectionH;

/**
 *
 * @author Andres
 */
public class ControladorArticulos {

    public static final int OK = 0;
    public static final int CODIGO_INCORRECTO = 1;
    
    private ControladorCaracteristicas caracteristicas;
    
    public ControladorArticulos() {
        caracteristicas = ControladorCaracteristicas.getInstance();
    }

    /*
     * Metodo responsable de realizar las consultas de stock
     */
    public String[][] consultar(String codigo, String talle, String color, String local) {
        String[][] resultado = new String[][]{};
        ConectionH c = new ConectionH();
        Statement stmt = c.getStatement();
        ResultSet rs;
        String consulta = "SELECT * FROM articulos";
        if (!codigo.equals("") || !talle.equals("") || !color.equals("") || !local.equals("")) {
            boolean solo_uno = true;
            consulta += " WHERE";
            if (!codigo.equals("")) {
                consulta += " codigo = \"" + codigo + "\"";
                solo_uno = false;
            }
            if (!talle.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " talle = \"" + talle + "\"";
                solo_uno = false;
            }
            if (!color.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " color = \"" + color + "\"";
                solo_uno = false;
            }
            if (!local.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " local = \"" + local + "\"";
            }
        }
        try {
            rs = stmt.executeQuery(consulta);
            rs.last();
            int rowCount = rs.getRow();
            if (rowCount != 1) {
                resultado = new String[rowCount + 1][5];
            } else {
                resultado = new String[rowCount][5];
            }
            int suma = 0;
            rs.first();
            for (int i = 0; i < rowCount; i++, rs.next()) {
                resultado[i][0] = rs.getString("codigo");
                resultado[i][1] = rs.getString("talle");
                resultado[i][2] = rs.getString("color");
                resultado[i][3] = rs.getString("local");
                resultado[i][4] = rs.getString("stock");
                suma += Integer.parseInt(rs.getString("stock"));
            }
            if (rowCount != 1) {
                if (!codigo.equals("")) {
                    resultado[rowCount][0] = codigo;
                } else {
                    resultado[rowCount][0] = "*";
                }
                if (!talle.equals("")) {
                    resultado[rowCount][1] = talle;
                } else {
                    resultado[rowCount][1] = "*";
                }
                if (!color.equals("")) {
                    resultado[rowCount][2] = color;
                } else {
                    resultado[rowCount][2] = "*";
                }
                if (!local.equals("")) {
                    resultado[rowCount][3] = local;
                } else {
                    resultado[rowCount][3] = "*";
                }
                resultado[rowCount][4] = String.valueOf(suma);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public int ingresar(String codigo, String talle, String color, String local, String cantidad) {
        
        return OK;
    }
}
