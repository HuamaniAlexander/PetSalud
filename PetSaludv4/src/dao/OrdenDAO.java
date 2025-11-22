package dao;

import modelo.entidades.OrdenVeterinaria;
import modelo.Enumeraciones.TipoExamen;
import modelo.Enumeraciones.EstadoOrden;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para OrdenVeterinaria - Gestion de ordenes de analisis
 */
public class OrdenDAO extends GenericDAO<OrdenVeterinaria> {
    
    @Override
    public OrdenVeterinaria crear(OrdenVeterinaria orden) throws SQLException {
        String sql = "INSERT INTO orden_veterinaria (tipo_examen, observaciones, estado, id_mascota, id_veterinario) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, orden.getTipoExamen().name());
            ps.setString(2, orden.getObservaciones());
            ps.setString(3, orden.getEstado().name());
            ps.setInt(4, orden.getIdMascota());
            ps.setInt(5, orden.getIdVeterinario());
            
            ps.executeUpdate();
            orden.setIdOrden(obtenerUltimoId(ps));
            
            return orden;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public OrdenVeterinaria obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM orden_veterinaria WHERE id_orden = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearOrden(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public List<OrdenVeterinaria> listarTodos() throws SQLException {
        String sql = "SELECT * FROM orden_veterinaria ORDER BY fecha_orden DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OrdenVeterinaria> ordenes = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
            return ordenes;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public OrdenVeterinaria actualizar(OrdenVeterinaria orden) throws SQLException {
        String sql = "UPDATE orden_veterinaria SET tipo_examen = ?, observaciones = ?, estado = ?, id_mascota = ?, id_veterinario = ? WHERE id_orden = ?";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, orden.getTipoExamen().name());
            ps.setString(2, orden.getObservaciones());
            ps.setString(3, orden.getEstado().name());
            ps.setInt(4, orden.getIdMascota());
            ps.setInt(5, orden.getIdVeterinario());
            ps.setInt(6, orden.getIdOrden());
            
            ps.executeUpdate();
            return orden;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM orden_veterinaria WHERE id_orden = ?";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            return ps.executeUpdate() > 0;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    // Metodo especifico: Listar por estado
    public List<OrdenVeterinaria> listarPorEstado(EstadoOrden estado) throws SQLException {
        String sql = "SELECT * FROM orden_veterinaria WHERE estado = ? ORDER BY fecha_orden DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OrdenVeterinaria> ordenes = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, estado.name());
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
            return ordenes;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Metodo especifico: Listar por mascota
    public List<OrdenVeterinaria> listarPorMascota(int idMascota) throws SQLException {
        String sql = "SELECT * FROM orden_veterinaria WHERE id_mascota = ? ORDER BY fecha_orden DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OrdenVeterinaria> ordenes = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idMascota);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
            return ordenes;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Metodo especifico: Listar por veterinario
    public List<OrdenVeterinaria> listarPorVeterinario(int idVeterinario) throws SQLException {
        String sql = "SELECT * FROM orden_veterinaria WHERE id_veterinario = ? ORDER BY fecha_orden DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OrdenVeterinaria> ordenes = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idVeterinario);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                ordenes.add(mapearOrden(rs));
            }
            return ordenes;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Mapear ResultSet a OrdenVeterinaria
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
}