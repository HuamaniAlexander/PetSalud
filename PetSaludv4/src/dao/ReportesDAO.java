package dao;

import java.sql.*;
import java.util.*;

public class ReportesDAO extends GenericDAO<Object> {
    
    public List<Map<String, Object>> reporteOrdenes(java.util.Date fechaInicio, java.util.Date fechaFin, String estado) throws SQLException {
        String sql = "{CALL sp_reporte_ordenes_detallado(?, ?, ?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultados = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            
            cs.setDate(1, new java.sql.Date(fechaInicio.getTime()));
            cs.setDate(2, new java.sql.Date(fechaFin.getTime()));
            cs.setString(3, estado);
            
            rs = cs.executeQuery();
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Debug: imprimir nombres de columnas
            System.out.println("=== Columnas del ResultSet ===");
            for (int i = 1; i <= columnCount; i++) {
                System.out.println(i + ": " + metaData.getColumnName(i));
            }
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    fila.put(columnName, value);
                    
                    // También agregar alias normalizados
                    String normalizedKey = normalizeKey(columnName);
                    fila.put(normalizedKey, value);
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
    
    private String normalizeKey(String key) {
        // Convertir nombres de columnas de BD a formato esperado
        String normalized = key.toLowerCase()
            .replace("_", " ")
            .trim();
        
        // Mapeo de claves comunes
        switch (normalized) {
            case "nombre mascota": return "mascota";
            case "nombre dueno": return "dueno";
            case "nombre veterinario": return "veterinario";
            case "telefono dueno": return "telefono_dueno";
            case "especialidad veterinario": return "especialidad_veterinario";
            default: return key; // mantener original también
        }
    }
    
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
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    fila.put(columnName, value);
                    fila.put(normalizeKey(columnName), value);
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
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    resumen.put(columnName, value);
                    resumen.put(normalizeKey(columnName), value);
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
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    fila.put(columnName, value);
                    fila.put(normalizeKey(columnName), value);
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
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    fila.put(columnName, value);
                    fila.put(normalizeKey(columnName), value);
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
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    fila.put(columnName, value);
                    fila.put(normalizeKey(columnName), value);
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