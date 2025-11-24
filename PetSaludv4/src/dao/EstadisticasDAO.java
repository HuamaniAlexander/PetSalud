package dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO para obtener estadísticas del dashboard
 */
public class EstadisticasDAO extends GenericDAO<Object> {
    
    public Map<String, Integer> obtenerEstadisticasDashboard() throws SQLException {
        String sql = "{CALL sp_obtener_estadisticas_dashboard()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        Map<String, Integer> stats = new HashMap<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                stats.put("total_duenos", rs.getInt("total_duenos"));
                stats.put("total_mascotas", rs.getInt("total_mascotas"));
                stats.put("ordenes_pendientes", rs.getInt("ordenes_pendientes"));
                stats.put("ordenes_en_proceso", rs.getInt("ordenes_en_proceso"));
                stats.put("resultados_pendientes", rs.getInt("resultados_pendientes"));
                stats.put("ordenes_hoy", rs.getInt("ordenes_hoy"));
            }
            
            return stats;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public Object crear(Object entity) throws SQLException {
        return null;
    }
    
    @Override
    public Object obtenerPorId(int id) throws SQLException {
        return null;
    }
    
    @Override
    public java.util.List<Object> listarTodos() throws SQLException {
        return null;
    }
    
    @Override
    public Object actualizar(Object entity) throws SQLException {
        return null;
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        return false;
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