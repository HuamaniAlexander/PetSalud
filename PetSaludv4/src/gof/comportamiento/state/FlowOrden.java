/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.state;

import modelo.entidades.OrdenVeterinaria;

// Clase utilitaria para gestionar flujo completo de orden
public class FlowOrden {
    private ContextoOrden contexto;
    
    public FlowOrden(OrdenVeterinaria orden) {
        this.contexto = new ContextoOrden(orden);
    }
    
    public void mostrarEstadoActual() {
        System.out.println("\n=== ESTADO ACTUAL ===");
        System.out.println("Estado: " + contexto.getEstadoActual().getNombreEstado());
        System.out.println("Operaciones permitidas:");
        for (String op : contexto.getOperacionesPermitidas()) {
            System.out.println("  - " + op);
        }
        System.out.println("=====================\n");
    }
    
    public void procesarHastaFinal() {
        System.out.println("Ejecutando flujo completo de orden...\n");
        
        mostrarEstadoActual();
        
        contexto.procesar();
        mostrarEstadoActual();
        
        contexto.completar();
        mostrarEstadoActual();
        
        contexto.validar();
        mostrarEstadoActual();
        
        contexto.entregar();
        mostrarEstadoActual();
    }
    
    public ContextoOrden getContexto() {
        return contexto;
    }
}