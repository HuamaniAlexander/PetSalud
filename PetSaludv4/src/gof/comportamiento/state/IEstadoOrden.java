/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gof.comportamiento.state;

// Interfaz State

public interface IEstadoOrden {
    void procesar(ContextoOrden contexto);
    void completar(ContextoOrden contexto);
    void validar(ContextoOrden contexto);
    void entregar(ContextoOrden contexto);
    void cancelar(ContextoOrden contexto);
    String getNombreEstado();
    String[] getOperacionesPermitidas();
}