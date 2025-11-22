/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.state;

import modelo.Enumeraciones;


// Estado concreto: En Proceso
public class EstadoEnProceso implements IEstadoOrden {
    
    @Override
    public void procesar(ContextoOrden contexto) {
        System.out.println("La orden ya esta en proceso");
    }
    
    @Override
    public void completar(ContextoOrden contexto) {
        System.out.println("Completando orden...");
        contexto.getOrden().setEstado(Enumeraciones.EstadoOrden.COMPLETADA);
        contexto.setEstado(new EstadoCompletada());
    }
    
    @Override
    public void validar(ContextoOrden contexto) {
        System.out.println("ERROR: No se puede validar una orden en proceso");
        System.out.println("Debe completarse primero");
    }
    
    @Override
    public void entregar(ContextoOrden contexto) {
        System.out.println("ERROR: No se puede entregar una orden en proceso");
    }
    
    @Override
    public void cancelar(ContextoOrden contexto) {
        System.out.println("Orden cancelada desde estado En Proceso");
    }
    
    @Override
    public String getNombreEstado() {
        return "EN_PROCESO";
    }
    
    @Override
    public String[] getOperacionesPermitidas() {
        return new String[]{"Completar", "Cancelar"};
    }
}
