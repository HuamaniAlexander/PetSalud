package controlador;

import modelo.entidades.Usuario;
import gof.estructural.composite.ConstructorMenu;
import gof.estructural.composite.IComponenteMenu;

/**
 * Controlador principal del sistema
 */
public class ControladorPrincipal {
    private Usuario usuarioActual;
    
    public ControladorPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
    }
    
    // Construir menu dinamico usando patron Composite
    public IComponenteMenu construirMenu() {
        return ConstructorMenu.construirMenuPorRol(usuarioActual.getRol());
    }
    
    public void mostrarMenu() {
        IComponenteMenu menu = construirMenu();
        System.out.println("\n=== MENU PRINCIPAL ===");
        menu.mostrar(0);
        System.out.println("======================\n");
    }
    
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
}