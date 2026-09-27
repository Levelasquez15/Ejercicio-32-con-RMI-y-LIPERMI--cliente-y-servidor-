# Parcial Sistemas Distribuidos - Ejercicio 32 (RMI con LipeRMI)
## Sistema Distribuido Cliente-Servidor para Cálculo de Edad en Días
### Docente: John Carlos Arrieta Arrieta | Solución por: Lewis Velásquez Watts

Este repositorio contiene la solución completa, modular y documentada para el **Ejercicio 32** de la asignatura **Sistemas Distribuidos**, implementado bajo la tecnología de **Invocación de Métodos Remotos (RMI - Remote Method Invocation)** utilizando la librería de terceros **LipeRMI** (`lipermi-1.0.1.jar`), siguiendo paso a paso los lineamientos y especificaciones de la foto-guía académica suministrada por el docente.

---

## 📐 Enunciado del Ejercicio 32 y Reglas de Negocio

El **Ejercicio 32** modela un sistema distribuido donde el cliente solicita el cálculo de los días aproximados vividos a partir de su edad en años cumplidos:

$$\text{días} = \text{edad} \times 365$$

* **Entrada**: Entero positivo mayor que cero (`edad > 0`).
* **Salida / Respuesta**: Objeto DTO `DatosEdad` con:
  * `edad`: Años cumplidos ingresados.
  * `resultadoDias`: Días calculados ($edad \times 365$).
  * `mensaje`: `"Estimación aproximada considerando 365 días por año"`.
* **Validaciones**: Si la edad ingresada es menor o igual a cero, o se introducen caracteres no numéricos, el sistema valida la entrada tanto en el cliente como en el servidor, emitiendo mensajes de error descriptivos y manteniendo la estabilidad del servicio.

---

## 📁 Estructura del Repositorio y Proyectos NetBeans

El sistema se desarrolló en **Apache NetBeans IDE (Java with Ant)** desacoplado en tres proyectos modulares independientes:

```text
parcial -ejercicio 32 - RMi/
├── lib/
│   └── lipermi-1.0.1.jar                  # Librería LipeRMI (componente externo)
├── LipeRmiLibEdad/                        # Proyecto 1 (Librería): Contrato y DTO
│   ├── build.xml
│   ├── nbproject/
│   └── src/com/edad/rmi/lib/
│       ├── DatosEdad.java                 # DTO serializable (edad, resultadoDias, mensaje)
│       └── IRemotaCalculoEdad.java        # Interfaz del servicio remoto (POJO limpio)
├── LipeRmiServidorEdad/                   # Proyecto 2 (Servidor): Lógica y Socket TCP
│   ├── build.xml
│   ├── nbproject/
│   └── src/com/edad/rmi/
│       ├── Principal.java                 # Clase principal que arranca el servicio
│       └── net/
│           ├── CalculoRmiEdadImplem.java  # Implementación del cálculo (edad * 365)
│           └── Servidor.java              # Gestión de CallHandler y Server.bind(9007)
├── LipeRmiClienteEdad/                    # Proyecto 3 (Cliente): Interfaz Gráfica Swing
│   ├── build.xml
│   ├── nbproject/
│   └── src/com/edad/rmi/
│       ├── Principal.java                 # Punto de entrada de la aplicación GUI
│       └── vistas/
│           └── VentanaPrincipal.java      # Formulario Swing (Tabs: CONEXIÓN y CALCULAR EDAD)
├── .gitignore
└── README.md
```

---

## 🛠️ Arquitectura de los Componentes

### 1. Proyecto Librería (`LipeRmiLibEdad`)
* **Tipo**: *Java Class Library* en NetBeans.
* **Clase `DatosEdad`**: Implementa `java.io.Serializable` para permitir su transferencia binaria por el socket de red. Posee los atributos `edad`, `resultadoDias`, `mensaje` con sus constructores, getters y setters.
* **Interfaz `IRemotaCalculoEdad`**: Define la firma remota del servicio:
  ```java
  public interface IRemotaCalculoEdad {
      public DatosEdad calcularDias(DatosEdad datos);
  }
  ```
  *(Al utilizar LipeRMI, la interfaz es un POJO puro: no requiere extender `java.rmi.Remote` ni declarar `throws RemoteException`).*

### 2. Proyecto Servidor (`LipeRmiServidorEdad`)
* **Dependencias**: Se agregan a las librerías del proyecto `lipermi-1.0.1.jar` y el JAR compilado `dist/LipeRmiLibEdad.jar`.
* **Clase `CalculoRmiEdadImplem`**: Implementa la interfaz `IRemotaCalculoEdad`. Valida que `edad > 0`, efectúa el cálculo `edad * 365L` y setea el mensaje informativo.
* **Clase `Servidor`**:
  * Configura el puerto TCP `9007`.
  * Instancia el despachador de llamadas `CallHandler`.
  * Registra la interfaz de forma global:
    ```java
    invocador.registerGlobal(IRemotaCalculoEdad.class, calculoEdad);
    servidor.bind(puerto, invocador);
    ```
* **Clase `Principal`**: Inicia el servidor y lo mantiene a la escucha mostrando el registro de peticiones en consola.

### 3. Proyecto Cliente (`LipeRmiClienteEdad`)
* **Interfaz Gráfica Swing**:
  * **Pestaña CONEXIÓN**: Campos para IP (`localhost`), puerto (`9007`), indicador visual de estado (*Desconectado/Conectado*) y botón *Conectar / Desconectar*.
  * **Pestaña CALCULAR EDAD**: Campo de texto para ingresar la edad, botón *CALCULAR*, etiqueta de resultado en días destacada en color rojo y cuadro de información con borde titulado.
* **Manejo Multihilo Concurrente**: La invocación remota se realiza dentro de un `Thread` secundario para evitar que el hilo gráfico principal (*Event Dispatch Thread*) se bloquee durante la comunicación de red, actualizando la GUI de forma segura con `SwingUtilities.invokeLater()`.

---

## 📊 Fundamento Teórico: LipeRMI vs. RMI Estándar de Java

Como parte de los objetivos académicos de la guía, se presenta el análisis comparativo entre la implementación con la librería **LipeRMI** y el esquema nativo de **Java RMI** (`java.rmi.*`):

| Criterio | RMI con Librería (`LipeRMI`) | RMI Estándar / Nativo de Java (`java.rmi.*`) |
| :--- | :--- | :--- |
| **Dependencia Externa** | Requiere el archivo JAR `lipermi-1.0.1.jar`. | Ninguna (estándar incluido en el JDK de Java). |
| **Definición de Interfaz** | POJO limpio. No necesita heredar interfaces especiales. | Obligatorio extender `java.rmi.Remote`. |
| **Manejo de Excepciones** | Métodos limpios sin requerir `throws RemoteException`. | Todos los métodos deben declarar `throws RemoteException`. |
| **Implementación Servidor** | Clase ordinaria registrada en el `CallHandler`. | Debe heredar de `UnicastRemoteObject` (o usar `exportObject`). |
| **Registro de Nombres** | Conexión directa TCP mediante `Server.bind(puerto, handler)`. | Requiere `rmiregistry` o `LocateRegistry.createRegistry()`. |
| **Mecanismo Interno** | Proxies dinámicos (`java.lang.reflect.Proxy`) sobre TCP. | Stubs y Skeletons compilados sobre protocolo JRMP. |
| **Acoplamiento y Facilidad** | Muy bajo acoplamiento, configuración sencilla y rápida. | Mayor sobrecarga sintáctica y reglas rígidas del lenguaje. |

---

## 🚀 Guía de Ejecución en Apache NetBeans IDE

> 💡 **Nota sobre el Proyecto de tipo Librería (`LipeRmiLibEdad`)**:
> El proyecto `LipeRmiLibEdad` **NO se ejecuta** (no tiene método `main`), por lo que **no debes darle a "Run"**. En este proyecto **únicamente se hace clic derecho > Clean and Build** para compilar el componente JAR en la carpeta `dist/`.

---

### Pasos para Ejecutar:

1. Abrir **Apache NetBeans IDE**.
2. Ir a `File > Open Project...` (o `Ctrl + Shift + O`).
3. Abrir los 3 proyectos ubicados en el repositorio:
   * [LipeRmiLibEdad](file:///c:/Users/pc/Documents/Proyectos/parcial%20-ejercicio%2032%20-%20RMi/LipeRmiLibEdad)
   * [LipeRmiServidorEdad](file:///c:/Users/pc/Documents/Proyectos/parcial%20-ejercicio%2032%20-%20RMi/LipeRmiServidorEdad)
   * [LipeRmiClienteEdad](file:///c:/Users/pc/Documents/Proyectos/parcial%20-ejercicio%2032%20-%20RMi/LipeRmiClienteEdad)
4. Hacer clic derecho sobre **`LipeRmiLibEdad`** > **Clean and Build** (compila `dist/LipeRmiLibEdad.jar`).
5. Hacer clic derecho sobre **`LipeRmiServidorEdad`** > **Run** (o `Shift + F6` en [Principal.java](file:///c:/Users/pc/Documents/Proyectos/parcial%20-ejercicio%2032%20-%20RMi/LipeRmiServidorEdad/src/com/edad/rmi/Principal.java)).
   * *El servidor se iniciará en la consola `Output` de NetBeans escuchando en el puerto TCP `9007`.*
6. Hacer clic derecho sobre **`LipeRmiClienteEdad`** > **Run** (o `Shift + F6` en [Principal.java](file:///c:/Users/pc/Documents/Proyectos/parcial%20-ejercicio%2032%20-%20RMi/LipeRmiClienteEdad/src/com/edad/rmi/Principal.java)).
   * *Se abrirá la ventana gráfica del cliente.*
   * En la pestaña **CONEXIÓN**, presionar **Conectar** (el estado pasará a verde: *Conectado*).
   * En la pestaña **CALCULAR EDAD**, ingresar los años cumplidos (ej. `20`) y presionar **CALCULAR**. Verás el cálculo inmediato de días vividos: `7300 días`.
