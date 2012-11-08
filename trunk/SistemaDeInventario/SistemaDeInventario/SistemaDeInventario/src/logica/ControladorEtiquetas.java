/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Andres
 */
public class ControladorEtiquetas {

    public static boolean imprimir(String archivo, String codigo, String descripcion, String precio, String talle, String color, int cantidad) {
        ArrayList<String> resultado = new ArrayList<String>();

        try {
            // Abrimos el archivo
            FileInputStream fstream = new FileInputStream("C:\\Sistema de RossiSport\\template.txt");
            // Creamos el objeto de entrada
            DataInputStream entrada = new DataInputStream(fstream);
            // Creamos el Buffer de Lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String strLinea;
            // Leer el archivo linea por linea
            while ((strLinea = buffer.readLine()) != null) {
                // Imprimimos la línea por pantalla
                resultado.add(strLinea);
            }
            // Cerramos el archivo
            entrada.close();
        } catch (Exception e) { //Catch de excepciones
        }

        // Formateo de los datos
        // Formateo de la barra
        String barra = codigo + talle;
        while(barra.length() < 8) {
            barra += " ";
        }
        barra += color;
        while(barra.length() < 13) {
            barra += " ";
        }
        // Formateo de la descripción
        String desc1 = "";
        String desc2 = "";
        if(descripcion.length() > 35){
            String[] palabras = descripcion.split(" ");
            int i = 0;
            while(i < palabras.length && desc1.length() + palabras[i].length() < 35){
                desc1 += palabras[i] + " ";
                i++;
            }
            while(i < palabras.length && desc2.length() + palabras[i].length() < 35){
                desc2 += palabras[i] + " ";
                i++;
            }
        } else {
            desc1 = descripcion;
        }
        
        // Formateo de la salida
        String aux;
        for (int i = 0; i < resultado.size(); i++) {
            aux = resultado.get(i);
            aux = aux.replace("<desc1>", desc1);
            aux = aux.replace("<desc2>", desc2);
            aux = aux.replace("<barra>", barra);
            aux = aux.replace("<precio>", precio);
            aux = aux.replace("<cant>", "" + cantidad);
            resultado.set(i, aux);
        }

        FileWriter fichero = null;
        PrintWriter pw;
        try {
            fichero = new FileWriter(archivo);
            pw = new PrintWriter(fichero);

            for (int i = 0; i < resultado.size(); i++) {
                pw.println(resultado.get(i));
            }
            pw.close();
            fichero.close();

        } catch (Exception e) {
            System.out.println("FUUUUU");
        } finally {
            try {
                // Nuevamente aprovechamos el finally para 
                // asegurarnos que se cierra el fichero.
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                System.out.println("FUUUUU");
            }
        }
        return true;
    }
}
