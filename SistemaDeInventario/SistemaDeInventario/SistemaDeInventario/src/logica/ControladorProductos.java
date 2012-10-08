/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemadeinventario.ConectionH;

/**
 *
 * @author Andres
 */
public class ControladorProductos {

    private ControladorCaracteristicas caracteristicas;
    private ConectionH c;

    public ControladorProductos(ControladorCaracteristicas caracteristicas) {
        this.caracteristicas = caracteristicas;
        this.c = new ConectionH();
    }

    public boolean crear(String codigo, String marca, String categoria, String descripcion, Hashtable<String, String> componentes) {
        Statement stmt = this.c.getStatement();
        String insert = "INSERT INTO descripciones (codigo, marca, categoria, descripcion) VALUES "
                + "('" + codigo + "', '" + marca + "', '" + categoria + "', '" + descripcion + "')";
        Enumeration<String> keys = componentes.keys();
        String key;
        try {
            stmt.executeUpdate(insert);
            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                insert = "INSERT INTO composiciones (codigo, component, porcentaje) VALUES "
                        + "('" + codigo + "', '" + key + "', '" + componentes.get(key) + "')";
                stmt.executeUpdate(insert);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Actualiza las listas cargadas en memoria
        this.caracteristicas.initCaracteristicas();
        return true;
    }

    public Hashtable<String, String> cargarDatos(String codigo) {
        Hashtable<String, String> datos = new Hashtable<String, String>();
        Statement stmt = this.c.getStatement();
        String consulta = "SELECT marca, categoria, descripcion FROM descripciones "
                + " WHERE codigo = \"" + codigo + "\"";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(consulta);
            rs.first();
            datos.put("marca", rs.getString("marca"));
            datos.put("categoria", rs.getString("categoria"));
            datos.put("descripcion", rs.getString("descripcion"));
        } catch (SQLException ex) {
            Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datos;
    }

    public Hashtable<String, String> cargarComponentes(String codigo) {
        Hashtable<String, String> componentes = new Hashtable<String, String>();
        Statement stmt = this.c.getStatement();
        String consulta = "SELECT component, porcentaje FROM composiciones "
                + " WHERE codigo = \"" + codigo + "\"";
        ResultSet rs;
        try {
            rs = stmt.executeQuery(consulta);
            while (rs.next()) {
                componentes.put(rs.getString("component"), rs.getString("porcentaje"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return componentes;
    }

    public boolean modificar(String codigo, String marca, String categoria, String descripcion, Hashtable<String, String> componentes) {
        Statement stmt = this.c.getStatement();
        String update = "UPDATE descripciones SET marca='" + marca + 
                "', categoria='" + categoria + 
                "', descripcion='" + descripcion
                + "' WHERE codigo='" + codigo + "'";
        try {
            stmt.executeUpdate(update);
        } catch (SQLException ex) {
            Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
        }

        String select;
        Enumeration<String> keys = componentes.keys();
        String key;
        ResultSet rs;
        try {
            while (keys.hasMoreElements()) {
                key = keys.nextElement();
                select = "SELECT porcentaje FROM composiciones "
                        + " WHERE codigo = '" + codigo + "' AND component = '" + key + "'";
                rs = stmt.executeQuery(select);
                rs.last();
                if (rs.getRow() == 0) {
                    stmt.executeUpdate("INSERT INTO composiciones (codigo, component, porcentaje) VALUES "
                            + "('" + codigo + "', '" + key + "', '" + componentes.get(key) + "')");
                } else {
                    stmt.executeUpdate("UPDATE composiciones SET porcentaje='" + componentes.get(key)
                            + "' WHERE codigo='" + codigo + "' AND component='" + key + "'");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean eliminar(String codigo) {
        Statement stmt = this.c.getStatement();
        String delete = "DELETE FROM descripciones"
                + " WHERE codigo = \"" + codigo + "\"";
        try {
            stmt.executeUpdate(delete);
        } catch (SQLException ex) {
            Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Actualiza las listas cargadas en memoria
        this.caracteristicas.initCaracteristicas();
        return true;
    }
}
