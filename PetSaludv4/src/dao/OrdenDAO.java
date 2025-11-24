package dao;

import modelo.entidades.OrdenVeterinaria;
import modelo.Enumeraciones.TipoExamen;
import modelo.Enumeraciones.EstadoOrden;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para OrdenVeterinaria usando Procedimientos Almacenados
 */
public class OrdenDAO extends GenericDAO<OrdenVeterinaria> {
    
    @Override
    public OrdenVeterinaria crear(OrdenVeterinaria orden) throws SQLException {
        String sql = "{CALL sp_crear_orden(?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, orden.getTipoExamen().name());
            cs.setString(2, orden.getObservaciones());
            cs.setInt(3, orden.getIdMascota());
            cs.setInt(4, orden.getIdVeterinario());
            cs.registerOutParameter(5, Types.INTEGER);
            
            cs.execute();
            orden.setIdOrden(cs.getInt(5));
            
            return orden;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public OrdenVeterinaria obtenerPorId(int id) throws SQLException {
        String sql = "{CALL sp_obtener_orden_por_id(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearOrden(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public List<OrdenVeterinaria> listarTodos() throws SQLException {
        String sql = "{CALL sp_listar_todas_ordenes()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<OrdenVeterinaria> ordenes = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
            return ordenes;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public OrdenVeterinaria actualizar(OrdenVeterinaria orden) throws SQLException {
        String sql = "{CALL sp_actualizar_estado_orden(?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, orden.getIdOrden());
            cs.setString(2, orden.getEstado().name());
            
            cs.execute();
            return orden;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        return false;
    }
    
    public List<OrdenVeterinaria> listarPorEstado(EstadoOrden estado) throws SQLException {
        String sql = "{CALL sp_listar_ordenes_por_estado(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<OrdenVeterinaria> ordenes = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, estado.name());
            rs = cs.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
            return ordenes;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public List<OrdenVeterinaria> listarPorMascota(int idMascota) throws SQLException {
        String sql = "{CALL sp_listar_ordenes_por_mascota(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<OrdenVeterinaria> ordenes = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, idMascota);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
            return ordenes;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    private OrdenVeterinaria mapearOrden(ResultSet rs) throws SQLException {
        OrdenVeterinaria orden = new OrdenVeterinaria();
        orden.setIdOrden(rs.getInt("id_orden"));
        orden.setFechaOrden(rs.getTimestamp("fecha_orden"));
        orden.setTipoExamen(TipoExamen.valueOf(rs.getString("tipo_examen")));
        orden.setObservaciones(rs.getString("observaciones"));
        orden.setEstado(EstadoOrden.valueOf(rs.getString("estado")));
        orden.setIdMascota(rs.getInt("id_mascota"));
        orden.setIdVeterinario(rs.getInt("id_veterinario"));
        orden.setFechaActualizacion(rs.getTimestamp("fecha_actualizacion"));
        return orden;
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