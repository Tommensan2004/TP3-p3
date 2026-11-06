package modelo;

import java.util.Random;

/** Genera grafos simples no dirigidos con una probabilidad dada para cada arista posible. */
public final class GeneradorGrafoAleatorio {
    public static final double PROBABILIDAD_ARISTA_PREDETERMINADA = 0.5;

    private GeneradorGrafoAleatorio() {
    }

    public static Grafo generar(int cantidadVertices) {
        return generar(cantidadVertices, PROBABILIDAD_ARISTA_PREDETERMINADA, new Random());
    }

    public static Grafo generar(int cantidadVertices, double probabilidadArista, Random random) {
        if (cantidadVertices <= 0) {
            throw new IllegalArgumentException("La cantidad de vertices debe ser mayor que cero.");
        }
        if (probabilidadArista < 0.0 || probabilidadArista > 1.0 || Double.isNaN(probabilidadArista)) {
            throw new IllegalArgumentException("La probabilidad de arista debe estar entre 0 y 1.");
        }
        if (random == null) {
            throw new IllegalArgumentException("El generador aleatorio no puede ser nulo.");
        }

        Grafo grafo = new Grafo(cantidadVertices);
        for (int origen = 0; origen < cantidadVertices; origen++) {
            for (int destino = origen + 1; destino < cantidadVertices; destino++) {
                if (random.nextDouble() < probabilidadArista) {
                    grafo.agregarArista(origen, destino);
                }
            }
        }
        return grafo;
    }
}
