/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.bridge;

// Implementacion: Formato de reporte
public interface IFormatoReporte {
    void setContenido(String contenido);
    void setTitulo(String titulo);
    String generar();
    String getExtension();
}