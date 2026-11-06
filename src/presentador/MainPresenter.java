package presentador;

import java.io.File;
import java.io.IOException;

import modelo.AlgoritmoGoloso;
import modelo.CargadorGrafo;
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
        this.vista.addCargarGrafoListener(e -> cargarGrafoDesdeArchivo());
        this.vista.addAgregarAristaListener(e -> agregarArista());
        this.vista.addEjecutarGolosoListener(e -> ejecutarGoloso());
    }

    private void crearGrafo() {
        String entrada = vista.getCantVertices();

        if (entrada.isEmpty() || !entrada.matches("\\d+")) {
            vista.mostrarMensaje("Error: Debe ingresar un número entero positivo para la cantidad de vértices.");
            return;
        }

        try {
            int cant = Integer.parseInt(entrada);
            if (cant <= 0) {
                vista.mostrarMensaje("Error: La cantidad de vértices debe ser mayor a 0.");
                return;
            }

        this.grafo = new Grafo(cant);
        vista.limpiarLog();
        vista.agregarLog("--> Grafo creado con " + cant + " vértices.");
        
        vista.actualizarGrafoVisual(grafo, null);
        vista.mostrarMensaje("Grafo inicializado correctamente.");
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Error: La cantidad de vértices es demasiado grande.");
        }
    }
    
    private void cargarGrafoDesdeArchivo() {
        File archivo = vista.seleccionarArchivoGrafo();
        if (archivo == null) {
            return;
        }

        try {
            Grafo nuevoGrafo = CargadorGrafo.cargar(archivo.toPath());
            this.grafo = nuevoGrafo;

            vista.limpiarLog();
            vista.agregarLog("Grafo cargado desde archivo: " + archivo.getName());
            vista.agregarLog("Vértices: " + grafo.getCantVertices());
            vista.agregarLog("Aristas: " + grafo.getCantAristas());
            vista.actualizarGrafoVisual(grafo, null);
            vista.mostrarMensaje("Grafo cargado correctamente.");
        } catch (IOException | IllegalArgumentException e) {
            vista.mostrarMensaje("No se pudo cargar el grafo:\n" + e.getMessage());
        }
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

        try {
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
            vista.agregarLog("Arista agregada: (" + u + " <--> " + v + ")");
            vista.limpiarCamposArista();
            vista.actualizarGrafoVisual(grafo, null);
        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Error: El número de vértice es demasiado grande.");
        }
    }

    private void ejecutarGoloso() {
        if (grafo == null) {
            vista.mostrarMensaje("No hay ningún grafo cargado para procesar.");
            return;
        }

        ResultadoDominante resultado = AlgoritmoGoloso.resolver(grafo);

        vista.agregarLog("\n==========================================");
        vista.agregarLog("Conjunto Dominante Mínimo Encontrado!");
        vista.agregarLog("Vértices elegidos: " + resultado.getConjuntoDominante());
        vista.agregarLog("Tamaño del conjunto: " + resultado.getTamanio());
        vista.agregarLog("Tiempo de ejecución: " + resultado.getTiempoEjecucionMillis() + " ms");
        vista.agregarLog("==========================================\n");

        vista.actualizarGrafoVisual(grafo, resultado.getConjuntoDominante());
    }
}