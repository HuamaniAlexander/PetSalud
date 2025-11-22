/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.decorator;

// Decorador abstracto
public abstract class DecoradorInforme implements IInforme {
    protected IInforme informe;
    
    public DecoradorInforme(IInforme informe) {
        this.informe = informe;
    }
    
    @Override
    public String generar() {
        return informe.generar();
    }
    
    @Override
    public String getTitulo() {
        return informe.getTitulo();
    }
}