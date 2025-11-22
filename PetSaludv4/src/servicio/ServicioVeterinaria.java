package servicio;

import dao.*;
import modelo.entidades.*;
import modelo.Enumeraciones.*;
import gof.estructural.facade.FachadaSistema;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio principal de logica de negocio veterinaria
 * Coordina operaciones entre DAOs y aplica reglas de negocio
 */
public class ServicioVeterinaria {
    private DuenoDAO duenoDAO;
    private MascotaDAO mascotaDAO;
    private OrdenDAO ordenDAO;
    private ResultadoDAO resultadoDAO;
    private FachadaSistema fachada;
    
    public ServicioVeterinaria() {
        this.duenoDAO = new DuenoDAO();
        this.mascotaDAO = new MascotaDAO();
        this.ordenDAO = new OrdenDAO();
        this.resultadoDAO = new ResultadoDAO();
        this.fachada = new FachadaSistema();
    }
    
    // --- DUENOS ---
    public Dueno registrarDueno(Dueno dueno) throws SQLException {
        // Validar que no exista
        Dueno existente = duenoDAO.buscarPorDNI(dueno.getDni());
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un dueno con DNI: " + dueno.getDni());
        }
        return duenoDAO.crear(dueno);
    }
    
    public List<Dueno> buscarDuenoPorNombre(String nombre) throws SQLException {
        return duenoDAO.buscarPorNombre(nombre);
    }
    
    public Dueno buscarDuenoPorDNI(String dni) throws SQLException {
        return duenoDAO.buscarPorDNI(dni);
    }
    
    // --- MASCOTAS ---
    public Mascota registrarMascota(Mascota mascota) throws SQLException {
        // Validar que el dueno existe
        Dueno dueno = duenoDAO.obtenerPorId(mascota.getIdDueno());
        if (dueno == null) {
            throw new IllegalArgumentException("Dueno no encontrado");
        }
        return mascotaDAO.crear(mascota);
    }
    
    public List<Mascota> listarMascotasPorDueno(int idDueno) throws SQLException {
        return mascotaDAO.listarPorDueno(idDueno);
    }
    
    public List<Mascota> buscarMascotaPorNombre(String nombre) throws SQLException {
        return mascotaDAO.buscarPorNombre(nombre);
    }
    
    // --- ORDENES ---
    public OrdenVeterinaria crearOrden(int idMascota, int idVeterinario, 
                                       TipoExamen tipoExamen, String observaciones) {
        return fachada.crearOrdenCompleta(idMascota, idVeterinario, tipoExamen, observaciones);
    }
    
    public List<OrdenVeterinaria> listarOrdenesPorEstado(EstadoOrden estado) throws SQLException {
        return ordenDAO.listarPorEstado(estado);
    }
    
    public OrdenVeterinaria obtenerOrden(int idOrden) throws SQLException {
        return ordenDAO.obtenerPorId(idOrden);
    }
    
    public boolean procesarOrden(int idOrden) throws SQLException {
        OrdenVeterinaria orden = ordenDAO.obtenerPorId(idOrden);
        if (orden != null && orden.getEstado() == EstadoOrden.PENDIENTE) {
            orden.procesar();
            ordenDAO.actualizar(orden);
            return true;
        }
        return false;
    }
    
    // --- RESULTADOS ---
    public boolean registrarResultado(int idOrden, String descripcion, 
                                      String valores, String conclusiones) {
        return fachada.procesarResultadoCompleto(idOrden, descripcion, valores, conclusiones);
    }
    
    public boolean validarResultado(int idResultado, int idVeterinario) {
        return fachada.validarYEntregarResultado(idResultado, idVeterinario);
    }
    
    public List<ResultadoVeterinario> listarResultadosPendientes() throws SQLException {
        return resultadoDAO.listarPendientesValidacion();
    }
    
    public ResultadoVeterinario obtenerResultadoPorOrden(int idOrden) throws SQLException {
        return resultadoDAO.obtenerPorOrden(idOrden);
    }
    
    // --- HISTORIAL ---
    public String obtenerHistorialMascota(int idMascota) {
        return fachada.obtenerHistorialMascota(idMascota);
    }
}