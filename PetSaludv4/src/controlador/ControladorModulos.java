package controlador;

import servicio.ServicioVeterinaria;
import modelo.entidades.*;
import modelo.Enumeraciones.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que gestiona todos los modulos funcionales
 */
public class ControladorModulos {
    private ServicioVeterinaria servicioVet;
    
    public ControladorModulos() {
        this.servicioVet = new ServicioVeterinaria();
    }
    
    // --- DUENOS ---
    public Dueno registrarDueno(String dni, String nombres, String apellidos, 
                                String telefono, String email, String direccion) {
        try {
            Dueno dueno = new Dueno(dni, nombres, apellidos, telefono);
            dueno.setEmail(email);
            dueno.setDireccion(direccion);
            
            return servicioVet.registrarDueno(dueno);
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error al registrar dueno: " + e.getMessage());
            return null;
        }
    }
    
    public Dueno buscarDuenoPorDNI(String dni) {
        try {
            return servicioVet.buscarDuenoPorDNI(dni);
        } catch (SQLException e) {
            System.err.println("Error al buscar dueno: " + e.getMessage());
            return null;
        }
    }
    
    public List<Dueno> buscarDuenoPorNombre(String nombre) {
        try {
            return servicioVet.buscarDuenoPorNombre(nombre);
        } catch (SQLException e) {
            System.err.println("Error al buscar duenos: " + e.getMessage());
            return null;
        }
    }
    
    // --- MASCOTAS ---
    public Mascota registrarMascota(String nombre, String especie, String raza, 
                                    int edad, SexoMascota sexo, double peso, int idDueno) {
        try {
            Mascota mascota = new Mascota(nombre, especie, sexo, idDueno);
            mascota.setRaza(raza);
            mascota.setEdad(edad);
            mascota.setPeso(peso);
            
            return servicioVet.registrarMascota(mascota);
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error al registrar mascota: " + e.getMessage());
            return null;
        }
    }
    
    public List<Mascota> listarMascotasPorDueno(int idDueno) {
        try {
            return servicioVet.listarMascotasPorDueno(idDueno);
        } catch (SQLException e) {
            System.err.println("Error al listar mascotas: " + e.getMessage());
            return null;
        }
    }
    
    // NUEVO MÉTODO: Listar todas las mascotas
    public List<Mascota> listarTodasMascotas() {
        try {
            return servicioVet.listarTodasMascotas();
        } catch (SQLException e) {
            System.err.println("Error al listar mascotas: " + e.getMessage());
            return null;
        }
    }
    
    // --- ORDENES ---
    public OrdenVeterinaria crearOrden(int idMascota, int idVeterinario, 
                                       TipoExamen tipoExamen, String observaciones) {
        return servicioVet.crearOrden(idMascota, idVeterinario, tipoExamen, observaciones);
    }
    
    public List<OrdenVeterinaria> listarOrdenesPendientes() {
        try {
            return servicioVet.listarOrdenesPorEstado(EstadoOrden.PENDIENTE);
        } catch (SQLException e) {
            System.err.println("Error al listar ordenes: " + e.getMessage());
            return null;
        }
    }
    
    public boolean procesarOrden(int idOrden) {
        try {
            return servicioVet.procesarOrden(idOrden);
        } catch (SQLException e) {
            System.err.println("Error al procesar orden: " + e.getMessage());
            return false;
        }
    }
    
    // --- RESULTADOS ---
    public boolean registrarResultado(int idOrden, String descripcion, 
                                      String valores, String conclusiones) {
        return servicioVet.registrarResultado(idOrden, descripcion, valores, conclusiones);
    }
    
    public boolean validarResultado(int idResultado, int idVeterinario) {
        return servicioVet.validarResultado(idResultado, idVeterinario);
    }
    
    public List<ResultadoVeterinario> listarResultadosPendientes() {
        try {
            return servicioVet.listarResultadosPendientes();
        } catch (SQLException e) {
            System.err.println("Error al listar resultados: " + e.getMessage());
            return null;
        }
    }
    
    // --- HISTORIAL ---
    public String obtenerHistorialMascota(int idMascota) {
        return servicioVet.obtenerHistorialMascota(idMascota);
    }
}