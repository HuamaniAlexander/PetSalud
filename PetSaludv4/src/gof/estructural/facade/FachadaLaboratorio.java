package gof.estructural.facade;

// Fachada especializada para laboratorio

import dao.OrdenDAO;
import dao.ResultadoDAO;
import java.sql.SQLException;
import modelo.Enumeraciones;
import modelo.entidades.OrdenVeterinaria;

public class FachadaLaboratorio {
    private OrdenDAO ordenDAO;
    private ResultadoDAO resultadoDAO;
    
    public FachadaLaboratorio() {
        this.ordenDAO = new OrdenDAO();
        this.resultadoDAO = new ResultadoDAO();
    }
    
    /**
     * Proceso: Iniciar procesamiento de orden
     */
    public boolean iniciarProcesamiento(int idOrden) {
        try {
            OrdenVeterinaria orden = ordenDAO.obtenerPorId(idOrden);
            if (orden == null) {
                return false;
            }
            
            if (orden.getEstado() == Enumeraciones.EstadoOrden.PENDIENTE) {
                orden.procesar(); // Patron State
                ordenDAO.actualizar(orden);
                System.out.println("Orden en procesamiento");
                return true;
            }
            
            return false;
        } catch (SQLException | IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtener ordenes pendientes de laboratorio
     */
    public String obtenerOrdenesPendientes() {
        try {
            var pendientes = ordenDAO.listarPorEstado(Enumeraciones.EstadoOrden.PENDIENTE);
            var enProceso = ordenDAO.listarPorEstado(Enumeraciones.EstadoOrden.EN_PROCESO);
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== ORDENES DE LABORATORIO ===\n\n");
            sb.append("PENDIENTES: ").append(pendientes.size()).append("\n");
            
            for (OrdenVeterinaria orden : pendientes) {
                sb.append("- Orden #").append(orden.getIdOrden())
                  .append(" | ").append(orden.getTipoExamen()).append("\n");
            }
            
            sb.append("\nEN PROCESO: ").append(enProceso.size()).append("\n");
            
            for (OrdenVeterinaria orden : enProceso) {
                sb.append("- Orden #").append(orden.getIdOrden())
                  .append(" | ").append(orden.getTipoExamen()).append("\n");
            }
            
            return sb.toString();
            
        } catch (SQLException e) {
            return "Error al obtener ordenes: " + e.getMessage();
        }
    }
}