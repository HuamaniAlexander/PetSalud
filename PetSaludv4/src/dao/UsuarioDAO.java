package dao;

import modelo.entidades.Usuario;
import modelo.Enumeraciones.RolUsuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends GenericDAO<Usuario> {

    @Override
    public Usuario crear(Usuario usuario) throws SQLException {
        String sql = "{CALL sp_crear_usuario(?, ?, ?, ?)}";
        CallableStatement cs = null;

        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, usuario.getNombreUsuario());
            cs.setString(2, usuario.getContrasena());
            cs.setString(3, usuario.getRol().name());
            cs.registerOutParameter(4, Types.INTEGER);

            cs.execute();
            usuario.setIdUsuario(cs.getInt(4));

            return usuario;
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
    }

    @Override
    public Usuario obtenerPorId(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Usuario> listarTodos() throws SQLException {
        String sql = "{CALL sp_listar_usuarios()}";
        CallableStatement cs = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();

        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            rs = cs.executeQuery();

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            return usuarios;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws SQLException {
        String sql = "{CALL sp_actualizar_usuario(?, ?, ?, ?, ?)}";
        CallableStatement cs = null;

        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, usuario.getIdUsuario());
            cs.setString(2, usuario.getNombreUsuario());
            cs.setString(3, usuario.getContrasena());
            cs.setString(4, usuario.getRol().name());
            cs.setBoolean(5, usuario.isActivo());

            cs.execute();
            return usuario;
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "{CALL sp_eliminar_usuario(?, ?)}";
        CallableStatement cs = null;

        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.VARCHAR);

            cs.execute();
            String resultado = cs.getString(2);

            if ("ELIMINADO_EXITOSAMENTE".equals(resultado)) {
                return true;
            } else if ("ERROR_FK_CONSTRAINT".equals(resultado)) {
                throw new SQLException("No se puede eliminar: El usuario tiene registros asociados (veterinarios o técnicos)");
            } else if ("USUARIO_NO_EXISTE".equals(resultado)) {
                throw new SQLException("El usuario no existe");
            }

            return false;
        } finally {
            if (cs != null) {
                cs.close();
            }
        }
    }

    public String verificarDependencias(int id) throws SQLException {
        String sql = "{CALL sp_verificar_dependencias_usuario(?)}";
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setInt(1, id);
            rs = cs.executeQuery();

            if (rs.next()) {
                int veterinarios = rs.getInt("veterinarios");
                int tecnicos = rs.getInt("tecnicos");

                if (veterinarios > 0 || tecnicos > 0) {
                    StringBuilder msg = new StringBuilder("El usuario tiene:\n");
                    if (veterinarios > 0) {
                        msg.append("• ").append(veterinarios).append(" veterinario(s) asociado(s)\n");
                    }
                    if (tecnicos > 0) {
                        msg.append("• ").append(tecnicos).append(" técnico(s) asociado(s)\n");
                    }
                    return msg.toString();
                }
            }
            return null; 
        } finally {
            cerrarRecursos(rs, cs);
        }
    }

    public Usuario autenticar(String nombreUsuario, String contrasena) throws SQLException {
        String sql = "{CALL sp_autenticar_usuario(?, ?)}";
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            Connection conn = getConnection();
            cs = conn.prepareCall(sql);
            cs.setString(1, nombreUsuario);
            cs.setString(2, contrasena);
            rs = cs.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } finally {
            cerrarRecursos(rs, cs);
        }
    }

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

    private void cerrarRecursos(ResultSet rs, CallableStatement cs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (cs != null) {
                cs.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
