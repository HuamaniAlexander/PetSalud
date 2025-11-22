package dao;

import modelo.entidades.Usuario;
import modelo.Enumeraciones.RolUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para Usuario - Gestion de usuarios y autenticacion
 */
public class UsuarioDAO extends GenericDAO<Usuario> {
    
    @Override
    public Usuario crear(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nombre_usuario, contrasena, rol, activo) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getRol().name());
            ps.setBoolean(4, usuario.isActivo());
            
            ps.executeUpdate();
            usuario.setIdUsuario(obtenerUltimoId(ps));
            
            return usuario;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public Usuario obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM usuario ORDER BY nombre_usuario";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            return usuarios;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    @Override
    public Usuario actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nombre_usuario = ?, contrasena = ?, rol = ?, activo = ? WHERE id_usuario = ?";
        PreparedStatement ps = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getRol().name());
            ps.setBoolean(4, usuario.isActivo());
            ps.setInt(5, usuario.getIdUsuario());
            
            ps.executeUpdate();
            return usuario;
        } finally {
            cerrarRecursos(ps);
        }
    }
    
    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
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
    
    // Metodo especifico: Autenticar usuario
    public Usuario autenticar(String nombreUsuario, String contrasena) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE nombre_usuario = ? AND contrasena = ? AND activo = TRUE";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Metodo especifico: Buscar por nombre de usuario
    public Usuario buscarPorNombreUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE nombre_usuario = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            Connection conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, ps);
        }
    }
    
    // Mapear ResultSet a Usuario
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setRol(RolUsuario.valueOf(rs.getString("rol")));
        usuario.setActivo(rs.getBoolean("activo"));
        usuario.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return usuario;
    }
}