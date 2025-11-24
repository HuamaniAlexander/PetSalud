package dao;

import java.sql.*;
import java.util.*;

/**
 * DAO para gestión de reportes del sistema
 */
public class ReportesDAO extends GenericDAO<Object> {
    
    // ============================================
    // REPORTE DE ORDENES
    // ============================================
    
    public List<Map<String, Object>> reporteOrdenes(Date fechaInicio, Date fechaFin, String estado) throws SQLException {
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
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("id_orden", rs.getInt("id_orden"));
                fila.put("fecha_orden", rs.getString("fecha_orden"));
                fila.put("tipo_examen", rs.getString("tipo_examen"));
                fila.put("estado", rs.getString("estado"));
                fila.put("mascota", rs.getString("mascota"));
                fila.put("especie", rs.getString("especie"));
                fila.put("dueno", rs.getString("dueno"));
                fila.put("telefono_dueno", rs.getString("telefono_dueno"));
                fila.put("veterinario", rs.getString("veterinario"));
                fila.put("especialidad_veterinario", rs.getString("especialidad_veterinario"));
                fila.put("observaciones", rs.getString("observaciones"));
                fila.put("tiene_resultado", rs.getString("tiene_resultado"));
                fila.put("estado_resultado", rs.getString("estado_resultado"));
                resultados.add(fila);
            }
            
            return resultados;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // REPORTE FINANCIERO
    // ============================================
    
    public List<Map<String, Object>> reporteFinanciero(Date fechaInicio, Date fechaFin) throws SQLException {
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
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("id_factura", rs.getInt("id_factura"));
                fila.put("fecha", rs.getString("fecha"));
                fila.put("cliente", rs.getString("cliente"));
                fila.put("telefono", rs.getString("telefono"));
                fila.put("metodo_pago", rs.getString("metodo_pago"));
                fila.put("monto_total", rs.getDouble("monto_total"));
                fila.put("estado_pago", rs.getString("estado_pago"));
                fila.put("servicios", rs.getString("servicios"));
                resultados.add(fila);
            }
            
            return resultados;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public Map<String, Object> resumenFinanciero(Date fechaInicio, Date fechaFin) throws SQLException {
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
                resumen.put("total_facturas", rs.getInt("total_facturas"));
                resumen.put("total_facturado", rs.getDouble("total_facturado"));
                resumen.put("total_pagado", rs.getDouble("total_pagado"));
                resumen.put("total_pendiente", rs.getDouble("total_pendiente"));
                resumen.put("promedio_factura", rs.getDouble("promedio_factura"));
                resumen.put("total_efectivo", rs.getDouble("total_efectivo"));
                resumen.put("total_tarjeta", rs.getDouble("total_tarjeta"));
                resumen.put("total_transferencia", rs.getDouble("total_transferencia"));
                return resumen;
            }
            
            return new HashMap<>();
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // REPORTE DE LABORATORIO
    // ============================================
    
    public List<Map<String, Object>> reporteLaboratorio(Date fechaInicio, Date fechaFin) throws SQLException {
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
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("tipo_examen", rs.getString("tipo_examen"));
                fila.put("cantidad_analisis", rs.getInt("cantidad_analisis"));
                fila.put("completados", rs.getInt("completados"));
                fila.put("validados", rs.getInt("validados"));
                fila.put("pendientes", rs.getInt("pendientes"));
                fila.put("en_proceso", rs.getInt("en_proceso"));
                fila.put("horas_promedio", rs.getDouble("horas_promedio"));
                fila.put("primera_fecha", rs.getString("primera_fecha"));
                fila.put("ultima_fecha", rs.getString("ultima_fecha"));
                resultados.add(fila);
            }
            
            return resultados;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // REPORTE DE VETERINARIOS
    // ============================================
    
    public List<Map<String, Object>> reporteVeterinarios(Date fechaInicio, Date fechaFin) throws SQLException {
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
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("id_veterinario", rs.getInt("id_veterinario"));
                fila.put("veterinario", rs.getString("veterinario"));
                fila.put("especialidad", rs.getString("especialidad"));
                fila.put("total_ordenes", rs.getInt("total_ordenes"));
                fila.put("pendientes", rs.getInt("pendientes"));
                fila.put("en_proceso", rs.getInt("en_proceso"));
                fila.put("completadas", rs.getInt("completadas"));
                fila.put("validadas", rs.getInt("validadas"));
                fila.put("mascotas_atendidas", rs.getInt("mascotas_atendidas"));
                fila.put("clientes_atendidos", rs.getInt("clientes_atendidos"));
                resultados.add(fila);
            }
            
            return resultados;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    // ============================================
    // REPORTE TOP CLIENTES
    // ============================================
    
    public List<Map<String, Object>> reporteTopClientes(Date fechaInicio, Date fechaFin, int limite) throws SQLException {
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
            
            while (rs.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                fila.put("id_dueno", rs.getInt("id_dueno"));
                fila.put("cliente", rs.getString("cliente"));
                fila.put("telefono", rs.getString("telefono"));
                fila.put("email", rs.getString("email"));
                fila.put("total_mascotas", rs.getInt("total_mascotas"));
                fila.put("total_ordenes", rs.getInt("total_ordenes"));
                fila.put("total_gastado", rs.getDouble("total_gastado"));
                fila.put("promedio_gasto", rs.getDouble("promedio_gasto"));
                fila.put("ultima_visita", rs.getString("ultima_visita"));
                resultados.add(fila);
            }
            
            return resultados;
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