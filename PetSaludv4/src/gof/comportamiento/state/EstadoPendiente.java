/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.state;

import modelo.Enumeraciones;


// Estado concreto: Pendiente
public class EstadoPendiente implements IEstadoOrden {
    
    @Override
    public void procesar(ContextoOrden contexto) {
        System.out.println("Iniciando procesamiento de orden...");
        contexto.getOrden().setEstado(Enumeraciones.EstadoOrden.EN_PROCESO);
        contexto.setEstado(new EstadoEnProceso());
    }
    
    @Override
    public void completar(ContextoOrden contexto) {
        System.out.println("ERROR: No se puede completar una orden pendiente");
        System.out.println("Debe procesarse primero");
    }
    
    @Override
    public void validar(ContextoOrden contexto) {
        System.out.println("ERROR: No se puede validar una orden pendiente");
    }
    
    @Override
    public void entregar(ContextoOrden contexto) {
        System.out.println("ERROR: No se puede entregar una orden pendiente");
    }
    
    @Override
    public void cancelar(ContextoOrden contexto) {
        System.out.println("Orden cancelada desde estado Pendiente");
        // Aqui se podria implementar un estado Cancelada
    }
    
    @Override
    public String getNombreEstado() {
        return "PENDIENTE";
    }
    
    @Override
    public String[] getOperacionesPermitidas() {
        return new String[]{"Procesar", "Cancelar"};
    }
}