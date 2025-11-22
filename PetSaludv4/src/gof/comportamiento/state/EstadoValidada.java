/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.comportamiento.state;

import modelo.Enumeraciones;


// Estado concreto: Validada
public class EstadoValidada implements IEstadoOrden {
    
    @Override
    public void procesar(ContextoOrden contexto) {
        System.out.println("ERROR: La orden ya fue validada");
    }
    
    @Override
    public void completar(ContextoOrden contexto) {
        System.out.println("ERROR: La orden ya esta completada y validada");
    }
    
    @Override
    public void validar(ContextoOrden contexto) {
        System.out.println("La orden ya esta validada");
    }
    
    @Override
    public void entregar(ContextoOrden contexto) {
        System.out.println("Entregando orden al cliente...");
        contexto.getOrden().setEstado(Enumeraciones.EstadoOrden.ENTREGADA);
        contexto.setEstado(new EstadoEntregada());
    }
    
    @Override
    public void cancelar(ContextoOrden contexto) {
        System.out.println("ERROR: No se puede cancelar una orden validada");
    }
    
    @Override
    public String getNombreEstado() {
        return "VALIDADA";
    }
    
    @Override
    public String[] getOperacionesPermitidas() {
        return new String[]{"Entregar"};
    }
}