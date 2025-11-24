package dao;

import modelo.entidades.ResultadoVeterinario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultadoDAO extends GenericDAO<ResultadoVeterinario> {
    
    @Override
    public ResultadoVeterinario crear(ResultadoVeterinario resultado) throws SQLException {
        String sql = "{CALL sp_crear_resultado(?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, resultado.getDescripcion());
            cs.setString(2, resultado.getValores());
            cs.setString(3, resultado.getConclusiones());
            cs.setInt(4, resultado.getIdOrden());
            cs.registerOutParameter(5, Types.INTEGER);
            
            cs.execute();
            resultado.setIdResultado(cs.getInt(5));
            
            return resultado;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public ResultadoVeterinario obtenerPorId(int id) throws SQLException {
        String sql = "{CALL sp_obtener_resultado_por_id(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearResultado(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public List<ResultadoVeterinario> listarTodos() throws SQLException {
        return new ArrayList<>();
    }
    
    @Override
    public ResultadoVeterinario actualizar(ResultadoVeterinario resultado) throws SQLException {
        if (resultado.isValidado() && resultado.getIdValidador() != null) {
            String sql = "{CALL sp_validar_resultado(?, ?)}";
            CallableStatement cs = null;
            
            try {
                Connection conn = getConnection();
                cs = conn.prepareCall(sql);
                cs.setInt(1, resultado.getIdResultado());
                cs.setInt(2, resultado.getIdValidador());
                cs.execute();
                return resultado;
            } finally {
                if (cs != null) cs.close();
            }
        }
        return resultado;
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        return false;
    }
    
    public ResultadoVeterinario obtenerPorOrden(int idOrden) throws SQLException {
        String sql = "{CALL sp_obtener_resultado_por_orden(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, idOrden);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearResultado(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public List<ResultadoVeterinario> listarPendientesValidacion() throws SQLException {
        String sql = "{CALL sp_listar_resultados_pendientes()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<ResultadoVeterinario> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                resultados.add(mapearResultado(rs));
            }
            return resultados;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    private ResultadoVeterinario mapearResultado(ResultSet rs) throws SQLException {
        ResultadoVeterinario resultado = new ResultadoVeterinario();
        resultado.setIdResultado(rs.getInt("id_resultado"));
        resultado.setFechaResultado(rs.getTimestamp("fecha_resultado"));
        resultado.setDescripcion(rs.getString("descripcion"));
        resultado.setValores(rs.getString("valores"));
        resultado.setConclusiones(rs.getString("conclusiones"));
        resultado.setValidado(rs.getBoolean("validado"));
        resultado.setIdOrden(rs.getInt("id_orden"));
        
        try {
            int idValidador = rs.getInt("id_validador");
            if (!rs.wasNull()) {
                resultado.setIdValidador(idValidador);
            }
        } catch (SQLException e) {}
        
        try {
            Timestamp fechaValidacion = rs.getTimestamp("fecha_validacion");
            if (fechaValidacion != null) {
                resultado.setFechaValidacion(fechaValidacion);
            }
        } catch (SQLException e) {}
        
        return resultado;
    }
    
    private void cerrarRecursos(ResultSet rs, CallableStatement cs) {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}