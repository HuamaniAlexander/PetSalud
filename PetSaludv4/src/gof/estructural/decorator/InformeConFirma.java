/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.decorator;


// Decorador: Agregar firma digital
public class InformeConFirma extends DecoradorInforme {
    
    public InformeConFirma(IInforme informe) {
        super(informe);
    }
    
    @Override
    public String generar() {
        return informe.generar() + "\n" + agregarFirma();
    }
    
    @Override
    public String getTitulo() {
        return informe.getTitulo() + " + Firma";
    }
    
    private String agregarFirma() {
        return "\n========================================\n" +
               "FIRMA DIGITAL DEL VETERINARIO\n" +
               "Dr. [Nombre del Veterinario]\n" +
               "Colegiatura: [Numero]\n" +
               "Fecha y Hora de Firma: [Timestamp]\n" +
               "Hash Digital: [SHA256]\n" +
               "========================================\n";
    }
}