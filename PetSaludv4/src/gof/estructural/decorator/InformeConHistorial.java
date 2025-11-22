/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.decorator;


// Decorador: Agregar historial medico
public class InformeConHistorial extends DecoradorInforme {
    
    public InformeConHistorial(IInforme informe) {
        super(informe);
    }
    
    @Override
    public String generar() {
        return informe.generar() + "\n" + agregarHistorial();
    }
    
    @Override
    public String getTitulo() {
        return informe.getTitulo() + " + Historial";
    }
    
    private String agregarHistorial() {
        return "\n=== HISTORIAL MEDICO PREVIO ===\n" +
               "- Consulta anterior: [Fecha]\n" +
               "- Vacunas al dia: SI\n" +
               "- Alergias conocidas: Ninguna\n" +
               "- Tratamientos previos: [Detalles]\n" +
               "================================\n";
    }
}