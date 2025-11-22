/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.decorator;


// Decorador: Agregar codigo QR
class InformeConQR extends DecoradorInforme {
    
    public InformeConQR(IInforme informe) {
        super(informe);
    }
    
    @Override
    public String generar() {
        return informe.generar() + "\n" + agregarQR();
    }
    
    @Override
    public String getTitulo() {
        return informe.getTitulo() + " + QR";
    }
    
    private String agregarQR() {
        return "\n[CODIGO QR GENERADO]\n" +
               "+-----------------+\n" +
               "| ## ### ## # ## |\n" +
               "| # ### ## ### # |\n" +
               "| ### # ## # ### |\n" +
               "+-----------------+\n" +
               "Escanear para validar autenticidad\n";
    }
}