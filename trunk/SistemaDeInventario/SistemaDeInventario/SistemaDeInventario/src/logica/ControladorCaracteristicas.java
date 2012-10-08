/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import sistemadeinventario.ConectionH;

/**
 *
 * @author Andres
 */
public class ControladorCaracteristicas {

    private Hashtable<String, List> caracteristicas;
    private boolean ok;
    private String msg;

    public ControladorCaracteristicas() {
        msg = "";
        initCaracteristicas();
    }

    public void initCaracteristicas() {
        this.caracteristicas = new Hashtable<String, List>();
        String[] tablas = {
            // Caracteristicas propias de la descripción de los articulos
            /*
             "pinzas",
             "telas",
             "estaciones",
             "tipopantalon",
             "tipoestampado",
             "cuellos",
             "mangas",
             "tiporemera",
             "lisorayado",
             "tipocampera",
             "lisafantasia",
             "tiposuela",
             "tipotirador",
             "tipocinturon",
             "tipopack",
             "vbotones",
             "vbase",
             "cortolargo",
             */
            //
            "colores",
            "talles",
            "locales",
            "marcas",
            "categorias",
            "descripciones",
            "componentes",
            // Controlador de pago de servicio
            "meses"
        };
        ConectionH c = new ConectionH();
        if (!c.getOk()) {
            this.msg = "Problema de conectividad!";
            this.ok = false;
            return;
        }
        Statement stmt = c.getStatement();
        ResultSet rs;
        List<String> lista;
        for (String tabla : tablas) {
            try {
                rs = stmt.executeQuery("SELECT * from " + tabla);
                lista = new ArrayList<String>();
                while (rs.next()) {
                    lista.add(rs.getString(1));
                }
                this.caracteristicas.put(tabla, lista);
            } catch (SQLException e) {
            }
        }
        Date d = new Date();
        String fecha;
        if ((d.getMonth() + 1) < 10) {
            fecha = "0" + (d.getMonth() + 1) + "/" + (d.getYear() + 1900);
        } else {
            fecha = "" + (d.getMonth() + 1) + "/" + (d.getYear() + 1900);
        }
        if (!this.existeElementoCaracteristica(fecha, "meses")) {
            this.ok = false;
            this.msg = "Error Fatal!!!";
            this.caracteristicas = new Hashtable<String, List>();
        } else {
            this.ok = true;
        }
    }

    public boolean getOk() {
        return this.ok;
    }

    public String getMsg() {
        return this.msg;
    }

    public List<String> getCaracteristica(String caracteristica) {
        return this.caracteristicas.get(caracteristica);
    }

    public boolean existeElementoCaracteristica(String elemento, String caracteristica) {
        List a = this.caracteristicas.get(caracteristica);
        return a.contains(elemento);
    }

    public boolean crear(String tabla, String elemento) {
        System.out.println(tabla + ";" + elemento);

        ConectionH c = new ConectionH();
        if (!c.getOk()) {
            this.msg = "Problema de conectividad!";
            this.ok = false;
            return false;
        }
        Statement stmt = c.getStatement();
        try {
            stmt.executeUpdate("INSERT INTO " + tabla + " VALUES ('" + elemento + "')");
        } catch (SQLException e) {
            this.msg = "Problema de inserción!\nNo esta permitida esta operación";
            this.ok = false;
            return false;
        }
        return true;
    }

    public boolean eliminar(String tabla, String elemento) {
        String columna = tabla.substring(0, tabla.length() - 1);
        if (columna.charAt(columna.length() - 1) == 'e') {
            columna = columna.substring(0, columna.length() - 1);
        }

        System.out.println(tabla + ";" + columna + ";" + elemento);
        ConectionH c = new ConectionH();
        if (!c.getOk()) {
            this.msg = "Problema de conectividad!";
            this.ok = false;
            return false;
        }
        Statement stmt = c.getStatement();
        try {
            stmt.executeUpdate("DELETE FROM " + tabla + " WHERE " + columna + " = '" + elemento + "'");
        } catch (SQLException e) {
            this.msg = "Problema de eliminación!\nNo esta permitida esta operación";
            this.ok = false;
            return false;
        }
        return true;
    }
}
