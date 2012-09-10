/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.Hashtable;

/**
 *
 * @author Andres
 */
public class ControladorProductos {
    
    public ControladorProductos(){
        
    }
    
    public boolean crear(String codigo, String marca, String categoria, String descripcion, Hashtable<String, String> componentes){
        
        return true;
    }
    
    public Hashtable<String, String> cargarDatos(String codigo){
        
        return null;
    }
    
    public Hashtable<String, String> cargarComponentes(String codigo){
        
        return null;
    }
    
    public boolean modificar(String codigo, String marca, String categoria, String descripcion, Hashtable<String, String> componentes){
        
        return true;
    }
    
    public boolean borrar(String codigo){
        
        return true;
    }
    
    
}
