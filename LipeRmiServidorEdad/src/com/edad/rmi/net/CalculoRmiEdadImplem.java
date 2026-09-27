package com.edad.rmi.net;

import com.edad.rmi.lib.DatosEdad;
import com.edad.rmi.lib.IRemotaCalculoEdad;

/**
 * Implementación remota de la interfaz IRemotaCalculoEdad.
 * Procesa el cálculo de edad en días usando LipeRMI.
 */
public class CalculoRmiEdadImplem implements IRemotaCalculoEdad {

    public CalculoRmiEdadImplem() {
    }

    @Override
    public DatosEdad calcularDias(DatosEdad datos) {
        if (datos == null) {
            datos = new DatosEdad();
            datos.setResultadoDias(0);
            datos.setMensaje("ERROR: Datos no proporcionados.");
            return datos;
        }

        if (datos.getEdad() <= 0) {
            datos.setResultadoDias(0);
            datos.setMensaje("ERROR: La edad debe ser un entero positivo mayor que cero.");
            System.out.println("[SERVIDOR] Solicitud rechazada: edad inválida (" + datos.getEdad() + ")");
            return datos;
        }

        // Cálculo del ejercicio 32: edad en años * 365 días
        long dias = (long) datos.getEdad() * 365L;
        datos.setResultadoDias(dias);
        datos.setMensaje("Estimación aproximada considerando 365 días por año");

        System.out.println("[SERVIDOR] Solicitud atendida -> Edad: " + datos.getEdad() + " años | Días calculados: " + dias);
        return datos;
    }
}
