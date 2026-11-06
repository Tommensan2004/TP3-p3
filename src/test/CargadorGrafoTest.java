package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import modelo.CargadorGrafo;
import modelo.Grafo;

public class CargadorGrafoTest {
    private Path archivoTemporal;

    @Before
    public void setUp() throws IOException {
        archivoTemporal = Files.createTempFile("grafo-test-", ".json");
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(archivoTemporal);
    }

    @Test
    public void cargaVerticesYAristasTest() throws IOException {
        escribir("{\n"
                + "  \"cantidadVertices\": 5,\n"
                + "  \"aristas\": [[0, 1], [0, 2], [1, 3], [3, 4]]\n"
                + "}");

        Grafo grafo = CargadorGrafo.cargar(archivoTemporal);

        assertEquals(5, grafo.getCantVertices());
        assertEquals(4, grafo.getCantAristas());
        assertTrue(grafo.existeArista(0, 1));
        assertTrue(grafo.existeArista(1, 0));
        assertTrue(grafo.existeArista(3, 4));
    }

    @Test
    public void aceptaLosCamposEnDistintoOrdenTest() throws IOException {
        escribir("{\"aristas\":[[0,1]],\"cantidadVertices\":2}");

        Grafo grafo = CargadorGrafo.cargar(archivoTemporal);

        assertEquals(2, grafo.getCantVertices());
        assertTrue(grafo.existeArista(0, 1));
    }

    @Test
    public void cargaGrafoSinAristasTest() throws IOException {
        escribir("{\"cantidadVertices\":4,\"aristas\":[]}");

        Grafo grafo = CargadorGrafo.cargar(archivoTemporal);

        assertEquals(4, grafo.getCantVertices());
        assertEquals(0, grafo.getCantAristas());
    }

    @Test
    public void aceptaEspaciosYSaltosDeLineaTest() throws IOException {
        escribir("  { \n  \"cantidadVertices\" : 3 , \n \"aristas\" : [ [ 0 , 1 ] ] \n }  ");

        Grafo grafo = CargadorGrafo.cargar(archivoTemporal);

        assertEquals(1, grafo.getCantAristas());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaArchivoVacioTest() throws IOException {
        escribir("   \n  ");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaJsonMalformadoTest() throws IOException {
        escribir("{\"cantidadVertices\": 3, \"aristas\": [[0, 1]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaContenidoDespuesDelObjetoJsonTest() throws IOException {
        escribir("{\"cantidadVertices\":2,\"aristas\":[]} texto");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaRaizQueNoEsUnObjetoTest() throws IOException {
        escribir("[2, [[0, 1]]]");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCampoCantidadVerticesAusenteTest() throws IOException {
        escribir("{\"aristas\":[]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCampoAristasAusenteTest() throws IOException {
        escribir("{\"cantidadVertices\":3}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCamposDesconocidosTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[],\"nombre\":\"prueba\"}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaClavesJsonDuplicadasTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"cantidadVertices\":4,\"aristas\":[]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCantidadDeVerticesCeroTest() throws IOException {
        escribir("{\"cantidadVertices\":0,\"aristas\":[]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCantidadDeVerticesNegativaTest() throws IOException {
        escribir("{\"cantidadVertices\":-2,\"aristas\":[]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCantidadDeVerticesNoNumericaTest() throws IOException {
        escribir("{\"cantidadVertices\":\"3\",\"aristas\":[]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCantidadDeVerticesFraccionariaTest() throws IOException {
        escribir("{\"cantidadVertices\":3.5,\"aristas\":[]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCantidadDeVerticesFueraDelRangoEnteroTest() throws IOException {
        escribir("{\"cantidadVertices\":2147483648,\"aristas\":[]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaNumeroConCerosInicialesTest() throws IOException {
        escribir("{\"cantidadVertices\":03,\"aristas\":[]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaArregloConComaFinalTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[[0,1],]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaAristasQueNoSonUnArregloTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":null}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaElementoDeAristasQueNoEsUnArregloTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[\"0-1\"]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaAristaConUnSoloVerticeTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[[0]]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaAristaConMasDeDosVerticesTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[[0,1,2]]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaVerticeNoNumericoTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[[0,\"1\"]]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaVerticeFraccionarioTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[[0,1.5]]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaVerticeFueraDeRangoTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[[0,3]]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaVerticeNegativoTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[[-1,2]]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaBucleTest() throws IOException {
        escribir("{\"cantidadVertices\":2,\"aristas\":[[1,1]]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaAristaDuplicadaEnSentidoInversoTest() throws IOException {
        escribir("{\"cantidadVertices\":3,\"aristas\":[[0,1],[1,0]]}");
        CargadorGrafo.cargar(archivoTemporal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaRutaNulaTest() throws IOException {
        CargadorGrafo.cargar(null);
    }

    private void escribir(String contenido) throws IOException {
        Files.write(archivoTemporal, contenido.getBytes(StandardCharsets.UTF_8));
    }
}
