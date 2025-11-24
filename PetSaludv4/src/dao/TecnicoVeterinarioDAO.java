package dao;

import modelo.entidades.TecnicoVeterinario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para TecnicoVeterinario usando Procedimientos Almacenados
 */
public class TecnicoVeterinarioDAO extends GenericDAO<TecnicoVeterinario> {
    
    @Override
    public TecnicoVeterinario crear(TecnicoVeterinario tecnico) throws SQLException {
        String sql = "{CALL sp_crear_tecnico(?, ?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, tecnico.getNombres());
            cs.setString(2, tecnico.getApellidos());
            cs.setString(3, tecnico.getEspecialidad());
            cs.setString(4, tecnico.getTelefono());
            
            if (tecnico.getIdUsuario() != null) {
                cs.setInt(5, tecnico.getIdUsuario());
            } else {
                cs.setNull(5, Types.INTEGER);
            }
            
            cs.registerOutParameter(6, Types.INTEGER);
            
            cs.execute();
            tecnico.setIdTecnico(cs.getInt(6));
            
            return tecnico;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public TecnicoVeterinario obtenerPorId(int id) throws SQLException {
        String sql = "{CALL sp_obtener_tecnico_por_id(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            rs = cs.executeQuery();
            
            if (rs.next()) {
                return mapearTecnico(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public List<TecnicoVeterinario> listarTodos() throws SQLException {
        String sql = "{CALL sp_listar_tecnicos()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<TecnicoVeterinario> tecnicos = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                tecnicos.add(mapearTecnico(rs));
            }
            return tecnicos;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    @Override
    public TecnicoVeterinario actualizar(TecnicoVeterinario tecnico) throws SQLException {
        String sql = "{CALL sp_actualizar_tecnico(?, ?, ?, ?, ?)}";
        CallableStatement cs = null;
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, tecnico.getIdTecnico());
            cs.setString(2, tecnico.getNombres());
            cs.setString(3, tecnico.getApellidos());
            cs.setString(4, tecnico.getEspecialidad());
            cs.setString(5, tecnico.getTelefono());
            
            cs.execute();
            return tecnico;
        } finally {
            if (cs != null) cs.close();
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        // No se implementa eliminación física por restricciones de integridad
        return false;
    }
    
    public List<TecnicoVeterinario> buscarPorNombre(String nombre) throws SQLException {
        String sql = "{CALL sp_buscar_tecnico_por_nombre(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<TecnicoVeterinario> tecnicos = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, nombre);
            rs = cs.executeQuery();
            
            while (rs.next()) {
                tecnicos.add(mapearTecnico(rs));
            }
            return tecnicos;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }
    
    private TecnicoVeterinario mapearTecnico(ResultSet rs) throws SQLException {
        TecnicoVeterinario tecnico = new TecnicoVeterinario();
        tecnico.setIdTecnico(rs.getInt("id_tecnico"));
        tecnico.setNombres(rs.getString("nombres"));
        tecnico.setApellidos(rs.getString("apellidos"));
        tecnico.setEspecialidad(rs.getString("especialidad"));
        tecnico.setTelefono(rs.getString("telefono"));
        
        try {
            int idUsuario = rs.getInt("id_usuario");
            if (!rs.wasNull()) {
                tecnico.setIdUsuario(idUsuario);
            }
        } catch (SQLException e) {
            // Campo puede ser NULL
        }
        
        tecnico.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return tecnico;
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