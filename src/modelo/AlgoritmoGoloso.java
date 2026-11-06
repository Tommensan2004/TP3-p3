package modelo;

import java.util.HashSet;
import java.util.Set;

public class AlgoritmoGoloso {

    public static ResultadoDominante resolver(Grafo grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("El grafo no puede ser nulo.");
        }

        int cantVertices = grafo.getCantVertices();
        Set<Integer> dominados = new HashSet<>();
        Set<Integer> conjuntoDominante = new HashSet<>();

        // Iterar hasta que todos los vértices del grafo estén dominados
        while (dominados.size() < cantVertices) {
            int mejorVertice = -1;
            int maxNuevosDominados = -1;

            // Evaluar cada vertice
            for (int v = 0; v < cantVertices; v++) {
                if (conjuntoDominante.contains(v)) {
                    continue; // omitimos seleccionados
                }

                // Contar nodos NO dominados, vecinos de V
                int nuevos = contarNuevosDominados(grafo, v, dominados);
                if (nuevos > maxNuevosDominados) {
                    maxNuevosDominados = nuevos;
                    mejorVertice = v;
                }
            }

            // Agregar el mejor vertice
            conjuntoDominante.add(mejorVertice);

            // Agregar a dominados el vertice y sus vecinos
            dominados.add(mejorVertice);
            dominados.addAll(grafo.getVecinos(mejorVertice));
        }

        return new ResultadoDominante(conjuntoDominante);
    }

    private static int contarNuevosDominados(Grafo grafo, int v, Set<Integer> dominados) {
        int cont = 0;

        if (!dominados.contains(v)) {
            cont++;
        }
        // Suma vecinos de v que no esten dominados
        for (int vecino : grafo.getVecinos(v)) {
            if (!dominados.contains(vecino)) {
                cont++;
            }
        }
        return cont;
    }
}