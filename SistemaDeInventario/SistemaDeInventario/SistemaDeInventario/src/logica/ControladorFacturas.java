/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;
import sistemadeinventario.ConectionH;

/**
 *
 * @author Andres
 */
public class ControladorFacturas {

    private ControladorArticulos articulos;
    private ControladorCostos costos;
    private ConectionH c;

    public ControladorFacturas(ControladorArticulos articulos, ControladorCostos costos) {
        this.articulos = articulos;
        this.costos = costos;
        this.c = new ConectionH();
    }

    /**
     *
     */
    public boolean crear(String prov, String fac, String fecha, String mon, String tipop, String plazop, int iva, int desc, ArrayList<String[]> lineas) {
        String local = getLocal();
        Statement stmt = this.c.getStatement();
        String insert =
                "INSERT INTO facturas "
                + "(proveedor, factura, fecha, moneda, tipopago, plazopago, iva, descuentos) "
                + "VALUES ('" + prov + "', '" + fac + "', STR_TO_DATE(REPLACE('" + fecha + "','/','.') ,GET_FORMAT(date,'EUR')), '" + mon + "', '" + tipop + "', '" + plazop + "', '" + iva + "', '" + desc + "')";
        int clave;
        try {
            stmt.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            clave = rs.getInt(1);
            rs.close();
            int i = 0;
            for (String[] linea : lineas) {
                insert = "INSERT INTO linea_factura (factura, linea, cantidad, codigo, talle, color, precio) "
                        + "VALUES ('" + clave + "', '" + i + "', '" + linea[0] + "', '" + linea[1] + "', '" + linea[2] + "', '" + linea[3] + "', '" + linea[4] + "')";
                stmt.executeUpdate(insert);
                this.articulos.actualizarStock(linea[1], linea[2], linea[3], local, Integer.parseInt(linea[0]));
                this.costos.modificar(linea[1], linea[2], linea[4]);
                i++;
            }
        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(null, "Ocurrió un problema al crear la factura.", "Error!", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private String getLocal() {
        String local = "";
        try {
            Properties p = new Properties();
            p.load(new FileInputStream("C:\\Sistema de RossiSport\\params.ini"));
            local = p.getProperty("local");
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, "Ocurrió un problema", "Error al leer la configuración", JOptionPane.CLOSED_OPTION, JOptionPane.WARNING_MESSAGE);
        }
        return local;
    }
}
