package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grafo {
    private int cantVertices;
    private List<Set<Integer>> vecinos; 

    public Grafo(int cantVertices) {
        if (cantVertices <= 0) {
            throw new IllegalArgumentException("El grafo debe tener al menos un vértice.");
        }
        
        this.cantVertices = cantVertices;
        this.vecinos = new ArrayList<>(cantVertices);
        for (int i = 0; i < cantVertices; i++) {
            vecinos.add(new HashSet<>());
        }
    }

    public void agregarArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        if (i == j) {
            throw new IllegalArgumentException("No se permiten bucles.");
        }
        vecinos.get(i).add(j);
        vecinos.get(j).add(i);
    }

    public void eliminarArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        vecinos.get(i).remove(j);
        vecinos.get(j).remove(i);
    }

    public boolean existeArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        return vecinos.get(i).contains(j);
    }

    public Set<Integer> getVecinos(int i) {
        validarVertice(i);
        return Collections.unmodifiableSet(vecinos.get(i));
    }

    public int getCantVertices() {
        return cantVertices;
    }

    private void validarVertice(int i) {
        if (i < 0 || i >= cantVertices) {
            throw new IllegalArgumentException("El vértice " + i + " no existe en el grafo.");
        }
    }
}
