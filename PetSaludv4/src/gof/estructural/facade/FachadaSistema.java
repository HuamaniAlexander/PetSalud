package gof.estructural.facade;

import dao.*;
import modelo.entidades.*;
import modelo.Enumeraciones.*;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Patron Facade - Simplifica operaciones complejas del sistema
 * Coordina multiples subsistemas para realizar procesos completos
 */

// Fachada principal del sistema
public class FachadaSistema {
    private DuenoDAO duenoDAO;
    private MascotaDAO mascotaDAO;
    private OrdenDAO ordenDAO;
    private ResultadoDAO resultadoDAO;
    private UsuarioDAO usuarioDAO;
    
    public FachadaSistema() {
        this.duenoDAO = new DuenoDAO();
        this.mascotaDAO = new MascotaDAO();
        this.ordenDAO = new OrdenDAO();
        this.resultadoDAO = new ResultadoDAO();
        this.usuarioDAO = new UsuarioDAO();
    }
    
    /**
     * Proceso completo: Registrar nuevo dueno con su mascota
     */
    public boolean registrarDuenoConMascota(Dueno dueno, Mascota mascota) {
        try {
            // 1. Validar que el dueno no exista
            Dueno duenoExistente = duenoDAO.buscarPorDNI(dueno.getDni());
            if (duenoExistente != null) {
                System.out.println("El dueno con DNI " + dueno.getDni() + " ya existe");
                return false;
            }
            
            // 2. Registrar dueno
            Dueno duenoCreado = duenoDAO.crear(dueno);
            System.out.println("Dueno registrado: " + duenoCreado.getNombreCompleto());
            
            // 3. Asociar mascota al dueno
            mascota.setIdDueno(duenoCreado.getIdDueno());
            
            // 4. Registrar mascota
            Mascota mascotaCreada = mascotaDAO.crear(mascota);
            System.out.println("Mascota registrada: " + mascotaCreada.getNombre());
            
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error en registro: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Proceso completo: Crear orden y notificar
     */
    public OrdenVeterinaria crearOrdenCompleta(int idMascota, int idVeterinario, 
                                                TipoExamen tipoExamen, String observaciones) {
        try {
            // 1. Verificar que la mascota existe
            Mascota mascota = mascotaDAO.obtenerPorId(idMascota);
            if (mascota == null) {
                System.out.println("Mascota no encontrada");
                return null;
            }
            
            // 2. Crear orden
            OrdenVeterinaria orden = new OrdenVeterinaria(tipoExamen, idMascota, idVeterinario);
            orden.setObservaciones(observaciones);
            orden = ordenDAO.crear(orden);
            
            System.out.println("Orden #" + orden.getIdOrden() + " creada exitosamente");
            
            // 3. Aqui se podria notificar via Observer
            // notificationService.notificarNuevaOrden(orden);
            
            return orden;
            
        } catch (SQLException e) {
            System.err.println("Error al crear orden: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Proceso completo: Registrar resultado y actualizar estado de orden
     */
    public boolean procesarResultadoCompleto(int idOrden, String descripcion, 
                                             String valores, String conclusiones) {
        try {
            // 1. Obtener orden
            OrdenVeterinaria orden = ordenDAO.obtenerPorId(idOrden);
            if (orden == null) {
                System.out.println("Orden no encontrada");
                return false;
            }
            
            // 2. Verificar que la orden este en proceso
            if (orden.getEstado() != EstadoOrden.EN_PROCESO) {
                System.out.println("La orden debe estar en proceso para registrar resultados");
                return false;
            }
            
            // 3. Crear resultado
            ResultadoVeterinario resultado = new ResultadoVeterinario(descripcion, valores, idOrden);
            resultado.setConclusiones(conclusiones);
            resultado = resultadoDAO.crear(resultado);
            
            // 4. Actualizar estado de orden
            orden.completar(); // Usa patron State
            ordenDAO.actualizar(orden);
            
            System.out.println("Resultado registrado y orden completada");
            
            // 5. Notificar
            // notificationService.notificarResultadoListo(orden);
            
            return true;
            
        } catch (SQLException | IllegalStateException e) {
            System.err.println("Error al procesar resultado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Proceso completo: Validar resultado y entregar
     */
    public boolean validarYEntregarResultado(int idResultado, int idVeterinario) {
        try {
            // 1. Obtener resultado
            ResultadoVeterinario resultado = resultadoDAO.obtenerPorId(idResultado);
            if (resultado == null) {
                System.out.println("Resultado no encontrado");
                return false;
            }
            
            // 2. Validar resultado
            resultado.validar(idVeterinario);
            resultadoDAO.actualizar(resultado);
            
            // 3. Actualizar orden a VALIDADA
            OrdenVeterinaria orden = ordenDAO.obtenerPorId(resultado.getIdOrden());
            orden.validar(); // Patron State
            ordenDAO.actualizar(orden);
            
            // 4. Generar informe (usando Decorator)
            // IInforme informe = new InformeConQR(new InformeConFirma(new InformeBasico(resultado)));
            // String informeGenerado = informe.generar();
            
            System.out.println("Resultado validado exitosamente");
            
            // 5. Notificar al dueno
            // notificationService.notificarResultadoValidado(orden);
            
            return true;
            
        } catch (SQLException | IllegalStateException e) {
            System.err.println("Error al validar resultado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Consulta completa: Obtener historial de mascota
     */
    public String obtenerHistorialMascota(int idMascota) {
        try {
            // 1. Obtener mascota
            Mascota mascota = mascotaDAO.obtenerPorId(idMascota);
            if (mascota == null) {
                return "Mascota no encontrada";
            }
            
            // 2. Obtener dueno
            Dueno dueno = duenoDAO.obtenerPorId(mascota.getIdDueno());
            
            // 3. Obtener ordenes
            var ordenes = ordenDAO.listarPorMascota(idMascota);
            
            // 4. Construir reporte
            StringBuilder reporte = new StringBuilder();
            reporte.append("=== HISTORIAL MEDICO ===\n\n");
            reporte.append("Mascota: ").append(mascota.getNombre()).append("\n");
            reporte.append("Especie: ").append(mascota.getEspecie()).append("\n");
            reporte.append("Dueno: ").append(dueno.getNombreCompleto()).append("\n\n");
            reporte.append("ORDENES REGISTRADAS: ").append(ordenes.size()).append("\n");
            
            for (OrdenVeterinaria orden : ordenes) {
                reporte.append("- Orden #").append(orden.getIdOrden())
                       .append(" | ").append(orden.getTipoExamen())
                       .append(" | ").append(orden.getEstado())
                       .append(" | ").append(orden.getFechaOrden()).append("\n");
            }
            
            return reporte.toString();
            
        } catch (SQLException e) {
            return "Error al obtener historial: " + e.getMessage();
        }
    }
}
