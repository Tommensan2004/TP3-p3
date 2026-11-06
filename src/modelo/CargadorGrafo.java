package modelo;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CargadorGrafo {
	private static final Set<String> CAMPOS_ADMITIDOS = new HashSet<>();

    static {
        CAMPOS_ADMITIDOS.add("cantidadVertices");
        CAMPOS_ADMITIDOS.add("aristas");
    }

    private CargadorGrafo() {
    }

    /** Lee y valida el archivo JSON seleccionado. */
    public static Grafo cargar(Path archivo) throws IOException {
        if (archivo == null) {
            throw new IllegalArgumentException("Debe seleccionar un archivo JSON.");
        }
        if (!Files.isRegularFile(archivo)) {
            throw new IOException("El archivo seleccionado no existe o no es un archivo regular.");
        }

        String contenido = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
        Object raiz = new JsonParser(contenido).parsear();
        if (!(raiz instanceof Map<?, ?>)) {
            throw new IllegalArgumentException("El contenido JSON debe comenzar con un objeto.");
        }

        Map<?, ?> objeto = (Map<?, ?>) raiz;
        validarCampos(objeto);

        int cantidadVertices = obtenerEntero(objeto.get("cantidadVertices"), "cantidadVertices");
        if (cantidadVertices <= 0) {
            throw new IllegalArgumentException("El campo 'cantidadVertices' debe ser mayor que cero.");
        }

        Object valorAristas = objeto.get("aristas");
        if (!(valorAristas instanceof List<?>)) {
            throw new IllegalArgumentException("El campo 'aristas' debe ser un arreglo JSON.");
        }

        Grafo grafo = new Grafo(cantidadVertices);
        List<?> aristas = (List<?>) valorAristas;
        for (int indice = 0; indice < aristas.size(); indice++) {
            Object valorArista = aristas.get(indice);
            if (!(valorArista instanceof List<?>)) {
                throw errorArista(indice, "debe ser un arreglo con dos vertices.");
            }

            List<?> arista = (List<?>) valorArista;
            if (arista.size() != 2) {
                throw errorArista(indice, "debe contener exactamente dos vertices: [origen, destino].");
            }

            int origen = obtenerEntero(arista.get(0), "origen de la arista " + indice);
            int destino = obtenerEntero(arista.get(1), "destino de la arista " + indice);
            validarVertice(origen, cantidadVertices, indice);
            validarVertice(destino, cantidadVertices, indice);

            if (origen == destino) {
                throw errorArista(indice, "no se permiten bucles (origen y destino iguales).");
            }
            if (grafo.existeArista(origen, destino)) {
                throw errorArista(indice, "la arista " + origen + " - " + destino + " esta duplicada.");
            }

            grafo.agregarArista(origen, destino);
        }

        return grafo;
    }

    private static void validarCampos(Map<?, ?> objeto) {
        for (String campo : CAMPOS_ADMITIDOS) {
            if (!objeto.containsKey(campo)) {
                throw new IllegalArgumentException("Falta el campo obligatorio '" + campo + "' en el JSON.");
            }
        }
        for (Object campo : objeto.keySet()) {
            if (!(campo instanceof String) || !CAMPOS_ADMITIDOS.contains(campo)) {
                throw new IllegalArgumentException("El campo '" + campo + "' no esta permitido en el JSON.");
            }
        }
    }

    private static int obtenerEntero(Object valor, String descripcion) {
        if (!(valor instanceof BigDecimal)) {
            throw new IllegalArgumentException("El valor de '" + descripcion + "' debe ser un numero entero.");
        }
        try {
            return ((BigDecimal) valor).intValueExact();
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("El valor de '" + descripcion + "' debe ser un entero dentro del rango permitido.");
        }
    }

    private static void validarVertice(int vertice, int cantidadVertices, int indiceArista) {
        if (vertice < 0 || vertice >= cantidadVertices) {
            throw errorArista(indiceArista, "el vertice " + vertice + " esta fuera del rango 0 a "
                    + (cantidadVertices - 1) + ".");
        }
    }

    private static IllegalArgumentException errorArista(int indice, String mensaje) {
        return new IllegalArgumentException("Error en aristas[" + indice + "]: " + mensaje);
    }

    private static final class JsonParser {
        private final String texto;
        private int posicion;

        private JsonParser(String texto) {
            this.texto = texto;
            this.posicion = 0;
        }

        private Object parsear() {
            omitirEspacios();
            Object valor = parsearValor();
            omitirEspacios();
            if (!fin()) {
                throw error("Hay contenido adicional despues del valor JSON");
            }
            return valor;
        }

        private Object parsearValor() {
            omitirEspacios();
            if (fin()) {
                throw error("El archivo esta vacio o el JSON esta incompleto");
            }

            char actual = texto.charAt(posicion);
            if (actual == '{') return parsearObjeto();
            if (actual == '[') return parsearArreglo();
            if (actual == '"') return parsearCadena();
            if (actual == 't') return parsearLiteral("true", Boolean.TRUE);
            if (actual == 'f') return parsearLiteral("false", Boolean.FALSE);
            if (actual == 'n') return parsearLiteral("null", null);
            if (actual == '-' || esDigito(actual)) return parsearNumero();
            throw error("Se encontro un caracter inesperado");
        }

        private Map<String, Object> parsearObjeto() {
            esperar('{');
            omitirEspacios();
            Map<String, Object> objeto = new HashMap<>();
            if (aceptar('}')) return objeto;

            while (true) {
                omitirEspacios();
                if (fin() || texto.charAt(posicion) != '"') {
                    throw error("Las claves de un objeto JSON deben ser cadenas");
                }
                String clave = parsearCadena();
                if (objeto.containsKey(clave)) {
                    throw error("La clave '" + clave + "' esta repetida");
                }

                omitirEspacios();
                esperar(':');
                Object valor = parsearValor();
                objeto.put(clave, valor);

                omitirEspacios();
                if (aceptar('}')) return objeto;
                esperar(',');
            }
        }

        private List<Object> parsearArreglo() {
            esperar('[');
            omitirEspacios();
            List<Object> arreglo = new ArrayList<>();
            if (aceptar(']')) return arreglo;

            while (true) {
                arreglo.add(parsearValor());
                omitirEspacios();
                if (aceptar(']')) return arreglo;
                esperar(',');
            }
        }

        private String parsearCadena() {
            esperar('"');
            StringBuilder resultado = new StringBuilder();
            while (!fin()) {
                char actual = texto.charAt(posicion++);
                if (actual == '"') return resultado.toString();
                if (actual < 0x20) {
                    throw error("Una cadena JSON no puede contener caracteres de control sin escapar");
                }
                if (actual != '\\') {
                    resultado.append(actual);
                    continue;
                }

                if (fin()) throw error("Secuencia de escape incompleta");
                char escape = texto.charAt(posicion++);
                switch (escape) {
                    case '"': resultado.append('"'); break;
                    case '\\': resultado.append('\\'); break;
                    case '/': resultado.append('/'); break;
                    case 'b': resultado.append('\b'); break;
                    case 'f': resultado.append('\f'); break;
                    case 'n': resultado.append('\n'); break;
                    case 'r': resultado.append('\r'); break;
                    case 't': resultado.append('\t'); break;
                    case 'u': resultado.append(parsearUnicode()); break;
                    default: throw error("Secuencia de escape JSON no valida");
                }
            }
            throw error("Cadena JSON sin comilla de cierre");
        }

        private char parsearUnicode() {
            if (posicion + 4 > texto.length()) {
                throw error("Secuencia Unicode incompleta");
            }
            int valor = 0;
            for (int i = 0; i < 4; i++) {
                char caracter = texto.charAt(posicion++);
                int digito;
                if (caracter >= '0' && caracter <= '9') {
                    digito = caracter - '0';
                } else if (caracter >= 'a' && caracter <= 'f') {
                    digito = caracter - 'a' + 10;
                } else if (caracter >= 'A' && caracter <= 'F') {
                    digito = caracter - 'A' + 10;
                } else {
                    throw error("Secuencia Unicode no valida");
                }
                valor = (valor << 4) | digito;
            }
            return (char) valor;
        }

        private Object parsearNumero() {
            int inicio = posicion;
            aceptar('-');

            if (aceptar('0')) {
                if (!fin() && esDigito(texto.charAt(posicion))) {
                    throw error("Un numero JSON no puede comenzar con ceros");
                }
            } else {
                exigirDigitos();
            }

            if (aceptar('.')) exigirDigitos();
            if (aceptar('e') || aceptar('E')) {
                if (!aceptar('+')) aceptar('-');
                exigirDigitos();
            }

            try {
                return new BigDecimal(texto.substring(inicio, posicion));
            } catch (NumberFormatException e) {
                throw error("Numero JSON no valido");
            }
        }

        private Object parsearLiteral(String literal, Object valor) {
            if (!texto.startsWith(literal, posicion)) {
                throw error("Literal JSON no valido");
            }
            posicion += literal.length();
            return valor;
        }

        private void exigirDigitos() {
            int inicio = posicion;
            while (!fin() && esDigito(texto.charAt(posicion))) posicion++;
            if (inicio == posicion) throw error("Se esperaba al menos un digito");
        }

        private void omitirEspacios() {
            while (!fin()) {
                char actual = texto.charAt(posicion);
                if (actual == ' ' || actual == '\n' || actual == '\r' || actual == '\t') {
                    posicion++;
                } else {
                    return;
                }
            }
        }

        private void esperar(char esperado) {
            if (!aceptar(esperado)) {
                throw error("Se esperaba el caracter '" + esperado + "'");
            }
        }

        private boolean aceptar(char esperado) {
            if (!fin() && texto.charAt(posicion) == esperado) {
                posicion++;
                return true;
            }
            return false;
        }

        private boolean fin() {
            return posicion >= texto.length();
        }

        private boolean esDigito(char caracter) {
            return caracter >= '0' && caracter <= '9';
        }

        private IllegalArgumentException error(String mensaje) {
            return new IllegalArgumentException("JSON invalido en la posicion " + (posicion + 1) + ": " + mensaje + ".");
        }
    }
}
