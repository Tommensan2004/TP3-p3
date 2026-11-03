package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import modelo.Grafo;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private PanelGrafo panelGrafo; // Componente para dibujar el grafo

    private JTextField txtCantVertices;
    private JButton btnCrearGrafo;

    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JButton btnAgregarArista;

    private JButton btnEjecutarGoloso;
    private JTextArea txtResultado;

    public MainFrame() {
        super("TP3 - Conjunto Dominante Mínimo (Algoritmo Goloso)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        //PANEL VISUAL DEL GRAFO
        panelGrafo = new PanelGrafo();
        panelGrafo.setBorder(BorderFactory.createTitledBorder("Visualización del Grafo"));
        add(panelGrafo, BorderLayout.CENTER);

        //PANEL DE CONTROLES
        JPanel panelIzquierdo = new JPanel(new GridLayout(3, 1, 5, 5));
        panelIzquierdo.setPreferredSize(new Dimension(300, 600));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inicialización del Grafo
        JPanel panelInicializacion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInicializacion.setBorder(BorderFactory.createTitledBorder("1. Crear Grafo"));
        panelInicializacion.add(new JLabel("Cant. Vértices:"));
        txtCantVertices = new JTextField(5);
        panelInicializacion.add(txtCantVertices);
        btnCrearGrafo = new JButton("Crear Grafo");
        panelInicializacion.add(btnCrearGrafo);

        // Aristas
        JPanel panelAristas = new JPanel(new GridLayout(3, 2, 5, 5));
        panelAristas.setBorder(BorderFactory.createTitledBorder("2. Cargar Aristas"));
        panelAristas.add(new JLabel("Origen:"));
        txtOrigen = new JTextField();
        panelAristas.add(txtOrigen);
        panelAristas.add(new JLabel("Destino:"));
        txtDestino = new JTextField();
        panelAristas.add(txtDestino);
        btnAgregarArista = new JButton("Agregar Arista");
        panelAristas.add(btnAgregarArista);

        // Ejecución
        JPanel panelEjecucion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEjecucion.setBorder(BorderFactory.createTitledBorder("3. Resolver"));
        btnEjecutarGoloso = new JButton("Calcular Dominante");
        panelEjecucion.add(btnEjecutarGoloso);

        panelIzquierdo.add(panelInicializacion);
        panelIzquierdo.add(panelAristas);
        panelIzquierdo.add(panelEjecucion);

        //PANEL INFERIOR CON TEXTO
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setPreferredSize(new Dimension(1000, 140));
        panelInferior.setBorder(BorderFactory.createTitledBorder("Resultados"));
        txtResultado = new JTextArea();
        txtResultado.setEditable(false);
        panelInferior.add(new JScrollPane(txtResultado), BorderLayout.CENTER);

        add(panelIzquierdo, BorderLayout.WEST);
        add(panelInferior, BorderLayout.SOUTH);
    }

    // Método para redibujar el grafo
    public void actualizarGrafoVisual(Grafo grafo, Set<Integer> conjuntoDominante) {
        panelGrafo.actualizarGrafo(grafo, conjuntoDominante);
    }

    public String getCantVertices() { return txtCantVertices.getText().trim(); }
    public String getOrigen() { return txtOrigen.getText().trim(); }
    public String getDestino() { return txtDestino.getText().trim(); }

    public void addCrearGrafoListener(ActionListener listener) { btnCrearGrafo.addActionListener(listener); }
    public void addAgregarAristaListener(ActionListener listener) { btnAgregarArista.addActionListener(listener); }
    public void addEjecutarGolosoListener(ActionListener listener) { btnEjecutarGoloso.addActionListener(listener); }

    public void mostrarMensaje(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }
    public void agregarLog(String mensaje) { txtResultado.append(mensaje + "\n"); }
    public void limpiarLog() { txtResultado.setText(""); }
    public void limpiarCamposArista() { txtOrigen.setText(""); txtDestino.setText(""); }
}