package dao;

import modelo.entidades.Dueno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Dueno usando Procedimientos Almacenados
 */
public class DuenoDAO extends GenericDAO<Dueno> {
    
    @Override
    public Dueno crear(Dueno dueno) throws SQLException {
        String sql = "{CALL sp_crear_dueno(?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, dueno.getDni());
            cs.setString(2, dueno.getNombres());
            cs.setString(3, dueno.getApellidos());
            cs.setString(4, dueno.getTelefono());
            cs.setString(5, dueno.getEmail());
            cs.setString(6, dueno.getDireccion());
            cs.registerOutParameter(7, Types.INTEGER);
            
            cs.execute();
            dueno.setIdDueno(cs.getInt(7));
            
            return dueno;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public Dueno obtenerPorId(int id) throws SQLException {
        String sql = "{CALL sp_obtener_dueno_por_id(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearDueno(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public List<Dueno> listarTodos() throws SQLException {
        String sql = "{CALL sp_listar_duenos()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Dueno> duenos = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                duenos.add(mapearDueno(rs));
            }
            return duenos;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public Dueno actualizar(Dueno dueno) throws SQLException {
        String sql = "{CALL sp_actualizar_dueno(?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, dueno.getIdDueno());
            cs.setString(2, dueno.getDni());
            cs.setString(3, dueno.getNombres());
            cs.setString(4, dueno.getApellidos());
            cs.setString(5, dueno.getTelefono());
            cs.setString(6, dueno.getEmail());
            cs.setString(7, dueno.getDireccion());
            
            cs.execute();
            return dueno;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        // No se implementa eliminación física por restricciones de integridad
        return false;
    }
    
    public Dueno buscarPorDNI(String dni) throws SQLException {
        String sql = "{CALL sp_buscar_dueno_por_dni(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, dni);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearDueno(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public List<Dueno> buscarPorNombre(String nombre) throws SQLException {
        String sql = "{CALL sp_buscar_dueno_por_nombre(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Dueno> duenos = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, nombre);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                duenos.add(mapearDueno(rs));
            }
            return duenos;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    private Dueno mapearDueno(ResultSet rs) throws SQLException {
        Dueno dueno = new Dueno();
        dueno.setIdDueno(rs.getInt("id_dueno"));
        dueno.setDni(rs.getString("dni"));
        dueno.setNombres(rs.getString("nombres"));
        dueno.setApellidos(rs.getString("apellidos"));
        dueno.setTelefono(rs.getString("telefono"));
        dueno.setEmail(rs.getString("email"));
        dueno.setDireccion(rs.getString("direccion"));
        dueno.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return dueno;
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