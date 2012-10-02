/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemadeinventario.ConectionH;

/**
 *
 * @author Andres
 */
public class ControladorPrecios {

    private ControladorCaracteristicas caracteristicas;
    private ConectionH c;

    public ControladorPrecios(ControladorCaracteristicas caracteristicas) {
        this.caracteristicas = caracteristicas;
        this.c = new ConectionH();

    }

    public String cargar(String codigo, String talle) {
        Statement stmt = this.c.getStatement();
        String precio = "0";
        try {
            String select = "SELECT precio FROM precios "
                    + " WHERE codigo = '" + codigo + "' AND talle = '" + talle + "'";
            ResultSet rs = stmt.executeQuery(select);
            rs.last();
            if (rs.getRow() == 0) {
                stmt.executeUpdate("INSERT INTO precios (codigo, talle, precio) VALUES "
                        + "('" + codigo + "', '" + talle + "', '" + precio + "')");
            } else {
                rs.first();
                precio = rs.getString("precio");
            }
        } catch (Exception e) {
        }
        return precio;
    }

    public boolean modificar(String codigo, String talle, String precio) {
        Statement stmt = this.c.getStatement();
        String update = "UPDATE precios SET precio='" + precio
                + "' WHERE codigo='" + codigo + "' AND talle='" + talle + "'";
        try {
            String select = "SELECT precio FROM precios "
                    + " WHERE codigo = '" + codigo + "' AND talle = '" + talle + "'";
            ResultSet rs = stmt.executeQuery(select);
            rs.last();
            if (rs.getRow() == 0) {
                stmt.executeUpdate("INSERT INTO precios (codigo, talle, precio) VALUES "
                        + "('" + codigo + "', '" + talle + "', '" + precio + "')");
            }
            stmt.executeUpdate(update);
        } catch (SQLException ex) {
            Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
