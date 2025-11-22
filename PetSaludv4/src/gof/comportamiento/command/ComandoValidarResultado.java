package gof.comportamiento.command;

import dao.ResultadoDAO;
import java.sql.SQLException;
import modelo.entidades.ResultadoVeterinario;

// Comando: Validar resultado
public class ComandoValidarResultado implements ICommand {
    private ResultadoDAO resultadoDAO;
    private int idResultado;
    private int idVeterinario;
    private boolean estadoAnterior;
    
    public ComandoValidarResultado(int idResultado, int idVeterinario) {
        this.resultadoDAO = new ResultadoDAO();
        this.idResultado = idResultado;
        this.idVeterinario = idVeterinario;
    }
    
    @Override
    public void ejecutar() {
        try {
            ResultadoVeterinario resultado = resultadoDAO.obtenerPorId(idResultado);
            if (resultado != null) {
                estadoAnterior = resultado.isValidado();
                resultado.validar(idVeterinario);
                resultadoDAO.actualizar(resultado);
                System.out.println("Resultado #" + idResultado + " validado");
            }
        } catch (SQLException | IllegalStateException e) {
            System.err.println("Error al validar resultado: " + e.getMessage());
        }
    }
    
    @Override
    public void deshacer() {
        try {
            ResultadoVeterinario resultado = resultadoDAO.obtenerPorId(idResultado);
            if (resultado != null) {
                resultado.setValidado(estadoAnterior);
                resultadoDAO.actualizar(resultado);
                System.out.println("Validacion de resultado #" + idResultado + " deshecha");
            }
        } catch (SQLException e) {
            System.err.println("Error al deshacer: " + e.getMessage());
        }
    }
    
    @Override
    public String getDescripcion() {
        return "Validar resultado #" + idResultado;
    }
}