package com.edad.rmi.lib;

import java.io.Serializable;

/**
 * Clase para transportar los datos de entrada y resultado del cálculo de edad en días.
 * Implementa Serializable para permitir su transferencia remota vía RMI / LipeRMI.
 */
public class DatosEdad implements Serializable {

    private static final long serialVersionUID = 1L;

    private int edad;
    private long resultadoDias;
    private String mensaje;

    public DatosEdad() {
    }

    public DatosEdad(int edad) {
        this.edad = edad;
    }

    public DatosEdad(int edad, long resultadoDias, String mensaje) {
        this.edad = edad;
        this.resultadoDias = resultadoDias;
        this.mensaje = mensaje;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public long getResultadoDias() {
        return resultadoDias;
    }

    public void setResultadoDias(long resultadoDias) {
        this.resultadoDias = resultadoDias;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "DatosEdad{" + "edad=" + edad + ", resultadoDias=" + resultadoDias + ", mensaje=" + mensaje + '}';
    }
}
