package dao;

import modelo.entidades.Veterinario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Veterinario usando Procedimientos Almacenados
 */
public class VeterinarioDAO extends GenericDAO<Veterinario> {
    
    @Override
    public Veterinario crear(Veterinario veterinario) throws SQLException {
        String sql = "{CALL sp_crear_veterinario(?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, veterinario.getNombres());
            cs.setString(2, veterinario.getApellidos());
            cs.setString(3, veterinario.getEspecialidad());
            cs.setString(4, veterinario.getTelefono());
            cs.setString(5, veterinario.getEmail());
            cs.setString(6, veterinario.getColegiatura());
            
            if (veterinario.getIdUsuario() != null) {
                cs.setInt(7, veterinario.getIdUsuario());
            } else {
                cs.setNull(7, Types.INTEGER);
            }
            
            cs.registerOutParameter(8, Types.INTEGER);
            
            cs.execute();
            veterinario.setIdVeterinario(cs.getInt(8));
            
            return veterinario;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public Veterinario obtenerPorId(int id) throws SQLException {
        String sql = "{CALL sp_obtener_veterinario_por_id(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearVeterinario(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public List<Veterinario> listarTodos() throws SQLException {
        String sql = "{CALL sp_listar_veterinarios()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Veterinario> veterinarios = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                veterinarios.add(mapearVeterinario(rs));
            }
            return veterinarios;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public Veterinario actualizar(Veterinario veterinario) throws SQLException {
        String sql = "{CALL sp_actualizar_veterinario(?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, veterinario.getIdVeterinario());
            cs.setString(2, veterinario.getNombres());
            cs.setString(3, veterinario.getApellidos());
            cs.setString(4, veterinario.getEspecialidad());
            cs.setString(5, veterinario.getTelefono());
            cs.setString(6, veterinario.getEmail());
            cs.setString(7, veterinario.getColegiatura());
            
            cs.execute();
            return veterinario;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        // No se implementa eliminación física por restricciones de integridad
        return false;
    }
    
    public List<Veterinario> buscarPorNombre(String nombre) throws SQLException {
        String sql = "{CALL sp_buscar_veterinario_por_nombre(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Veterinario> veterinarios = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, nombre);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                veterinarios.add(mapearVeterinario(rs));
            }
            return veterinarios;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    public Veterinario buscarPorColegiatura(String colegiatura) throws SQLException {
        String sql = "{CALL sp_buscar_veterinario_por_colegiatura(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, colegiatura);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearVeterinario(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    private Veterinario mapearVeterinario(ResultSet rs) throws SQLException {
        Veterinario veterinario = new Veterinario();
        veterinario.setIdVeterinario(rs.getInt("id_veterinario"));
        veterinario.setNombres(rs.getString("nombres"));
        veterinario.setApellidos(rs.getString("apellidos"));
        veterinario.setEspecialidad(rs.getString("especialidad"));
        veterinario.setTelefono(rs.getString("telefono"));
        veterinario.setEmail(rs.getString("email"));
        veterinario.setColegiatura(rs.getString("colegiatura"));
        
        try {
            int idUsuario = rs.getInt("id_usuario");
            if (!rs.wasNull()) {
                veterinario.setIdUsuario(idUsuario);
            }
        } catch (SQLException e) {
            // Campo puede ser NULL
        }
        
        veterinario.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return veterinario;
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