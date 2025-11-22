package dao;

import modelo.entidades.Dueno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Dueno - Gestion de propietarios de mascotas
 */
public class DuenoDAO extends GenericDAO<Dueno> {
    
    @Override
    public Dueno crear(Dueno dueno) throws SQLException {
        String sql = "INSERT INTO dueno (dni, nombres, apellidos, telefono, email, direccion) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dueno.getDni());
            ps.setString(2, dueno.getNombres());
            ps.setString(3, dueno.getApellidos());
            ps.setString(4, dueno.getTelefono());
            ps.setString(5, dueno.getEmail());
            ps.setString(6, dueno.getDireccion());
            
            ps.executeUpdate();
            dueno.setIdDueno(obtenerUltimoId(ps));
            
            return dueno;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public Dueno obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM dueno WHERE id_dueno = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearDueno(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public List<Dueno> listarTodos() throws SQLException {
        String sql = "SELECT * FROM dueno ORDER BY apellidos, nombres";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Dueno> duenos = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                duenos.add(mapearDueno(rs));
            }
            return duenos;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public Dueno actualizar(Dueno dueno) throws SQLException {
        String sql = "UPDATE dueno SET dni = ?, nombres = ?, apellidos = ?, telefono = ?, email = ?, direccion = ? WHERE id_dueno = ?";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, dueno.getDni());
            ps.setString(2, dueno.getNombres());
            ps.setString(3, dueno.getApellidos());
            ps.setString(4, dueno.getTelefono());
            ps.setString(5, dueno.getEmail());
            ps.setString(6, dueno.getDireccion());
            ps.setInt(7, dueno.getIdDueno());
            
            ps.executeUpdate();
            return dueno;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM dueno WHERE id_dueno = ?";
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
    
    // Metodo especifico: Buscar por DNI
    public Dueno buscarPorDNI(String dni) throws SQLException {
        String sql = "SELECT * FROM dueno WHERE dni = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, dni);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearDueno(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Metodo especifico: Buscar por nombre
    public List<Dueno> buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM dueno WHERE CONCAT(nombres, ' ', apellidos) LIKE ? ORDER BY apellidos, nombres";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Dueno> duenos = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                duenos.add(mapearDueno(rs));
            }
            return duenos;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Mapear ResultSet a Dueno
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
}