package com.edad.rmi.lib;

/**
 * Interfaz remota para invocar los métodos de cálculo de edad.
 * En LipeRMI no requiere extender Remote ni declarar RemoteException.
 */
public interface IRemotaCalculoEdad {

    public DatosEdad calcularDias(DatosEdad datos);

}
