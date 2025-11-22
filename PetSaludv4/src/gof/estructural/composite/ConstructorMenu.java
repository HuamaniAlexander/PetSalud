/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gof.estructural.composite;

import modelo.Enumeraciones;
import static modelo.Enumeraciones.RolUsuario.ADMIN;
import static modelo.Enumeraciones.RolUsuario.RECEPCIONISTA;
import static modelo.Enumeraciones.RolUsuario.TECNICO;
import static modelo.Enumeraciones.RolUsuario.VETERINARIO;


// Constructor de menus segun rol
public class ConstructorMenu {
    
    public static IComponenteMenu construirMenuPorRol(Enumeraciones.RolUsuario rol) {
        GrupoMenu menuPrincipal = new GrupoMenu("Sistema Veterinario");
        
        switch (rol) {
            case ADMIN:
                construirMenuAdmin(menuPrincipal);
                break;
            case VETERINARIO:
                construirMenuVeterinario(menuPrincipal);
                break;
            case TECNICO:
                construirMenuTecnico(menuPrincipal);
                break;
            case RECEPCIONISTA:
                construirMenuRecepcionista(menuPrincipal);
                break;
        }
        
        // Opciones comunes para todos
        menuPrincipal.agregar(new ItemMenu("Configuracion"));
        menuPrincipal.agregar(new ItemMenu("Cerrar Sesion"));
        
        return menuPrincipal;
    }
    
    private static void construirMenuAdmin(GrupoMenu menu) {
        // Gestion de usuarios
        GrupoMenu usuarios = new GrupoMenu("Usuarios");
        usuarios.agregar(new ItemMenu("Crear Usuario"));
        usuarios.agregar(new ItemMenu("Listar Usuarios"));
        usuarios.agregar(new ItemMenu("Modificar Usuario"));
        menu.agregar(usuarios);
        
        // Gestion de personal
        GrupoMenu personal = new GrupoMenu("Personal");
        personal.agregar(new ItemMenu("Registrar Veterinario"));
        personal.agregar(new ItemMenu("Registrar Tecnico"));
        personal.agregar(new ItemMenu("Listar Personal"));
        menu.agregar(personal);
        
        // Reportes
        GrupoMenu reportes = new GrupoMenu("Reportes");
        reportes.agregar(new ItemMenu("Reporte Financiero"));
        reportes.agregar(new ItemMenu("Reporte de Ordenes"));
        reportes.agregar(new ItemMenu("Reporte de Laboratorio"));
        menu.agregar(reportes);
        
        // Acceso completo a todo
        menu.agregar(new ItemMenu("Gestion de Duenos y Mascotas"));
        menu.agregar(new ItemMenu("Gestion de Ordenes"));
        menu.agregar(new ItemMenu("Facturacion"));
    }
    
    private static void construirMenuVeterinario(GrupoMenu menu) {
        // Gestion de pacientes
        GrupoMenu pacientes = new GrupoMenu("Pacientes");
        pacientes.agregar(new ItemMenu("Buscar Mascota"));
        pacientes.agregar(new ItemMenu("Ver Historial"));
        pacientes.agregar(new ItemMenu("Registrar Consulta"));
        menu.agregar(pacientes);
        
        // Ordenes
        GrupoMenu ordenes = new GrupoMenu("Ordenes");
        ordenes.agregar(new ItemMenu("Nueva Orden"));
        ordenes.agregar(new ItemMenu("Mis Ordenes"));
        ordenes.agregar(new ItemMenu("Validar Resultados"));
        menu.agregar(ordenes);
        
        // Resultados
        menu.agregar(new ItemMenu("Consultar Resultados"));
        menu.agregar(new ItemMenu("Generar Informes"));
    }
    
    private static void construirMenuTecnico(GrupoMenu menu) {
        // Laboratorio
        GrupoMenu laboratorio = new GrupoMenu("Laboratorio");
        laboratorio.agregar(new ItemMenu("Ordenes Pendientes"));
        laboratorio.agregar(new ItemMenu("Registrar Toma de Muestra"));
        laboratorio.agregar(new ItemMenu("Registrar Resultados"));
        laboratorio.agregar(new ItemMenu("Mis Analisis"));
        menu.agregar(laboratorio);
        
        menu.agregar(new ItemMenu("Consultar Ordenes"));
    }
    
    private static void construirMenuRecepcionista(GrupoMenu menu) {
        // Registro
        GrupoMenu registro = new GrupoMenu("Registro");
        registro.agregar(new ItemMenu("Registrar Dueno"));
        registro.agregar(new ItemMenu("Registrar Mascota"));
        registro.agregar(new ItemMenu("Buscar Dueno"));
        registro.agregar(new ItemMenu("Buscar Mascota"));
        menu.agregar(registro);
        
        // Citas y ordenes
        menu.agregar(new ItemMenu("Agendar Cita"));
        menu.agregar(new ItemMenu("Ver Ordenes"));
        
        // Facturacion
        GrupoMenu facturacion = new GrupoMenu("Facturacion");
        facturacion.agregar(new ItemMenu("Generar Factura"));
        facturacion.agregar(new ItemMenu("Consultar Facturas"));
        facturacion.agregar(new ItemMenu("Registrar Pago"));
        menu.agregar(facturacion);
    }
}