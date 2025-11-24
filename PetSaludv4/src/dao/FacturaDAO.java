package dao;

import modelo.entidades.Factura;
import modelo.Enumeraciones.MetodoPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Factura usando Procedimientos Almacenados
 */
public class FacturaDAO extends GenericDAO<Factura> {
    
    @Override
    public Factura crear(Factura factura) throws SQLException {
        String sql = "{CALL sp_crear_factura(?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setDouble(1, factura.getMontoTotal());
            cs.setString(2, factura.getMetodoPago().name());
            cs.setInt(3, factura.getIdDueno());
            cs.registerOutParameter(4, Types.INTEGER);
            
            cs.execute();
            factura.setIdFactura(cs.getInt(4));
            
            return factura;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public Factura obtenerPorId(int id) throws SQLException {
        String sql = "{CALL sp_obtener_factura_por_id(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearFactura(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public List<Factura> listarTodos() throws SQLException {
        String sql = "{CALL sp_listar_facturas()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Factura> facturas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                facturas.add(mapearFactura(rs));
            }
            return facturas;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public Factura actualizar(Factura factura) throws SQLException {
        String sql = "{CALL sp_actualizar_factura(?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, factura.getIdFactura());
            cs.setDouble(2, factura.getMontoTotal());
            cs.setString(3, factura.getMetodoPago().name());
            cs.setBoolean(4, factura.isPagado());
            
            cs.execute();
            return factura;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        // No se implementa eliminación física por restricciones de integridad
        return false;
    }
    
    public List<Factura> listarPorDueno(int idDueno) throws SQLException {
        String sql = "{CALL sp_listar_facturas_por_dueno(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Factura> facturas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, idDueno);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                facturas.add(mapearFactura(rs));
            }
            return facturas;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public List<Factura> listarPendientes() throws SQLException {
        String sql = "{CALL sp_listar_facturas_pendientes()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Factura> facturas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                facturas.add(mapearFactura(rs));
            }
            return facturas;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public boolean marcarComoPagada(int idFactura) throws SQLException {
        String sql = "{CALL sp_marcar_factura_pagada(?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, idFactura);
            cs.execute();
            return true;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    private Factura mapearFactura(ResultSet rs) throws SQLException {
        Factura factura = new Factura();
        factura.setIdFactura(rs.getInt("id_factura"));
        factura.setFechaEmision(rs.getTimestamp("fecha_emision"));
        factura.setMontoTotal(rs.getDouble("monto_total"));
        factura.setMetodoPago(MetodoPago.valueOf(rs.getString("metodo_pago")));
        factura.setIdDueno(rs.getInt("id_dueno"));
        factura.setPagado(rs.getBoolean("pagado"));
        return factura;
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