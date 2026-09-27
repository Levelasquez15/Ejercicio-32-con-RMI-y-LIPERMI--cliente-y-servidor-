package com.edad.rmi.vistas;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

import com.edad.rmi.lib.DatosEdad;
import com.edad.rmi.lib.IRemotaCalculoEdad;
import net.sf.lipermi.handler.CallHandler;
import net.sf.lipermi.net.Client;

/**
 * Formulario Swing para la aplicación Cliente RMI con LipeRMI.
 * Adaptado según la guía de desarrollo distribuido del docente.
 */
public class VentanaPrincipal extends JFrame {

    private CallHandler invocadorRemoto;
    private String ipServidor = "localhost";
    private int puerto = 9007;
    private IRemotaCalculoEdad calculoEdadRemoto;
    private Client cliente;

    // Componentes de la interfaz
    private JLabel lblTitulo;
    private JTabbedPane jTabbedPane1;

    // Pestaña Conexión
    private JPanel panelConexion;
    private JLabel lblIp;
    private JTextField campoIPServidor;
    private JLabel lblPuerto;
    private JTextField campoPuertoServidor;
    private JLabel lblEstadoEtiqueta;
    private JLabel txtEstado;
    private JButton btnIniciar;

    // Pestaña Calcular Edad
    private JPanel panelCalculo;
    private JLabel lblEdad;
    private JTextField campoEdad;
    private JButton btnCalcular;
    private JLabel lblDiasEtiqueta;
    private JLabel txtResultado;
    private JLabel txtMensaje;

    public VentanaPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("CLIENTE EDAD - RMI (LipeRMI)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(480, 360);
        setLayout(null);

        lblTitulo = new JLabel("CLIENTE EDAD (LipeRMI)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setBounds(20, 15, 430, 30);
        add(lblTitulo);

        jTabbedPane1 = new JTabbedPane();
        jTabbedPane1.setBounds(20, 55, 430, 250);

        // ================= PESTAÑA 1: CONEXIÓN =================
        panelConexion = new JPanel();
        panelConexion.setLayout(null);

        lblIp = new JLabel("DIRECCIÓN IP:");
        lblIp.setBounds(30, 30, 120, 25);
        panelConexion.add(lblIp);

        campoIPServidor = new JTextField("localhost");
        campoIPServidor.setBounds(160, 30, 220, 25);
        panelConexion.add(campoIPServidor);

        lblPuerto = new JLabel("PUERTO DE RED:");
        lblPuerto.setBounds(30, 70, 120, 25);
        panelConexion.add(lblPuerto);

        campoPuertoServidor = new JTextField("9007");
        campoPuertoServidor.setBounds(160, 70, 220, 25);
        panelConexion.add(campoPuertoServidor);

        lblEstadoEtiqueta = new JLabel("ESTADO:");
        lblEstadoEtiqueta.setBounds(30, 115, 80, 25);
        panelConexion.add(lblEstadoEtiqueta);

        txtEstado = new JLabel("Desconectado");
        txtEstado.setFont(new Font("Tahoma", Font.BOLD, 13));
        txtEstado.setForeground(new Color(255, 0, 51));
        txtEstado.setBounds(160, 115, 200, 25);
        panelConexion.add(txtEstado);

        btnIniciar = new JButton("Conectar");
        btnIniciar.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnIniciar.setForeground(new Color(0, 153, 51));
        btnIniciar.setBounds(140, 160, 150, 35);
        btnIniciar.addActionListener(evt -> btnIniciarActionPerformed(evt));
        panelConexion.add(btnIniciar);

        jTabbedPane1.addTab("CONEXION", panelConexion);

        // ================= PESTAÑA 2: CALCULAR EDAD =================
        panelCalculo = new JPanel();
        panelCalculo.setLayout(null);

        lblEdad = new JLabel("EDAD (AÑOS):");
        lblEdad.setBounds(25, 25, 100, 25);
        panelCalculo.add(lblEdad);

        campoEdad = new JTextField();
        campoEdad.setBounds(125, 25, 120, 25);
        panelCalculo.add(campoEdad);

        btnCalcular = new JButton("CALCULAR");
        btnCalcular.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnCalcular.setForeground(new Color(0, 153, 51));
        btnCalcular.setBounds(265, 20, 135, 35);
        btnCalcular.addActionListener(evt -> btnCalcularActionPerformed(evt));
        panelCalculo.add(btnCalcular);

        lblDiasEtiqueta = new JLabel("DÍAS:");
        lblDiasEtiqueta.setBounds(25, 75, 80, 25);
        panelCalculo.add(lblDiasEtiqueta);

        txtResultado = new JLabel("0 días");
        txtResultado.setFont(new Font("Tahoma", Font.BOLD, 14));
        txtResultado.setForeground(new Color(255, 0, 51));
        txtResultado.setBounds(125, 75, 275, 25);
        panelCalculo.add(txtResultado);

        txtMensaje = new JLabel("Ingrese una edad en años y presione Calcular.");
        txtMensaje.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtMensaje.setBorder(BorderFactory.createTitledBorder("Información"));
        txtMensaje.setBounds(25, 120, 375, 60);
        panelCalculo.add(txtMensaje);

        jTabbedPane1.addTab("CALCULAR EDAD", panelCalculo);

        add(jTabbedPane1);
    }

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (btnIniciar.getText().equalsIgnoreCase("Conectar")) {
                puerto = Integer.parseInt(campoPuertoServidor.getText().trim());
                ipServidor = campoIPServidor.getText().trim();

                invocadorRemoto = new CallHandler();
                cliente = new Client(ipServidor, puerto, invocadorRemoto);
                calculoEdadRemoto = (IRemotaCalculoEdad) cliente.getGlobal(IRemotaCalculoEdad.class);

                btnIniciar.setText("Desconectar");
                btnIniciar.setForeground(new Color(255, 0, 51));
                txtEstado.setText("Conectado");
                txtEstado.setForeground(new Color(0, 153, 51));

                campoIPServidor.setEnabled(false);
                campoPuertoServidor.setEnabled(false);
                JOptionPane.showMessageDialog(this, "Conexión RMI establecida exitosamente con " + ipServidor + ":" + puerto, "Conectado", JOptionPane.INFORMATION_MESSAGE);
            } else if (btnIniciar.getText().equalsIgnoreCase("Desconectar")) {
                if (cliente != null) {
                    cliente.close();
                }
                calculoEdadRemoto = null;
                btnIniciar.setText("Conectar");
                btnIniciar.setForeground(new Color(0, 153, 51));
                txtEstado.setText("Desconectado");
                txtEstado.setForeground(new Color(255, 0, 51));

                campoIPServidor.setEnabled(true);
                campoPuertoServidor.setEnabled(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El puerto debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "ERROR AL CONECTAR al servidor RMI:\n" + ex.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {
        if (calculoEdadRemoto == null) {
            JOptionPane.showMessageDialog(this, "Primero debe conectarse al servidor en la pestaña CONEXIÓN.", "Sin Conexión", JOptionPane.WARNING_MESSAGE);
            jTabbedPane1.setSelectedIndex(0);
            return;
        }

        String textoEdad = campoEdad.getText().trim();
        if (textoEdad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese la edad en años.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int edad = Integer.parseInt(textoEdad);
            DatosEdad datos = new DatosEdad(edad);
            datos = calculoEdadRemoto.calcularDias(datos);
            txtResultado.setText(datos.getResultadoDias() + " días");
            txtMensaje.setText("<html>" + datos.getMensaje() + "</html>");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La edad debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR con el cliente: " + ex.getMessage(), "Error Remoto", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
