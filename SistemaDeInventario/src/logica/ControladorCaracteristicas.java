/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import sistemadeinventario.ConectionH;

/**
 *
 * @author Andres
 */
public class ControladorCaracteristicas {

    private Hashtable<String, List> caracteristicas;
    
    public ControladorCaracteristicas() {
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
            "componentes"
        };
        ConectionH c = new ConectionH();
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
                e.printStackTrace();
            }
        }
    }

    public List<String> getCaracteristica(String caracteristica) {
        return this.caracteristicas.get(caracteristica);
    }
    
    public boolean existeElementoCaracteristica(String elemento, String caracteristica){
        List a = this.caracteristicas.get(caracteristica);
        return a.contains(elemento);
    }
    
}
