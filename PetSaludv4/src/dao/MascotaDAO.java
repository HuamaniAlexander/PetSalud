package dao;

import modelo.entidades.Mascota;
import modelo.Enumeraciones.SexoMascota;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Mascota - Gestion de mascotas
 */
public class MascotaDAO extends GenericDAO<Mascota> {
    
    @Override
    public Mascota crear(Mascota mascota) throws SQLException {
        String sql = "INSERT INTO mascota (nombre, especie, raza, edad, sexo, peso, id_dueno) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, mascota.getNombre());
            ps.setString(2, mascota.getEspecie());
            ps.setString(3, mascota.getRaza());
            ps.setInt(4, mascota.getEdad());
            ps.setString(5, mascota.getSexo().name());
            ps.setDouble(6, mascota.getPeso());
            ps.setInt(7, mascota.getIdDueno());
            
            ps.executeUpdate();
            mascota.setIdMascota(obtenerUltimoId(ps));
            
            return mascota;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public Mascota obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM mascota WHERE id_mascota = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearMascota(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public List<Mascota> listarTodos() throws SQLException {
        String sql = "SELECT * FROM mascota ORDER BY nombre";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Mascota> mascotas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                mascotas.add(mapearMascota(rs));
            }
            return mascotas;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public Mascota actualizar(Mascota mascota) throws SQLException {
        String sql = "UPDATE mascota SET nombre = ?, especie = ?, raza = ?, edad = ?, sexo = ?, peso = ?, id_dueno = ? WHERE id_mascota = ?";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, mascota.getNombre());
            ps.setString(2, mascota.getEspecie());
            ps.setString(3, mascota.getRaza());
            ps.setInt(4, mascota.getEdad());
            ps.setString(5, mascota.getSexo().name());
            ps.setDouble(6, mascota.getPeso());
            ps.setInt(7, mascota.getIdDueno());
            ps.setInt(8, mascota.getIdMascota());
            
            ps.executeUpdate();
            return mascota;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM mascota WHERE id_mascota = ?";
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
    
    // Metodo especifico: Listar mascotas por dueno
    public List<Mascota> listarPorDueno(int idDueno) throws SQLException {
        String sql = "SELECT * FROM mascota WHERE id_dueno = ? ORDER BY nombre";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Mascota> mascotas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDueno);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                mascotas.add(mapearMascota(rs));
            }
            return mascotas;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Metodo especifico: Buscar por nombre
    public List<Mascota> buscarPorNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM mascota WHERE nombre LIKE ? ORDER BY nombre";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Mascota> mascotas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();
            
            while (rs.next()) {
                mascotas.add(mapearMascota(rs));
            }
            return mascotas;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Mapear ResultSet a Mascota
    private Mascota mapearMascota(ResultSet rs) throws SQLException {
        Mascota mascota = new Mascota();
        mascota.setIdMascota(rs.getInt("id_mascota"));
        mascota.setNombre(rs.getString("nombre"));
        mascota.setEspecie(rs.getString("especie"));
        mascota.setRaza(rs.getString("raza"));
        mascota.setEdad(rs.getInt("edad"));
        mascota.setSexo(SexoMascota.valueOf(rs.getString("sexo")));
        mascota.setPeso(rs.getDouble("peso"));
        mascota.setIdDueno(rs.getInt("id_dueno"));
        mascota.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return mascota;
    }
}