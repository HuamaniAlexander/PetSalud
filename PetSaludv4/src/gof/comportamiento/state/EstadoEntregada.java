/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.state;

// Estado concreto: Entregada (estado final)

public class EstadoEntregada implements IEstadoOrden {
    
    @Override
    public void procesar(ContextoOrden contexto) {
        System.out.println("ERROR: La orden ya fue entregada (estado final)");
    }
    
    @Override
    public void completar(ContextoOrden contexto) {
        System.out.println("ERROR: La orden ya fue entregada (estado final)");
    }
    
    @Override
    public void validar(ContextoOrden contexto) {
        System.out.println("ERROR: La orden ya fue entregada (estado final)");
    }
    
    @Override
    public void entregar(ContextoOrden contexto) {
        System.out.println("La orden ya fue entregada");
    }
    
    @Override
    public void cancelar(ContextoOrden contexto) {
        System.out.println("ERROR: No se puede cancelar una orden entregada");
    }
    
    @Override
    public String getNombreEstado() {
        return "ENTREGADA";
    }
    
    @Override
    public String[] getOperacionesPermitidas() {
        return new String[]{}; // Ninguna operacion permitida
    }
}