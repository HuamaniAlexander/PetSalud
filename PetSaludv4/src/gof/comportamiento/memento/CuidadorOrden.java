/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.memento;

import java.util.ArrayList;
import java.util.List;

// Caretaker: Gestiona multiples mementos
public class CuidadorOrden {
    private List<MementoOrden> historial;
    private int indiceActual;
    
    public CuidadorOrden() {
        this.historial = new ArrayList<>();
        this.indiceActual = -1;
    }
    
    // Guardar un nuevo memento
    public void guardar(MementoOrden memento) {
        // Eliminar todos los mementos despues del indice actual
        if (indiceActual < historial.size() - 1) {
            historial.subList(indiceActual + 1, historial.size()).clear();
        }
        
        historial.add(memento);
        indiceActual = historial.size() - 1;
        
        System.out.println("Memento guardado. Total: " + historial.size());
    }
    
    // Obtener memento en posicion especifica
    public MementoOrden obtenerMemento(int indice) {
        if (indice >= 0 && indice < historial.size()) {
            return historial.get(indice);
        }
        return null;
    }
    
    // Obtener memento anterior (deshacer)
    public MementoOrden deshacer() {
        if (puedeDeshacer()) {
            indiceActual--;
            System.out.println("Deshaciendo cambio. Indice: " + indiceActual);
            return historial.get(indiceActual);
        }
        System.out.println("No hay cambios para deshacer");
        return null;
    }
    
    // Obtener memento siguiente (rehacer)
    public MementoOrden rehacer() {
        if (puedeRehacer()) {
            indiceActual++;
            System.out.println("Rehaciendo cambio. Indice: " + indiceActual);
            return historial.get(indiceActual);
        }
        System.out.println("No hay cambios para rehacer");
        return null;
    }
    
    public boolean puedeDeshacer() {
        return indiceActual > 0;
    }
    
    public boolean puedeRehacer() {
        return indiceActual < historial.size() - 1;
    }
    
    // Mostrar historial completo
    public void mostrarHistorial() {
        System.out.println("\n=== HISTORIAL DE CAMBIOS ===");
        for (int i = 0; i < historial.size(); i++) {
            String indicador = (i == indiceActual) ? " <-- ACTUAL" : "";
            System.out.println(i + ". " + historial.get(i) + indicador);
        }
    }
    
    // Limpiar historial
    public void limpiar() {
        historial.clear();
        indiceActual = -1;
        System.out.println("Historial limpiado");
    }
    
    public int getTamanoHistorial() {
        return historial.size();
    }
    
    public int getIndiceActual() {
        return indiceActual;
    }
}
