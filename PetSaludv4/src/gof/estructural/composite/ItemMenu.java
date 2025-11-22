package gof.estructural.composite;

import modelo.Enumeraciones.RolUsuario;
import java.util.ArrayList;
import java.util.List;

/**
 * Patron Composite - Construye menus jerarquicos dinamicamente segun rol
 */

// Hoja: Item de menu individual
public class ItemMenu implements IComponenteMenu {
    private String nombre;
    private Runnable accion;
    
    public ItemMenu(String nombre, Runnable accion) {
        this.nombre = nombre;
        this.accion = accion;
    }
    
    public ItemMenu(String nombre) {
        this(nombre, () -> System.out.println("Ejecutando: " + nombre));
    }
    
    @Override
    public void mostrar(int nivel) {
        String indent = "  ".repeat(nivel);
        System.out.println(indent + "- " + nombre);
    }
    
    @Override
    public void ejecutar() {
        if (accion != null) {
            accion.run();
        }
    }
    
    @Override
    public String getNombre() {
        return nombre;
    }
    
    @Override
    public boolean esCompuesto() {
        return false;
    }
    
    @Override
    public void agregar(IComponenteMenu componente) {
        throw new UnsupportedOperationException("No se pueden agregar componentes a un item simple");
    }
    
    @Override
    public void remover(IComponenteMenu componente) {
        throw new UnsupportedOperationException("No se pueden remover componentes de un item simple");
    }
    
    @Override
    public List<IComponenteMenu> getHijos() {
        return new ArrayList<>();
    }
}
