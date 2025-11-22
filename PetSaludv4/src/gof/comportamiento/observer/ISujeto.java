/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gof.comportamiento.observer;

// Interfaz Subject
interface ISujeto {
    void agregarObservador(IObservador observador);
    void removerObservador(IObservador observador);
    void notificarObservadores(String evento);
}