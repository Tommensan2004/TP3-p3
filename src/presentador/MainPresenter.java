package presentador;

import modelo.AlgoritmoGoloso;
import modelo.Grafo;
import modelo.ResultadoDominante;
import vista.MainFrame;

public class MainPresenter {
    private MainFrame vista;
    private Grafo grafo;

    public MainPresenter(MainFrame vista) {
        if (vista == null) {
            throw new IllegalArgumentException("La vista no puede ser nula.");
        }
        this.vista = vista;

        this.vista.addCrearGrafoListener(e -> crearGrafo());
        this.vista.addAgregarAristaListener(e -> agregarArista());
        this.vista.addEjecutarGolosoListener(e -> ejecutarGoloso());
    }

    private void crearGrafo() {
        String entrada = vista.getCantVertices();

        if (entrada.isEmpty() || !entrada.matches("\\d+")) {
            vista.mostrarMensaje("Error: Debe ingresar un número entero positivo para la cantidad de vértices.");
            return;
        }

        int cant = Integer.parseInt(entrada);

        if (cant <= 0) {
            vista.mostrarMensaje("Error: La cantidad de vértices debe ser mayor a 0");
            return;
        }

        this.grafo = new Grafo(cant);
        vista.limpiarLog();
        vista.agregarLog("--> Grafo creado con " + cant + " vértices.");
        
        vista.actualizarGrafoVisual(grafo, null);
        vista.mostrarMensaje("Grafo inicializado correctamente.");
    }


    private void agregarArista() {
        if (grafo == null) {
            vista.mostrarMensaje("Primero debe crear e inicializar el grafo.");
            return;
        }

        String origenStr = vista.getOrigen();
        String destinoStr = vista.getDestino();

        if (origenStr.isEmpty() || !origenStr.matches("\\d+") || destinoStr.isEmpty() || !destinoStr.matches("\\d+")) {
            vista.mostrarMensaje("Error: Los vértices origen y destino deben ser números enteros.");
            return;
        }

        int u = Integer.parseInt(origenStr);
        int v = Integer.parseInt(destinoStr);

        
        int cant = grafo.getCantVertices();
        if (u < 0 || u >= cant || v < 0 || v >= cant) {
            vista.mostrarMensaje("Error: Los vértices deben estar en el rango de 0 a " + (cant - 1) + ".");
            return;
        }

        if (u == v) {
            vista.mostrarMensaje("Error: No se permiten bucles en un mismo vértice.");
            return;
        }

        if (grafo.existeArista(u, v)) {
            vista.mostrarMensaje("La arista entre " + u + " y " + v + " ya fue agregada.");
            return;
        }

        grafo.agregarArista(u, v);
        vista.agregarLog("Arista agregada: (" + u + " <---> " + v + ")");
        vista.limpiarCamposArista();
        vista.actualizarGrafoVisual(grafo, null);
    }

    private void ejecutarGoloso() {
        if (grafo == null) {
            vista.mostrarMensaje("No hay ningún grafo cargado para procesar.");
            return;
        }

        ResultadoDominante resultado = AlgoritmoGoloso.resolver(grafo);

        vista.agregarLog("\n==========================================");
        vista.agregarLog("¡Conjunto Dominante Mínimo Encontrado!");
        vista.agregarLog("Vértices elegidos: " + resultado.getConjuntoDominante());
        vista.agregarLog("Tamaño del conjunto: " + resultado.getTamanio());
        vista.agregarLog("Tiempo de ejecución: " + resultado.getTiempoEjecucionMillis() + " ms");
        vista.agregarLog("==========================================\n");

        vista.actualizarGrafoVisual(grafo, resultado.getConjuntoDominante());
    }
}