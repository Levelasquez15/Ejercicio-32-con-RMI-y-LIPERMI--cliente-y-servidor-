package com.edad.rmi;

import com.edad.rmi.net.Servidor;

/**
 * Punto de entrada principal para ejecutar el Servidor RMI con LipeRMI.
 */
public class Principal {

    public static void main(String[] args) {
        Servidor servicio = new Servidor();
        try {
            servicio.iniciar();
            // Mantener el hilo principal activo para que el servidor permanezca a la escucha
            Thread.currentThread().join();
        } catch (Exception ex) {
            System.err.println("Error al iniciar el servidor: " + ex.getLocalizedMessage());
        }
    }
}
