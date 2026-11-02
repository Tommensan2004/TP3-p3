package modelo;

import java.util.Set;

public class ResultadoDominante {
    private final Set<Integer> conjuntoDominante;
    private final double tiempoEjecucionNanos;

    public ResultadoDominante(Set<Integer> conjuntoDominante, double tiempoTotal) {
        this.conjuntoDominante = conjuntoDominante;
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