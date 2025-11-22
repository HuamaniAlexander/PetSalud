package gof.comportamiento.command;

import dao.*;
import modelo.entidades.*;
import java.sql.SQLException;
import java.util.Stack;

/**
 * Patron Command - Encapsula operaciones como objetos
 * Permite deshacer/rehacer y auditoria de acciones
 */

// Comando: Registrar orden
public class ComandoRegistrarOrden implements ICommand {
    private OrdenDAO ordenDAO;
    private OrdenVeterinaria orden;
    private Integer idOrdenCreada;
    
    public ComandoRegistrarOrden(OrdenVeterinaria orden) {
        this.ordenDAO = new OrdenDAO();
        this.orden = orden;
    }
    
    @Override
    public void ejecutar() {
        try {
            OrdenVeterinaria ordenCreada = ordenDAO.crear(orden);
            this.idOrdenCreada = ordenCreada.getIdOrden();
            System.out.println("Orden #" + idOrdenCreada + " registrada");
        } catch (SQLException e) {
            System.err.println("Error al registrar orden: " + e.getMessage());
        }
    }
    
    @Override
    public void deshacer() {
        if (idOrdenCreada != null) {
            try {
                ordenDAO.eliminar(idOrdenCreada);
                System.out.println("Orden #" + idOrdenCreada + " eliminada (deshacer)");
            } catch (SQLException e) {
                System.err.println("Error al deshacer: " + e.getMessage());
            }
        }
    }
    
    @Override
    public String getDescripcion() {
        return "Registrar orden de " + orden.getTipoExamen();
    }
}


// Comando: Generar informe
class ComandoGenerarInforme implements ICommand {
    private int idResultado;
    private String informeGenerado;
    
    public ComandoGenerarInforme(int idResultado) {
        this.idResultado = idResultado;
    }
    
    @Override
    public void ejecutar() {
        // Simula generacion de informe
        informeGenerado = "Informe_" + idResultado + "_" + System.currentTimeMillis() + ".pdf";
        System.out.println("Informe generado: " + informeGenerado);
    }
    
    @Override
    public void deshacer() {
        // Simula eliminacion de informe
        System.out.println("Informe eliminado: " + informeGenerado);
        informeGenerado = null;
    }
    
    @Override
    public String getDescripcion() {
        return "Generar informe para resultado #" + idResultado;
    }
}

// Comando: Enviar notificacion
class ComandoEnviarNotificacion implements ICommand {
    private String destinatario;
    private String mensaje;
    private boolean enviado;
    
    public ComandoEnviarNotificacion(String destinatario, String mensaje) {
        this.destinatario = destinatario;
        this.mensaje = mensaje;
    }
    
    @Override
    public void ejecutar() {
        // Simula envio de notificacion
        System.out.println("Notificacion enviada a: " + destinatario);
        System.out.println("Mensaje: " + mensaje);
        enviado = true;
    }
    
    @Override
    public void deshacer() {
        // No se puede deshacer un envio de notificacion
        System.out.println("No se puede deshacer envio de notificacion");
    }
    
    @Override
    public String getDescripcion() {
        return "Enviar notificacion a " + destinatario;
    }
}

// Comando: Actualizar estado de orden
class ComandoActualizarEstado implements ICommand {
    private OrdenDAO ordenDAO;
    private int idOrden;
    private String nuevoEstado;
    private String estadoAnterior;
    
    public ComandoActualizarEstado(int idOrden, String nuevoEstado) {
        this.ordenDAO = new OrdenDAO();
        this.idOrden = idOrden;
        this.nuevoEstado = nuevoEstado;
    }
    
    @Override
    public void ejecutar() {
        try {
            OrdenVeterinaria orden = ordenDAO.obtenerPorId(idOrden);
            if (orden != null) {
                estadoAnterior = orden.getEstado().name();
                // Aqui se aplicarian las transiciones del patron State
                ordenDAO.actualizar(orden);
                System.out.println("Estado de orden #" + idOrden + " actualizado");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
    }
    
    @Override
    public void deshacer() {
        try {
            OrdenVeterinaria orden = ordenDAO.obtenerPorId(idOrden);
            if (orden != null && estadoAnterior != null) {
                ordenDAO.actualizar(orden);
                System.out.println("Estado de orden #" + idOrden + " restaurado");
            }
        } catch (SQLException e) {
            System.err.println("Error al deshacer: " + e.getMessage());
        }
    }
    
    @Override
    public String getDescripcion() {
        return "Actualizar estado de orden #" + idOrden + " a " + nuevoEstado;
    }
}

// Invoker: Ejecutor de comandos con soporte para undo/redo
class InvocadorComando {
    private Stack<ICommand> historial;
    private Stack<ICommand> rehacer;
    
    public InvocadorComando() {
        this.historial = new Stack<>();
        this.rehacer = new Stack<>();
    }
    
    public void ejecutar(ICommand comando) {
        comando.ejecutar();
        historial.push(comando);
        rehacer.clear(); // Limpiar pila de rehacer al ejecutar nuevo comando
        
        System.out.println("[LOG] Ejecutado: " + comando.getDescripcion());
    }
    
    public void deshacer() {
        if (!historial.isEmpty()) {
            ICommand comando = historial.pop();
            comando.deshacer();
            rehacer.push(comando);
            
            System.out.println("[LOG] Deshecho: " + comando.getDescripcion());
        } else {
            System.out.println("No hay comandos para deshacer");
        }
    }
    
    public void rehacerUltimo() {
        if (!rehacer.isEmpty()) {
            ICommand comando = rehacer.pop();
            comando.ejecutar();
            historial.push(comando);
            
            System.out.println("[LOG] Rehecho: " + comando.getDescripcion());
        } else {
            System.out.println("No hay comandos para rehacer");
        }
    }
    
    public void mostrarHistorial() {
        System.out.println("\n=== HISTORIAL DE COMANDOS ===");
        if (historial.isEmpty()) {
            System.out.println("No hay comandos en el historial");
        } else {
            int i = historial.size();
            for (ICommand comando : historial) {
                System.out.println(i + ". " + comando.getDescripcion());
                i--;
            }
        }
    }
    
    public void limpiarHistorial() {
        historial.clear();
        rehacer.clear();
        System.out.println("Historial limpiado");
    }
    
    public int getTamanoHistorial() {
        return historial.size();
    }
}