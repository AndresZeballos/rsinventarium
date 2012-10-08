/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sistemadeinventario.ConectionH;

/**
 *
 * @author Andres
 */
public class ControladorArticulos {

    public static final int OK = 0;
    public static final int CODIGO_INCORRECTO = 1;
    public static final int NO_NUMERICO = 2;
    private ControladorCaracteristicas caracteristicas;
    private ConectionH c;

    public ControladorArticulos(ControladorCaracteristicas caracteristicas) {
        this.caracteristicas = caracteristicas;
        this.c = new ConectionH();

    }

    /*
     * Metodo responsable de realizar las consultas de stock
     */
    public String[][] consultar(String codigo, String talle, String color, String local, String categoria, String marca, String tela) {
        String[][] resultado = new String[][]{};
        Statement stmt = this.c.getStatement();
        ResultSet rs;
        String consulta = "SELECT DISTINCT articulos.codigo, articulos.talle, articulos.color, articulos.local, articulos.stock FROM articulos, descripciones, composiciones";
        consulta += " WHERE articulos.codigo = descripciones.codigo AND articulos.codigo = composiciones.codigo";
        if (!codigo.equals("") || !talle.equals("") || !color.equals("") || !local.equals("") || !categoria.equals("") || !marca.equals("") || !tela.equals("")) {
            boolean solo_uno = true;
            consulta += " AND";
            if (!codigo.equals("")) {
                consulta += " articulos.codigo = \"" + codigo + "\"";
                solo_uno = false;
            }
            if (!talle.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " articulos.talle = \"" + talle + "\"";
                solo_uno = false;
            }
            if (!color.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " articulos.color = \"" + color + "\"";
                solo_uno = false;
            }
            if (!local.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " articulos.local = \"" + local + "\"";
                solo_uno = false;
            }
            if (!categoria.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " descripciones.categoria = \"" + categoria + "\"";
                solo_uno = false;
            }
            if (!marca.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " descripciones.marca = \"" + marca + "\"";
                solo_uno = false;
            }
            if (!tela.equals("")) {
                if (!solo_uno) {
                    consulta += " AND";
                }
                consulta += " composiciones.component = \"" + tela + "\"";
                //solo_uno = false;
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
        }
        return resultado;
    }

    public boolean actualizarStock(String codigo, String talle, String color, String local, int cantidad) {
        Statement stmt = this.c.getStatement();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT stock FROM articulos "
                    + " WHERE codigo = '" + codigo
                    + "' AND talle = '" + talle
                    + "' AND color = '" + color
                    + "' AND local = '" + local + "'");
            rs.last();
            if (rs.getRow() == 0) {
                stmt.executeUpdate("INSERT INTO articulos (codigo, talle, color, local, stock) VALUES "
                        + "('" + codigo + "', '" + talle + "', '" + color + "', '" + local + "', '" + cantidad + "')");
            } else {
                stmt.executeUpdate("UPDATE articulos SET stock = stock + " + cantidad + " WHERE codigo = \"" + codigo
                        + "\" AND talle = \"" + talle + "\" AND color = \"" + color + "\" AND local = \"" + local + "\"");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public boolean cargar(String archivo, boolean cargar) {
        ArrayList<String[]> csv = levantarCSV(archivo);
        if (!validarCSV(csv)) {
            return false;
        }
        Statement stmt = this.c.getStatement();
        String select, update;
        String[] linea = new String[6];
        ResultSet rs;
        for (int i = 0; i < csv.size(); i++) {
            try {
                linea = csv.get(i);
                select = "SELECT * FROM articulos "
                        + " WHERE codigo = '" + linea[0]
                        + "' AND talle = '" + linea[1]
                        + "' AND color = '" + linea[2]
                        + "' AND local = '" + linea[3] + "'";
                rs = stmt.executeQuery(select);
                rs.last();
                if (rs.getRow() == 0) {
                    stmt.executeUpdate("INSERT INTO articulos "
                            + "(codigo, talle, color, local, stock) VALUES "
                            + "('" + linea[0] + "', '" + linea[1] + "', '" + linea[2] + "', '" + linea[3] + "', '0')");
                }
                if (cargar) {
                    update = "UPDATE articulos SET stock = stock + " + linea[4]
                            + " WHERE codigo = '" + linea[0]
                            + "' AND talle = '" + linea[1]
                            + "' AND color = '" + linea[2]
                            + "' AND local = '" + linea[3] + "'";
                } else {
                    update = "UPDATE articulos SET stock = stock - " + linea[4]
                            + " WHERE codigo = '" + linea[0]
                            + "' AND talle = '" + linea[1]
                            + "' AND color = '" + linea[2]
                            + "' AND local = '" + linea[3] + "'";
                }
                stmt.executeUpdate(update);
                linea[5] = "OK";
            } catch (SQLException ex) {
                Logger.getLogger(ControladorArticulos.class.getName()).log(Level.SEVERE, null, ex);
                linea[5] = "NO";
            }
        }
        guardarCSV(archivo, csv);
        return true;
    }

    private ArrayList<String[]> levantarCSV(String archivo) {
        ArrayList<String[]> resultado = new ArrayList<String[]>();
        try {
            //create BufferedReader to read csv file
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String strLine;
            StringTokenizer st;
            int lineNumber = 0, tokenNumber = 0;
            //read comma separated file line by line
            while ((strLine = br.readLine()) != null) {
                //break comma separated line using ","
                st = new StringTokenizer(strLine, ",");
                resultado.add(new String[6]);
                while (st.hasMoreTokens()) {
                    //display csv values
                    resultado.get(lineNumber)[tokenNumber] = st.nextToken();
                    tokenNumber++;
                }
                //reset token number
                tokenNumber = 0;
                // Nueva linea
                lineNumber++;
            }
        } catch (Exception e) {
            System.out.println("Exception while reading csv file: " + e);
        }
        return resultado;
    }

    private boolean validarCSV(ArrayList<String[]> csv) {
        boolean resultado = true;
        String[] linea;
        for (int i = 0; i < csv.size(); i++) {
            linea = csv.get(i);
            resultado = resultado
                    && this.caracteristicas.existeElementoCaracteristica(linea[0], "descripciones");
        }
        return resultado;
    }

    private void guardarCSV(String archivo, ArrayList<String[]> resultado) {
        FileWriter fichero = null;
        BufferedWriter bw;
        String str;
        String[] linea;
        try {
            fichero = new FileWriter(archivo);
            bw = new BufferedWriter(fichero);
            for (int i = 0; i < resultado.size(); i++) {
                str = "";
                linea = resultado.get(i);
                for (int j = 0; j < linea.length; j++) {
                    str += linea[j] + ",";
                }
                bw.write(str + "\n");
            }
            bw.close();
        } catch (Exception e) {
        } finally {
            try {
                if (fichero != null) {
                    fichero.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
