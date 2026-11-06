package test;

import static org.junit.Assert.*;
import org.junit.Test;

import modelo.AlgoritmoGoloso;
import modelo.Grafo;
import modelo.ResultadoDominante;

public class AlgoritmoGolosoTest {

    @Test
    public void resuelveGrafoEstrellaTest() {
        Grafo grafo = new Grafo(5);
        for (int v = 1; v < 5; v++) {
            grafo.agregarArista(0, v);
        }

        ResultadoDominante resultado = AlgoritmoGoloso.resolver(grafo);
        assertEquals(1, resultado.getTamanio());
        assertTrue(resultado.getConjuntoDominante().contains(0));
        assertTrue(esDominante(grafo, resultado.getConjuntoDominante()));
    }

    @Test
    public void resuelveGrafoSinAristasTest() {
        Grafo grafo = new Grafo(4);
        ResultadoDominante resultado = AlgoritmoGoloso.resolver(grafo);

        assertEquals(4, resultado.getTamanio());
        assertTrue(esDominante(grafo, resultado.getConjuntoDominante()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void grafoNuloTest() {
        AlgoritmoGoloso.resolver(null);
    }

    private boolean esDominante(Grafo grafo, java.util.Set<Integer> conjunto) {
        for (int v = 0; v < grafo.getCantVertices(); v++) {
            if (conjunto.contains(v)) continue;
            boolean dominado = false;
            for (int vecino : grafo.getVecinos(v)) {
                if (conjunto.contains(vecino)) {
                    dominado = true;
                    break;
                }
            }
            if (!dominado) return false;
        }
        return true;
    }
}
