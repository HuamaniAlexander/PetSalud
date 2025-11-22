package gof.estructural.proxy;

import dao.ResultadoDAO;
import modelo.entidades.ResultadoVeterinario;
import modelo.entidades.Usuario;
import modelo.Enumeraciones.RolUsuario;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Patron Proxy - Control de acceso y cache para resultados veterinarios
 */


// Sujeto real
public class AccesoResultadoReal implements IAccesoResultado {
    private ResultadoDAO resultadoDAO;
    
    public AccesoResultadoReal() {
        this.resultadoDAO = new ResultadoDAO();
    }
    
    @Override
    public ResultadoVeterinario obtenerResultado(int idResultado, Usuario usuario) throws SQLException {
        System.out.println("Accediendo a BD para resultado #" + idResultado);
        return resultadoDAO.obtenerPorId(idResultado);
    }
    
    @Override
    public boolean validarResultado(int idResultado, Usuario usuario) throws SQLException {
        ResultadoVeterinario resultado = resultadoDAO.obtenerPorId(idResultado);
        if (resultado != null && !resultado.isValidado()) {
            resultado.setValidado(true);
            resultado.setIdValidador(usuario.getIdUsuario());
            resultadoDAO.actualizar(resultado);
            return true;
        }
        return false;
    }
    
    @Override
    public String generarInforme(int idResultado, Usuario usuario) {
        try {
            ResultadoVeterinario resultado = resultadoDAO.obtenerPorId(idResultado);
            if (resultado != null) {
                return "Informe generado para resultado #" + idResultado;
            }
            return "Resultado no encontrado";
        } catch (SQLException e) {
            return "Error al generar informe";
        }
    }
}
