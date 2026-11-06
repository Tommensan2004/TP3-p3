package modelo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ResultadoDominante {
    private final Set<Integer> conjuntoDominante;

    public ResultadoDominante(Set<Integer> conjuntoDominante) {
        if (conjuntoDominante == null) {
            throw new IllegalArgumentException("El conjunto dominante no puede ser nulo.");
        }
        this.conjuntoDominante = Collections.unmodifiableSet(new HashSet<>(conjuntoDominante));
    }

    public Set<Integer> getConjuntoDominante() {
        return conjuntoDominante;
    }

    public int getTamanio() {
        return conjuntoDominante.size();
    }
}