package ec.edu.taller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GestorIncidentes {

    private Queue<IncidenteSeguridad> colaIncidentes;
    private int capacidadMaxima;

    public GestorIncidentes(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.colaIncidentes = new LinkedList<>(); // OJO: nunca dejarla en null
    }

    public boolean registrarIncidente(IncidenteSeguridad incidente) {
        // 1. No acepta null
        if (incidente == null) {
            return false;
        }
        // 2. No acepta código nulo o vacío (incluye solo espacios)
        String codigo = incidente.getCodigo();
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        // 3. No acepta duplicados por código
        if (existeIncidente(codigo)) {
            return false;
        }
        // 4. No acepta más si la cola está llena
        if (colaIncidentes.size() >= capacidadMaxima) {
            return false;
        }
        // 5. Todo correcto: se encola al final (enqueue)
        return colaIncidentes.offer(incidente);
    }

    public boolean existeIncidente(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        for (IncidenteSeguridad i : colaIncidentes) {
            if (i.getCodigo().equals(codigo)) {
                return true;
            }
        }
        return false;
    }

    public IncidenteSeguridad consultarSiguienteIncidente() {
        // peek(): mira el primero SIN eliminarlo; retorna null si está vacía
        return colaIncidentes.peek();
    }

    public IncidenteSeguridad atenderSiguienteIncidente() {
        // poll(): saca el primero; retorna null si está vacía
        IncidenteSeguridad incidente = colaIncidentes.poll();
        if (incidente != null) {
            incidente.setEstado("ATENDIDO");
        }
        return incidente;
    }

    public int contarIncidentesPendientes() {
        return colaIncidentes.size();
    }

    public int consultarEspaciosDisponibles() {
        return capacidadMaxima - colaIncidentes.size();
    }

    public List<IncidenteSeguridad> listarIncidentes() {
        // Copia en el mismo orden FIFO; la cola original no se toca
        return new ArrayList<>(colaIncidentes);
    }
}
