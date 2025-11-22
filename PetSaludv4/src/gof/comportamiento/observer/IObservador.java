/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gof.comportamiento.observer;

// Interfaz Observer

import modelo.entidades.OrdenVeterinaria;

public interface IObservador {
    void actualizar(OrdenVeterinaria orden, String evento);
    String getTipo();
}