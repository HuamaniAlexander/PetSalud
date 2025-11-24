package dao;

import modelo.entidades.Mascota;
import modelo.Enumeraciones.SexoMascota;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Mascota usando Procedimientos Almacenados
 */
public class MascotaDAO extends GenericDAO<Mascota> {
    
    @Override
    public Mascota crear(Mascota mascota) throws SQLException {
        String sql = "{CALL sp_crear_mascota(?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, mascota.getNombre());
            cs.setString(2, mascota.getEspecie());
            cs.setString(3, mascota.getRaza());
            cs.setInt(4, mascota.getEdad());
            cs.setString(5, mascota.getSexo().name());
            cs.setDouble(6, mascota.getPeso());
            cs.setInt(7, mascota.getIdDueno());
            cs.registerOutParameter(8, Types.INTEGER);
            
            cs.execute();
            mascota.setIdMascota(cs.getInt(8));
            
            return mascota;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public Mascota obtenerPorId(int id) throws SQLException {
        String sql = "{CALL sp_obtener_mascota_por_id(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearMascota(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public List<Mascota> listarTodos() throws SQLException {
        String sql = "{CALL sp_listar_mascotas()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Mascota> mascotas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                mascotas.add(mapearMascota(rs));
            }
            return mascotas;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public Mascota actualizar(Mascota mascota) throws SQLException {
        String sql = "{CALL sp_actualizar_mascota(?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, mascota.getIdMascota());
            cs.setString(2, mascota.getNombre());
            cs.setString(3, mascota.getEspecie());
            cs.setString(4, mascota.getRaza());
            cs.setInt(5, mascota.getEdad());
            cs.setString(6, mascota.getSexo().name());
            cs.setDouble(7, mascota.getPeso());
            
            cs.execute();
            return mascota;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        return false;
    }
    
    public List<Mascota> listarPorDueno(int idDueno) throws SQLException {
        String sql = "{CALL sp_listar_mascotas_por_dueno(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Mascota> mascotas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, idDueno);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                mascotas.add(mapearMascota(rs));
            }
            return mascotas;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public List<Mascota> buscarPorNombre(String nombre) throws SQLException {
        String sql = "{CALL sp_buscar_mascota_por_nombre(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Mascota> mascotas = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, nombre);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                mascotas.add(mapearMascota(rs));
            }
            return mascotas;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
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
    
    private void cerrarRecursos(ResultSet rs, CallableStatement cs) {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}