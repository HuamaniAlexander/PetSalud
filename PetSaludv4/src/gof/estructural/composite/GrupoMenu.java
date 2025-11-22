/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.composite;

// Compuesto: Grupo de menu que puede contener items u otros grupos

import java.util.ArrayList;
import java.util.List;

public class GrupoMenu implements IComponenteMenu {
    private String nombre;
    private List<IComponenteMenu> hijos;
    
    public GrupoMenu(String nombre) {
        this.nombre = nombre;
        this.hijos = new ArrayList<>();
    }
    
    @Override
    public void mostrar(int nivel) {
        String indent = "  ".repeat(nivel);
        System.out.println(indent + "+ " + nombre);
        
        for (IComponenteMenu hijo : hijos) {
            hijo.mostrar(nivel + 1);
        }
    }
    
    @Override
    public void ejecutar() {
        System.out.println("Mostrando submenu: " + nombre);
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public boolean esCompuesto() {
        return true;
    }
    
    @Override
    public void agregar(IComponenteMenu componente) {
        hijos.add(componente);
    }
    
    @Override
    public void remover(IComponenteMenu componente) {
        hijos.remove(componente);
    }
    
    @Override
    public List<IComponenteMenu> getHijos() {
        return new ArrayList<>(hijos);
    }
}