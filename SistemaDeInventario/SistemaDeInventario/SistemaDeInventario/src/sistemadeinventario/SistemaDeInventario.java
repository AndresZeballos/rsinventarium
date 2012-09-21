/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemadeinventario;

import presentacion.Principal;

/**
 *
 * @author Andres
 */
public class SistemaDeInventario {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Principal principal = new Principal();
        if (principal.getOk()) {
            principal.setVisible(true);
        } else {
            principal.dispose();
        }
    }
}
