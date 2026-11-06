package modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ResultadoDominante {
    private final Set<Integer> conjuntoDominante;
    private final double tiempoEjecucionNanos;

    public ResultadoDominante(Set<Integer> conjuntoDominante, double tiempoTotal) {
    	if (conjuntoDominante == null) {
            throw new IllegalArgumentException("El conjunto dominante no puede ser nulo.");
        }
        if (tiempoTotal < 0) {
            throw new IllegalArgumentException("El tiempo de ejecucion no puede ser negativo.");
        }
        this.conjuntoDominante = Collections.unmodifiableSet(new HashSet<>(conjuntoDominante));
        this.tiempoEjecucionNanos = tiempoTotal;
    }

    public Set<Integer> getConjuntoDominante() {
        return conjuntoDominante;
    }

    public int getTamanio() {
        return conjuntoDominante.size();
    }

    public double getTiempoEjecucionNanos() {
        return tiempoEjecucionNanos;
    }

    public double getTiempoEjecucionMillis() {
        return tiempoEjecucionNanos / 1_000_000.0;
    }
}