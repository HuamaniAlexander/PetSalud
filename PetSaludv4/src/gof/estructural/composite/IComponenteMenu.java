/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package gof.estructural.composite;

// Componente base

import java.util.List;

public interface IComponenteMenu {
    void mostrar(int nivel);
    void ejecutar();
    String getNombre();
    boolean esCompuesto();
    void agregar(IComponenteMenu componente);
    void remover(IComponenteMenu componente);
    List<IComponenteMenu> getHijos();
}