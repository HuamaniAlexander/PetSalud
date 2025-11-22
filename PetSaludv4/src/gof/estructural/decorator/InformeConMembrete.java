/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.decorator;

// Decorador: Agregar membrete oficial
public class InformeConMembrete extends DecoradorInforme {
    
    public InformeConMembrete(IInforme informe) {
        super(informe);
    }
    
    @Override
    public String generar() {
        return agregarMembrete() + "\n" + informe.generar() + "\n" + agregarPiePagina();
    }
    
    @Override
    public String getTitulo() {
        return "Oficial - " + informe.getTitulo();
    }
    
    private String agregarMembrete() {
        return "╔════════════════════════════════════════════════╗\n" +
               "║         VETERINARIA PETSALUD                  ║\n" +
               "║  Cuidando de tus mascotas desde 2020         ║\n" +
               "║  Direccion: Av. Principal 123                ║\n" +
               "║  Telefono: (01) 234-5678                     ║\n" +
               "║  Email: contacto@petsalud.com                ║\n" +
               "╚════════════════════════════════════════════════╝\n";
    }
    
    private String agregarPiePagina() {
        return "\n---------------------------------------------------\n" +
               "Este documento es valido solo con firma digital\n" +
               "PetSalud - Todos los derechos reservados\n" +
               "---------------------------------------------------\n";
    }
}