package dao;

import modelo.entidades.DetalleFactura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para DetalleFactura usando Procedimientos Almacenados
 */
public class DetalleFacturaDAO extends GenericDAO<DetalleFactura> {
    
    @Override
    public DetalleFactura crear(DetalleFactura detalle) throws SQLException {
        String sql = "{CALL sp_crear_detalle_factura(?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, detalle.getDescripcionServicio());
            cs.setInt(2, detalle.getCantidad());
            cs.setDouble(3, detalle.getPrecioUnitario());
            cs.setInt(4, detalle.getIdFactura());
            cs.registerOutParameter(5, Types.INTEGER);
            
            cs.execute();
            detalle.setIdDetalle(cs.getInt(5));
            
            return detalle;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public DetalleFactura obtenerPorId(int id) throws SQLException {
        // No implementado - se usa listarPorFactura
        return null;
    }
    
    @Override
    public List<DetalleFactura> listarTodos() throws SQLException {
        // No implementado - se usa listarPorFactura
        return new ArrayList<>();
    }
    
    @Override
    public DetalleFactura actualizar(DetalleFactura detalle) throws SQLException {
        // No implementado - los detalles no se actualizan, se eliminan y crean nuevos
        return detalle;
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "{CALL sp_eliminar_detalle_factura(?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            cs.execute();
            return true;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    public List<DetalleFactura> listarPorFactura(int idFactura) throws SQLException {
        String sql = "{CALL sp_listar_detalles_por_factura(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<DetalleFactura> detalles = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, idFactura);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                detalles.add(mapearDetalle(rs));
            }
            return detalles;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    private DetalleFactura mapearDetalle(ResultSet rs) throws SQLException {
        DetalleFactura detalle = new DetalleFactura();
        detalle.setIdDetalle(rs.getInt("id_detalle"));
        detalle.setDescripcionServicio(rs.getString("descripcion_servicio"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
        detalle.setIdFactura(rs.getInt("id_factura"));
        return detalle;
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