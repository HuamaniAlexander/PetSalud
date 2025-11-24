package dao;

import java.sql.*;
import java.util.*;

/**
 * DAO para gestión de reportes del sistema - VERSIÓN CORREGIDA
 */
public class ReportesDAO extends GenericDAO<Object> {
    
    // ============================================
    // REPORTE DE ORDENES
    // ============================================
    
    public List<Map<String, Object>> reporteOrdenes(java.util.Date fechaInicio, java.util.Date fechaFin, String estado) throws SQLException {
        String sql = "{CALL sp_reporte_ordenes_detallado(?, ?, ?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            
            // Convertir java.util.Date a java.sql.Date
            cs.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            cs.setDate(2, new java.sql.Date(fechaFin.getTime()));
            cs.setString(3, estado);
            
            rs = cs.executeQuery();
            
            // Obtener metadata para saber qué columnas existen
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                
                // Llenar el mapa con todas las columnas disponibles
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    fila.put(columnName, value);
                }
                
                resultados.add(fila);
            }
            
            return resultados;
        } catch (SQLException e) {
            System.err.println("Error en reporteOrdenes: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // REPORTE FINANCIERO
    // ============================================
    
    public List<Map<String, Object>> reporteFinanciero(java.util.Date fechaInicio, java.util.Date fechaFin) throws SQLException {
        String sql = "{CALL sp_reporte_financiero_detallado(?, ?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            cs.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            rs = cs.executeQuery();
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    fila.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultados.add(fila);
            }
            
            return resultados;
        } catch (SQLException e) {
            System.err.println("Error en reporteFinanciero: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public Map<String, Object> resumenFinanciero(java.util.Date fechaInicio, java.util.Date fechaFin) throws SQLException {
        String sql = "{CALL sp_resumen_financiero(?, ?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            cs.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            rs = cs.executeQuery();
            
            if (rs.next()) {
                Map<String, Object> resumen = new LinkedHashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                for (int i = 1; i <= columnCount; i++) {
                    resumen.put(metaData.getColumnName(i), rs.getObject(i));
                }
                return resumen;
            }
            
            return new HashMap<>();
        } catch (SQLException e) {
            System.err.println("Error en resumenFinanciero: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // REPORTE DE LABORATORIO
    // ============================================
    
    public List<Map<String, Object>> reporteLaboratorio(java.util.Date fechaInicio, java.util.Date fechaFin) throws SQLException {
        String sql = "{CALL sp_reporte_laboratorio_detallado(?, ?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            cs.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            rs = cs.executeQuery();
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    fila.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultados.add(fila);
            }
            
            return resultados;
        } catch (SQLException e) {
            System.err.println("Error en reporteLaboratorio: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // REPORTE DE VETERINARIOS
    // ============================================
    
    public List<Map<String, Object>> reporteVeterinarios(java.util.Date fechaInicio, java.util.Date fechaFin) throws SQLException {
        String sql = "{CALL sp_reporte_veterinarios(?, ?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            cs.setDate(2, new java.sql.Date(fechaFin.getTime()));
            
            rs = cs.executeQuery();
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    fila.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultados.add(fila);
            }
            
            return resultados;
        } catch (SQLException e) {
            System.err.println("Error en reporteVeterinarios: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // REPORTE TOP CLIENTES
    // ============================================
    
    public List<Map<String, Object>> reporteTopClientes(java.util.Date fechaInicio, java.util.Date fechaFin, int limite) throws SQLException {
        String sql = "{CALL sp_reporte_top_clientes(?, ?, ?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            cs.setDate(2, new java.sql.Date(fechaFin.getTime()));
            cs.setInt(3, limite);
            
            rs = cs.executeQuery();
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    fila.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultados.add(fila);
            }
            
            return resultados;
        } catch (SQLException e) {
            System.err.println("Error en reporteTopClientes: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // MÉTODOS NO IMPLEMENTADOS (requeridos por GenericDAO)
    // ============================================
    
    @Override
    public Object crear(Object entity) throws SQLException {
        return null;
    }
    
    @Override
    public Object obtenerPorId(int id) throws SQLException {
        return null;
    }
    
    @Override
    public List<Object> listarTodos() throws SQLException {
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