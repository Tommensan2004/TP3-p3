package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import modelo.Grafo;

public class GrafoTest {
    private Grafo grafo;

    @Before
    public void setUp() {
        grafo = new Grafo(3);
    }

    @Test
    public void crearGrafoTest() {
        assertEquals(3, grafo.getCantVertices());
        assertEquals(0, grafo.getCantAristas());
    }

    @Test
    public void agregarAristaNoDirigidaTest() {
        grafo.agregarArista(0, 1);

        assertTrue(grafo.existeArista(0, 1));
        assertTrue(grafo.existeArista(1, 0));
        assertEquals(1, grafo.getCantAristas());
        assertTrue(grafo.getVecinos(0).contains(1));
    }

    @Test
    public void agregarAristaRepetidaNoDuplicaTest() {
        grafo.agregarArista(0, 1);
        grafo.agregarArista(1, 0);
        assertEquals(1, grafo.getCantAristas());
    }

    @Test
    public void eliminarAristaTest() {
        grafo.agregarArista(0, 2);
        grafo.eliminarArista(0, 2);
        assertFalse(grafo.existeArista(0, 2));
        assertFalse(grafo.existeArista(2, 0));
        assertEquals(0, grafo.getCantAristas());
    }

    @Test(expected = IllegalArgumentException.class)
    public void crearGrafoSinVerticesTest() {
        new Grafo(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void agregarAristaConVerticeInexistenteTest() {
        grafo.agregarArista(0, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void agregarBucleTest() {
        grafo.agregarArista(1, 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void vecinosNoPermiteModificacionesExternasTest() {
        grafo.getVecinos(0).add(2);
    }
}
