package com.edad.rmi.net;

import java.io.IOException;
import com.edad.rmi.lib.IRemotaCalculoEdad;
import net.sf.lipermi.exception.LipeRMIException;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Server;

/**
 * Clase que gestiona el ciclo de vida del servidor RMI usando la librería LipeRMI.
 */
public class Servidor {

    private int puerto = 9007;
    private CallHandler invocador;
    private Server servidor;
    private CalculoRmiEdadImplem calculoEdad;
    private IRemotaCalculoEdad calculoEdadRemoto;

    public Servidor() {
        this(9007);
    }

    public Servidor(int puerto) {
        this.puerto = puerto;
        invocador = new CallHandler();
        servidor = new Server();
        calculoEdad = new CalculoRmiEdadImplem();
    }

    public void iniciar() throws Exception {
        try {
            invocador.registerGlobal(IRemotaCalculoEdad.class, calculoEdad);
            servidor.bind(puerto, invocador);
            System.out.println("==================================================");
            System.out.println("  SERVIDOR RMI (LipeRMI) INICIADO CORRECTAMENTE   ");
            System.out.println("  Puerto de escucha: " + puerto);
            System.out.println("  Interfaz remota  : IRemotaCalculoEdad           ");
            System.out.println("  Esperando peticiones de clientes...            ");
            System.out.println("==================================================");
        } catch (LipeRMIException ex) {
            throw new Exception("Error: No es posible invocar metodos remotos - " + ex.getMessage());
        } catch (IOException ex) {
            throw new Exception("Error: I/O en puerto " + puerto + " - " + ex.getMessage());
        }
    }

    public void detener() {
        if (servidor != null) {
            servidor.close();
            System.out.println("[SERVIDOR] Servidor LipeRMI detenido.");
        }
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
}
